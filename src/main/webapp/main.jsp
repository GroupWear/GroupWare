<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*, com.groupware.dto.*, com.groupware.dao.*" %>
<%
    // [1순위 고정] 자바 로직이 돌기 전, 요청과 응답 스트림의 구멍을 UTF-8로 선제 타격해서 열어둡니다.
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- [2순위 고정] JSTL이 브라우저 언어헤더를 읽어 동적으로 프로퍼티를 선택할 때, 깨지지 않도록 인코딩 필터 주입 --%>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="resources.message" scope="session" />
<%
    /* =========================================================================
     * [Step 1] 사용자 세션 체크 및 실시간 정보 동기화 로직
     * ========================================================================= */
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp"); 
        return;
    }
    // 실시간 연차 갱신 로직
    EmployeeDAO empDao = new EmployeeDAO();
    EmployeeDTO updatedEmp = empDao.getEmployeeByNo(String.valueOf(loginEmp.getEmpNo()));
    if (updatedEmp != null) {
        session.setAttribute("loginEmp", updatedEmp); 
        loginEmp = updatedEmp; 
    }
    // 페이징 변수 (Controller에서 세팅된 값 사용)
    int currentEmpNo = loginEmp.getEmpNo();              
    boolean isManagerMode = "Y".equals(loginEmp.getManager()); 
    LocalDateTime currentDateTime = LocalDateTime.now();       
    String currentMapping = request.getRequestURI();           

    /* =========================================================================
     * [Step 2] 삼분할 다중 페이징 규격 변수 세팅 (Pagination Config)
     * ========================================================================= */
    final int RECORDS_PER_PAGE = 5; 
    final int PAGES_PER_BLOCK = 5;  
    // 현재 요청된 페이지 파라미터 받기 (기본값 1)
    int resPage = 1;
    String resPageParam = request.getParameter("resPage");
    if (resPageParam != null && !resPageParam.isEmpty()) resPage = Integer.parseInt(resPageParam);

    int rentalPage = 1;
    String rentalPageParam = request.getParameter("rentalPage");
    if (rentalPageParam != null && !rentalPageParam.isEmpty()) rentalPage = Integer.parseInt(rentalPageParam);

    int leavePage = 1;
    String leavePageParam = request.getParameter("leavePage");
    if (leavePageParam != null && !leavePageParam.isEmpty()) leavePage = Integer.parseInt(leavePageParam);

    /* =========================================================================
     * [Step 3] 비즈니스 데이터 연산 및 페이지 조각화 (SubList 처리)
     * ========================================================================= */
    
    // 3-1. 회의실 예약 현황 데이터 수집 및 정렬/페이징
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> rawReserveList = resDao.getMyReservations(currentEmpNo); 
    List<Map<String, Object>> fullReserveList = new ArrayList<>();

    // raw 디티오 리스트를 정렬이 가능한 Map 리스트로 먼저 가공하면서 이용종료 상태 판별
    if (rawReserveList != null) {
        for (ReservationDTO dto : rawReserveList) {
            Map<String, Object> m = new HashMap<>();
            m.put("resNo", dto.getResNo());
            m.put("roomId", dto.getRoomId());
            m.put("resDate", dto.getResDate() != null ? dto.getResDate().toString() : "");
            m.put("startTime", dto.getStartTime());
            m.put("endTime", dto.getEndTime());
            m.put("purpose", dto.getPurpose());
            
            String displayStatus = dto.getStatus();
            if ("예약완료".equals(displayStatus)) {
                try {
                    LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                    LocalTime eTime = LocalTime.parse(dto.getEndTime());
                    if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                        displayStatus = "이용 종료";
                    }
                } catch (Exception e) {}
            }
            m.put("status", displayStatus);
            m.put("rawDto", dto); // 달력 컴포넌트 등 백업 백업용 원본 저장
            
            fullReserveList.add(m);
        }
    }

    // 상태별 우선순위 정렬 (예약완료 = 1등, 이용 종료/취소됨 = 2등)
    if (!fullReserveList.isEmpty()) {
        Collections.sort(fullReserveList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                String s1 = (String) m1.get("status");
                String s2 = (String) m2.get("status");
                
                int p1 = "예약완료".equals(s1) ? 1 : 2;
                int p2 = "예약완료".equals(s2) ? 1 : 2;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1우선순위: 상태 비교
                }
                
                // 2우선순위: 상태가 같으면 날짜 오름차순 정렬
                String d1 = (String) m1.get("resDate");
                String d2 = (String) m2.get("resDate");
                return d1.compareTo(d2);
            }
        });
    }
    
    // 정렬이 완료된 상태에서 페이징 데이터 쪼개기 진행
    int resTotalCount = fullReserveList.size();
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
    
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
    
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
    
    // 최종 화면 테이블에 출력될 5개의 행 조각 리스트 생성
    List<Map<String, Object>> resSubList = (resStartIdx < resTotalCount) 
                                          ? fullReserveList.subList(resStartIdx, resEndIdx) : null;

    // =========================================================================
    // 3-2. 비품 대여 데이터 계산 및 상태별 우선순위 정렬
    // =========================================================================
    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> rawRentalList = isManagerMode ? rentalDao.getAllDocumentList() : rentalDao.getMyRentalList(currentEmpNo);
    List<RentalHistoryDTO> filteredRentalList = new ArrayList<>();
    
    if (rawRentalList != null) {
        for (RentalHistoryDTO item : rawRentalList) {
            if (isManagerMode) {
                boolean isRetired = (item.getEmpLevel() == 0); 
                boolean isMyDoc = (item.getEmpNo() == currentEmpNo);
                boolean isTargetRetired = isRetired && ("대여중".equals(item.getStatus()) || "미반납".equals(item.getStatus()));
                if (!isTargetRetired && !isMyDoc) continue; 
            }
            filteredRentalList.add(item);
        }
    }
    
    // 비품 대여 상태별 우선순위 정렬 로직
    if (!filteredRentalList.isEmpty()) {
        Collections.sort(filteredRentalList, new Comparator<RentalHistoryDTO>() {
            @Override
            public int compare(RentalHistoryDTO r1, RentalHistoryDTO r2) {
                String s1 = r1.getStatus();
                String s2 = r2.getStatus();
                
                // 상태별 우선순위 점수 매기기 (낮을수록 상단 노출)
                int p1 = 3; // 기본값 (반납완료, 반려됨 등)
                if ("대여중".equals(s1)) p1 = 0;
                else if ("미반납".equals(s1)) p1 = 1;
                else if ("승인대기".equals(s1)) p1 = 2;
                
                int p2 = 3;
                if ("대여중".equals(s2)) p2 = 0;
                else if ("미반납".equals(s2)) p2 = 1;
                else if ("승인대기".equals(s2)) p2 = 2;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1우선순위: 상태 점수 비교
                }
                
                // 2우선순위: 상태가 같으면 대여 시작일(RentalDate) 기준 최신순(내림차순) 정렬
                String d1 = r1.getRentalDate() != null ? r1.getRentalDate().toString() : "";
                String d2 = r2.getRentalDate() != null ? r2.getRentalDate().toString() : "";
                return d2.compareTo(d1); // 최신 날짜가 위로 오도록 d2와 d1 순서 변경
            }
        });
    }
    
    // 정렬이 완료된 filteredRentalList를 가지고 페이징 쪼개기 진행
    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;
    
    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);
    
    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    
    // 최종 화면에 출력될 5개의 정렬된 대여 행
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount) ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx) : null;  
            
    /* =========================================================================
     * [Step 3-3] 내 휴가 신청 데이터 수집 및 상태별 우선순위 정렬
     * ========================================================================= */
    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    
    // 휴가 신청 상태별 우선순위 (승인대기 = 0점, 승인완료 = 1점, 반려됨 = 2점)
    if (fullLeaveList != null && !fullLeaveList.isEmpty()) {
        Collections.sort(fullLeaveList, new Comparator<LeaveHistoryDTO>() {
            @Override
            public int compare(LeaveHistoryDTO l1, LeaveHistoryDTO l2) {
                String s1 = l1.getStatus();
                String s2 = l2.getStatus();
                
                // 상태별 우선순위 점수 매기기 (낮을수록 상단 노출)
                int p1 = 2; // 기본값 (반려됨 등)
                if ("승인대기".equals(s1)) p1 = 0;
                else if ("승인완료".equals(s1)) p1 = 1;
                
                int p2 = 2;
                if ("승인대기".equals(s2)) p2 = 0;
                else if ("승인완료".equals(s2)) p2 = 1;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1우선순위: 상태 점수 비교
                }
                
                // 2우선순위: 상태가 같으면 휴가 시작일(StartDate) 기준 최신순(내림차순) 정렬
                String d1 = l1.getStartDate() != null ? l1.getStartDate().toString() : "";
                String d2 = l2.getStartDate() != null ? l2.getStartDate().toString() : "";
                return d2.compareTo(d1); // 최신 날짜가 위로 오도록 d2와 d1 순서 변경
            }
        });
    }
    
    // 정렬이 완료된 상태에서 페이징 쪼개기 진행
    int leaveTotalCount = (fullLeaveList != null) ? fullLeaveList.size() : 0;
    int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / RECORDS_PER_PAGE);
    if (leaveTotalPages == 0) leaveTotalPages = 1;
    
    int leaveStartPage = ((leavePage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int leaveEndPage = Math.min(leaveStartPage + PAGES_PER_BLOCK - 1, leaveTotalPages);
    
    int leaveStartIdx = (leavePage - 1) * RECORDS_PER_PAGE;
    int leaveEndIdx = Math.min(leaveStartIdx + RECORDS_PER_PAGE, leaveTotalCount);
    
    // 최종 화면에 출력될 5개의 정렬된 휴가 행
    List<LeaveHistoryDTO> leaveList = (fullLeaveList != null && leaveStartIdx < leaveTotalCount) ? fullLeaveList.subList(leaveStartIdx, leaveEndIdx) : null;
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="dashboard.title" /></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js'></script>
	<script>
	document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: { left: 'prev,next', center: 'title', right: '' },
            height: 'auto',
            dayMaxEvents: 2, 
            events: [
                <% if (fullReserveList != null) {
                    boolean isFirst = true;
                    for (Map<String, Object> map : fullReserveList) {
                        if (!"취소됨".equals(map.get("status"))) {
                            if (!isFirst) { out.print(","); } isFirst = false;
                %>
                {
                    title: '[<%=map.get("roomId")%>호]\n<%=map.get("startTime")%> ~ <%=map.get("endTime")%>',
                    start: '<%=map.get("resDate")%>',
                    extendedProps: {
                        purpose: '<%=((String)map.get("purpose")).replace("'", "\\'")%>'
                    },
                    backgroundColor: '#4e73df', 
                    borderColor: '#4e73df'
                }
                <%      } 
                    } 
                } %>
            ],
            eventClick: function(info) {
                alert("<fmt:message key="alert.cal.title" />" + info.event.title.replace('\n', ' ') + "<fmt:message key="alert.cal.purpose" />" +
                      info.event.extendedProps.purpose);
            }
        });
        calendar.render();
    });
	</script>
	<script>
        function returnProcess(rentalNo) { 
            if (confirm("<fmt:message key="alert.return.confirm" />")) {
                saveScroll(); 
                location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; 
            }
        }
        
        function cancelReserve(resNo) { 
            if (confirm("<fmt:message key="alert.cancel.confirm" />")) {
                saveScroll(); 
                location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; 
            }
        }

        function navigateWithScroll(url) {
            saveScroll();
            location.href = url;
        }
        
        function saveScroll() {
            localStorage.setItem("main_scroll_y", window.scrollY);
        }

        window.addEventListener("DOMContentLoaded", function() {
            var savedScrollY = localStorage.getItem("main_scroll_y");
            if (savedScrollY !== null) {
                window.scrollTo({
                    top: parseInt(savedScrollY),
                    behavior: 'instant' 
                });
                localStorage.removeItem("main_scroll_y");
            }
        });
    </script>
</head>
<body>

    <jsp:include page="header.jsp" />

    <div class="dashboard-container">
        
        <div class="info-card">
            <div class="welcome-box">
                <%-- {0}자리에 유동적으로 사원명을 주입하여 환영 텍스트 다국어화 --%>
                <h3><fmt:message key="dashboard.welcome"><fmt:param value="<%=loginEmp.getEmpName()%>"/></fmt:message></h3>
                <p><fmt:message key="dashboard.dept"><fmt:param value="<%=loginEmp.getDept()%>"/></fmt:message></p>
            </div>
            <div class="leave-box">
                <p class="leave-label"><fmt:message key="dashboard.leave.label" /></p>
                <div class="leave-count">
                    <span class="current"><%=loginEmp.getCurLeave()%></span>
                    <span class="divider">/</span>
                    <span class="total"><%=loginEmp.getMaxLeave()%></span>
                </div>
            </div>
        </div>
        
        <div class="container">

			<div class="dashboard-grid">
				<div class="calendar-section">
					<div id="calendar"></div>
				</div>

			<div class="section-title"> <a href="myReservation.jsp" class="data-link"><fmt:message key="table.res.title" /></a></div>
            <div class="table-wrapper">
                <table class="table-res">
                    <thead>
                        <tr>
                            <th><fmt:message key="table.res.no" /></th>
                            <th><fmt:message key="table.res.room" /></th>
                            <th><fmt:message key="table.res.date" /></th>
                            <th><fmt:message key="table.res.time" /></th>
                            <th><fmt:message key="table.res.purpose" /></th>
                            <th><fmt:message key="table.res.status" /></th>
                            <th><fmt:message key="table.res.note" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (resSubList == null || resSubList.isEmpty()) { %>
                            <tr><td colspan="7" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.res.empty" /></td></tr>
                        <% } else {
                               for (Map<String, Object> resMap : resSubList) {
                                   String displayStatus = (String) resMap.get("status");
                                   String statusClass = "bg-primary"; 

                                   if ("이용 종료".equals(displayStatus)) {
                                       statusClass = "bg-secondary"; 
                                   } else if ("취소됨".equals(displayStatus)) {
                                       statusClass = "bg-danger"; 
                                   }
                                   
                                   // 상태 컬럼 다국어 치환 처리
                                   String langStatus = displayStatus;
                                   if("예약완료".equals(displayStatus)) langStatus = "예약완료"; // 한글 properties 맵핑 유지가능하나 직접 노출도 가능
                                   // 만약 상태명 자체도 하드코딩 교체가 필요하다면 bundle에서 직접 꺼내거나 분기해도 좋으나 우선 DB바인딩 상태명 유지
                        %>
                        <tr>
                                <td style="color: #6c757d;"><%=resMap.get("resNo")%></td>
                                <td style="font-weight: 600; color: #343a40;"><%=resMap.get("roomId")%>호</td>
                                <td><%=resMap.get("resDate")%></td>
                                <td><%=resMap.get("startTime")%> ~ <%=resMap.get("endTime")%></td>
                                <td><span class="title-link" title="<%=resMap.get("purpose")%>"><%=resMap.get("purpose")%></span></td>
                                <td><span class="status-badge <%=statusClass%>"><%=langStatus%></span></td>
                                <td>
                                    <% if ("예약완료".equals(displayStatus)) { %>
                                        <button class="btn-action" onclick="cancelReserve(<%=resMap.get("resNo")%>)"><fmt:message key="table.res.btn.cancel" /></button> 
                                    <% } else { %> 
                                        <span style="color: #ced4da;">-</span> 
                                    <% } %>
                                </td>
                            </tr>
                    <%     }
                       } %>
                    </tbody>
                </table>
            </div>
            
            <div class="pagination-container">
                <% if (resStartPage > 1) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resStartPage-1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = resStartPage; i <= resEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == resPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=i%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (resEndPage < resTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resEndPage+1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>


            <div class="section-title">
                <a href="myRental.jsp" class="data-link">
                    <%-- 관리자모드 유무에 따른 상단 텍스트 번들 처리 --%>
                    <fmt:message key="<%= isManagerMode ? \"table.rental.title.admin\" : \"table.rental.title.user\" %>" />
                </a>
            </div>
            <div class="table-wrapper">
                <table class="table-rental <%= isManagerMode ? "admin-view" : "" %>">
                    <thead>
                        <tr>
                            <th><fmt:message key="table.rental.no" /></th>
                            <% if (isManagerMode) { %> <th><fmt:message key="table.rental.writer" /></th> <% } %>
                            <th><fmt:message key="table.rental.title" /></th>
                            <th><fmt:message key="table.rental.date" /></th>
                            <th><fmt:message key="table.rental.status" /></th>
                            <th><fmt:message key="table.rental.note" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (rentalList == null || rentalList.isEmpty()) { %>
                        <tr><td colspan="<%= isManagerMode ? 6 : 5 %>" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.rental.empty" /></td></tr>
                    <% } else {
                               for (RentalHistoryDTO item : rentalList) {
                                   String displayStatus = item.getStatus();
                                   boolean isRetiredCreator = (item.getEmpLevel() == 0);
                                   
                                   String badgeClass = "bg-secondary";
                                   if ("승인대기".equals(displayStatus)) badgeClass = "bg-warning";
                                   else if ("대여중".equals(displayStatus)) badgeClass = "bg-success";
                                   else if ("반려됨".equals(displayStatus) || "미반납".equals(displayStatus)) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td><%=item.getRentalNo()%></td>
                            <% if (isManagerMode) { %>
                                <td style="font-weight: 600; color: #64748b;">
                                    <%=item.getEmpName()%> 
                                    <fmt:message key="<%= isRetiredCreator ? \"table.rental.retired\" : \"table.rental.myself\" %>" var="empType"/>
                                    <br>(${empType})
                                </td>
                            <% } %>
                            <td>
                                <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" 
                                   style="text-decoration: none; color: #6366f1; font-weight: 600; display: block;">
                                    <span class="title-link" title="<%=item.getTitle()%>">
                                        <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                                    </span>
                                </a>
                            </td>
                            <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><%=displayStatus%></span></td>
                            <td>
                                <% if ("대여중".equals(displayStatus) || "미반납".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="returnProcess('<%=item.getRentalNo()%>')"><fmt:message key="table.rental.btn.return" /></button>
                                <% if (isManagerMode) { %> <th><fmt:message key="table.rental.writer" /></th> <% } %>
                                <% } else { %>
                                    <span style="color: #ced4da;">-</span>
                                <% } %>
                            </td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
                </table>
            </div> 
            
            <div class="pagination-container">
                <% if (rentalStartPage > 1) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalStartPage-1%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = rentalStartPage; i <= rentalEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == rentalPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=i%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (rentalEndPage < rentalTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalEndPage+1%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>


            <div class="section-title"><a href="myLeave.jsp" class="data-link"><fmt:message key="table.leave.title" /></a></div>
            <div class="table-wrapper">
                <table class="table-leave">
                    <thead>
                        <tr>
                            <th><fmt:message key="table.leave.no" /></th>
                            <th><fmt:message key="table.leave.date" /></th>
                            <th><fmt:message key="table.leave.days" /></th>
                            <th><fmt:message key="table.leave.reason" /></th>
                            <th><fmt:message key="table.leave.status" /></th>
                        </tr>
                    </thead>
                     <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <tr><td colspan="5" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.leave.empty" /></td></tr>
                    <% } else { 
                           for (LeaveHistoryDTO leave : leaveList) {
                               String badgeClass = "bg-warning";
                               if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                               else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                            <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                            <td><b><%=leave.getUseDays()%><fmt:message key="table.leave.days.unit" /></b></td>
                            <td><%=leave.getReason()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
                </table>
            </div> 
            
            <div class="pagination-container">
                <% if (leaveStartPage > 1) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveStartPage-1%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = leaveStartPage; i <= leaveEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == leavePage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=i%>')"><%=i%></button>
                <% } %>
                <% if (leaveEndPage < leaveTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveEndPage+1%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>

        </div>
    </div>
    </div>

</body>
</html>