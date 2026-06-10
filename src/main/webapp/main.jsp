<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*, com.groupware.dto.*, com.groupware.dao.*, com.groupware.util.StatusUtil" %>
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
     * [Step 3-1] 회의실 예약 현황 데이터 수집 / 정렬 / 페이징 (Locale-Independent)
     * ========================================================================= */
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> rawReserveList = resDao.getMyReservations(currentEmpNo); 
    List<Map<String, Object>> fullReserveList = new ArrayList<>();

    if (rawReserveList != null) {
        for (ReservationDTO dto : rawReserveList) {
            Map<String, Object> m = new HashMap<>();
            m.put("resNo", dto.getResNo());
            m.put("roomId", dto.getRoomId());
            m.put("resDate", dto.getResDate() != null ? dto.getResDate().toString() : "");
            m.put("startTime", dto.getStartTime());
            m.put("endTime", dto.getEndTime());
            m.put("purpose", dto.getPurpose());
             
            // 💡 [국가독립적 리팩토링] 한국어 하드코딩 문자열 대신 StatusUtil에서 다국어 '프로퍼티 키'를 먼저 추출
            String targetKey = StatusUtil.getStatusKey(dto.getStatus());
            
            // 💡 [기능유지] 예약완료 상태인 건들 중, 현재 시간이 이용 종료 시간을 넘어섰는지 체크하는 기존 로직 유지
            if ("status.res.complete".equals(targetKey)) {
                try {
                    LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                    LocalTime eTime = LocalTime.parse(dto.getEndTime());
                    if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                        // 조건 충족 시 '이용 종료' 프로퍼티 키로 강제 치환
                        targetKey = "status.res.finished";
                    }
                } catch (Exception e) {}
            }
            
            m.put("statusKey", targetKey); // 정렬과 화면 출력을 위해 한국어 대신 프로퍼티 키를 바인딩
            m.put("rawDto", dto);          // 풀캘린더나 확장 기능을 대비한 원본 백업
            fullReserveList.add(m);
        }
    }

    // 💡 [국가독립적 정렬] 추출된 '다국어 프로퍼티 키' 문자열을 기준으로 우선순위 정렬 수행
    if (!fullReserveList.isEmpty()) {
        Collections.sort(fullReserveList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                String k1 = (String) m1.get("statusKey");
                String k2 = (String) m2.get("statusKey");
                 
                // 예약완료(status.res.complete) 상태에 최우선 가중치(1순위) 부여
                int p1 = "status.res.complete".equals(k1) ? 1 : 2;
                int p2 = "status.res.complete".equals(k2) ? 1 : 2;
                 
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 상태 가중치 비교
                }
                 
                // 2순위: 상태가 동률일 경우 예약일자 오름차순 정렬 (기존 기능 유지)
                String d1 = (String) m1.get("resDate");
                String d2 = (String) m2.get("resDate");
                return d1.compareTo(d2);
            }
        });
    }
     
    // 정렬이 완료된 컬렉션을 규격에 맞춰 페이징 데이터 쪼개기(subList) 진행
    int resTotalCount = fullReserveList.size();
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
     
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
     
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
     
    // 화면 단 <tbody> 루프에 공급할 최종 회의실 리스트 조각 생성
    List<Map<String, Object>> resSubList = (resStartIdx < resTotalCount) 
                                         ? fullReserveList.subList(resStartIdx, resEndIdx) : null;

    /* =========================================================================
     * [Step 3-2] 비품 대여 데이터 계산 / 권한별 필터링 / 정렬 (Locale-Independent)
     * ========================================================================= */
    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> rawRentalList = isManagerMode ? rentalDao.getAllDocumentList() : rentalDao.getMyRentalList(currentEmpNo);
    List<RentalHistoryDTO> filteredRentalList = new ArrayList<>();
    
    // 💡 [기능유지] 관리자 모드 시 퇴사자 계정 분기 및 본인 작성 글 필터링 복잡 로직 원본 유지
    if (rawRentalList != null) {
        for (RentalHistoryDTO item : rawRentalList) {
            if (isManagerMode) {
                boolean isRetired = (item.getEmpLevel() == 0); 
                boolean isMyDoc = (item.getEmpNo() == currentEmpNo);
                // 퇴사자이면서 현재 진행 중(대여중/미반납)이거나, 내가 올린 결재 문서가 아니라면 스킵
                boolean isTargetRetired = isRetired && ("대여중".equals(item.getStatus()) || "미반납".equals(item.getStatus()));
                if (!isTargetRetired && !isMyDoc) continue; 
            }
            filteredRentalList.add(item);
        }
    }
    
    // 💡 [국가독립적 정렬] 비품 대여 상태별 정렬 가중치를 StatusUtil Key 기반으로 점수화
    if (!filteredRentalList.isEmpty()) {
        Collections.sort(filteredRentalList, new Comparator<RentalHistoryDTO>() {
            @Override
            public int compare(RentalHistoryDTO r1, RentalHistoryDTO r2) {
                String k1 = StatusUtil.getStatusKey(r1.getStatus());
                String k2 = StatusUtil.getStatusKey(r2.getStatus());
                
                // 가중치 스코어링 (낮을수록 리스트의 최상단에 배치됨)
                int p1 = 3; // 기본값 (반납완료, 반려됨 등 종료 상태)
                if ("status.rental.renting".equals(k1)) p1 = 0;       // 대여중 (0순위)
                else if ("status.rental.notreturned".equals(k1)) p1 = 1; // 미반납 (1순위)
                else if ("status.rental.wait".equals(k1)) p1 = 2;       // 승인대기 (2순위)
                
                int p2 = 3;
                if ("status.rental.renting".equals(k2)) p2 = 0;
                else if ("status.rental.notreturned".equals(k2)) p2 = 1;
                else if ("status.rental.wait".equals(k2)) p2 = 2;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 상태 가중치 정렬
                }
                
                // 2순위: 상태 점수가 완전히 같으면 대여 시작일(RentalDate) 기준 최신순(내림차순) 정렬
                String d1 = r1.getRentalDate() != null ? r1.getRentalDate().toString() : "";
                String d2 = r2.getRentalDate() != null ? r2.getRentalDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    // 비품 대여 페이징 쪼개기 블록 연산
    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;
    
    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);
    
    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    
    // 화면 단에 노출될 최종 5개 행 서브리스트
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount) ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx) : null;  
            

    /* =========================================================================
     * [Step 3-3] 내 휴가 신청 데이터 수집 / 정렬 / 페이징 (Locale-Independent)
     * ========================================================================= */
    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    
    // 💡 [국가독립적 정렬] 휴가 데이터 정렬 기준을 StatusUtil Key 기반으로 스위칭
    if (fullLeaveList != null && !fullLeaveList.isEmpty()) {
        Collections.sort(fullLeaveList, new Comparator<LeaveHistoryDTO>() {
            @Override
            public int compare(LeaveHistoryDTO l1, LeaveHistoryDTO l2) {
                String k1 = StatusUtil.getStatusKey(l1.getStatus());
                String k2 = StatusUtil.getStatusKey(l2.getStatus());
                
                int p1 = 2; // 기본값 (반려됨 등 최하단)
                if ("status.rental.wait".equals(k1)) p1 = 0;       // 승인대기 (최상단)
                else if ("status.leave.complete".equals(k1)) p1 = 1; // 승인완료 (중간)
                
                int p2 = 2;
                if ("status.rental.wait".equals(k2)) p2 = 0;
                else if ("status.leave.complete".equals(k2)) p2 = 1;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 휴가 승인 상태 우선순위 정렬
                }
                
                // 2순위: 상태가 같으면 휴가 시작일(StartDate) 기준 최신날짜 순(내림차순) 정렬
                String d1 = l1.getStartDate() != null ? l1.getStartDate().toString() : "";
                String d2 = l2.getStartDate() != null ? l2.getStartDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    // 휴가 데이터 페이징 쪼개기 블록 연산
    int leaveTotalCount = (fullLeaveList != null) ? fullLeaveList.size() : 0;
    int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / RECORDS_PER_PAGE);
    if (leaveTotalPages == 0) leaveTotalPages = 1;
    
    int leaveStartPage = ((leavePage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int leaveEndPage = Math.min(leaveStartPage + PAGES_PER_BLOCK - 1, leaveTotalPages);
    
    int leaveStartIdx = (leavePage - 1) * RECORDS_PER_PAGE;
    int leaveEndIdx = Math.min(leaveStartIdx + RECORDS_PER_PAGE, leaveTotalCount);
    
    // 최종 출력용 휴가 리스트 조각 생성
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
                        if (!"status.res.canceled".equals(map.get("statusKey"))) {
                            if (!isFirst) { out.print(","); } isFirst = false;
                %>
                {
                    title: '[<%=map.get("roomId")%>号室]\n<%=map.get("startTime")%> ~ <%=map.get("endTime")%>',
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
        // 비품 반납 핸들러
        function returnProcess(rentalNo) { 
            if (confirm("<fmt:message key="alert.return.confirm" />")) {
                saveScroll(); 
                location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; 
            }
        }
        
        // 회의실 예약 취소 핸들러
        function cancelReserve(resNo) { 
            if (confirm("<fmt:message key="alert.cancel.confirm" />")) {
                saveScroll(); 
                location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; 
            }
        }

        // 삼분할 페이징 전용 스크롤 유지 라우터
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
        
        <div class="info-card" style="width: 1100px; margin: 20px auto; background: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.03); display: flex; justify-content: space-between; align-items: center; box-sizing: border-box;">
            <div class="welcome-box">
                <h3 style="margin: 0 0 5px 0; font-size: 20px; color: #34495e;"><fmt:message key="dashboard.welcome"><fmt:param value="<%=loginEmp.getEmpName()%>"/></fmt:message></h3>
                <p style="margin: 0; color: #7f8c8d; font-size: 14px;"><fmt:message key="dashboard.dept"><fmt:param value="<%=loginEmp.getDept()%>"/></fmt:message></p>
            </div>
            <div class="leave-box" style="text-align: right;">
                <p class="leave-label" style="margin: 0 0 5px 0; font-size: 13px; color: #7f8c8d;"><fmt:message key="dashboard.leave.label" /></p>
                <div class="leave-count" style="font-size: 22px; font-weight: bold; color: #2c3e50;">
                    <span class="current" style="color: #4f46e5;"><%=loginEmp.getCurLeave()%></span>
                    <span class="divider" style="color: #d1d5db; margin: 0 4px;">/</span>
                    <span class="total"><%=loginEmp.getMaxLeave()%></span>
                </div>
            </div>
        </div>
        
        <div class="container">

            <div class="dashboard-grid">
                <div class="calendar-section">
                    <div id="calendar"></div>
                </div>
            </div> <div class="section-title"> 
                <a href="myReservation.jsp" class="data-link"><fmt:message key="table.res.title" /></a>
            </div>
            <div class="table-wrapper">
                <table>
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
                                   String statusKey = (String) resMap.get("statusKey");
                                   String statusClass = "bg-primary"; 
                                   
                                   if ("status.res.finished".equals(statusKey)) {
                                       statusClass = "bg-secondary"; 
                                   } else if ("status.res.canceled".equals(statusKey)) {
                                       statusClass = "bg-danger"; 
                                   }
                        %>
                        <tr>
                            <td style="color: #6c757d;"><%=resMap.get("resNo")%></td>
                            <td style="font-weight: 600; color: #343a40;"><%=resMap.get("roomId")%>호</td>
                            <td><%=resMap.get("resDate")%></td>
                            <td><%=resMap.get("startTime")%> ~ <%=resMap.get("endTime")%></td>
                            <td>
                                <span class="title-link" title="<%=resMap.get("purpose")%>"><%=resMap.get("purpose")%></span>
                            </td>
                            <td><span class="status-badge <%=statusClass%>"><fmt:message key="<%=statusKey%>" /></span></td>
                            <td>
                                <% if ("status.res.complete".equals(statusKey)) { %>
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
                    <fmt:message key="<%= isManagerMode ? \"table.rental.title.admin\" : \"table.rental.title.user\" %>" />
                </a>
            </div>
            <div class="table-wrapper">
                <table class="table-rental <%= isManagerMode ? "admin-view" : "" %>">
                    <thead>
                        <tr>
                            <th style="width: 10%;"><fmt:message key="table.rental.no" /></th>
                            <% if (isManagerMode) { %> <th style="width: 15%;"><fmt:message key="table.rental.writer" /></th> <% } %>
                            <th><fmt:message key="table.rental.title" /></th>
                            <th style="width: 25%;"><fmt:message key="table.rental.date" /></th>
                            <th style="width: 12%;"><fmt:message key="table.rental.status" /></th>
                            <th style="width: 12%;"><fmt:message key="table.rental.note" /></th>
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
                                <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" style="text-decoration: none; display: block;">
                                    <span class="title-link" style="color: #6366f1; font-weight: 600;" title="<%=item.getTitle()%>">
                                        <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                                    </span>
                                </a>
                            </td>
                            <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><fmt:message key="<%= StatusUtil.getStatusKey(displayStatus) %>" /></span></td>
                            <td>
                                <% if ("대여중".equals(displayStatus) || "미반납".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="returnProcess('<%=item.getRentalNo()%>')"><fmt:message key="table.rental.btn.return" /></button>
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
                            <th style="width: 10%;"><fmt:message key="table.leave.no" /></th>
                            <th style="width: 30%;"><fmt:message key="table.leave.date" /></th>
                            <th style="width: 15%;"><fmt:message key="table.leave.days" /></th>
                            <th><fmt:message key="table.leave.reason" /></th>
                            <th style="width: 15%;"><fmt:message key="table.leave.status" /></th>
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
                            <td><span class="title-link" title="<%=leave.getReason()%>"><%=leave.getReason()%></span></td>
                            <td><span class="status-badge <%=badgeClass%>"><fmt:message key="<%= StatusUtil.getStatusKey(leave.getStatus()) %>" /></span></td>
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

        </div> </div> </body>
</html>