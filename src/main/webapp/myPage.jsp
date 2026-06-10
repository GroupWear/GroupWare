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
     * [Step 1] 세션 검증 및 기초 데이터 수집 (Auth & Context)
     * ========================================================================= */
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    int currentEmpNo = loginEmp.getEmpNo();              
    boolean isManagerMode = "Y".equals(loginEmp.getManager()); 
    LocalDateTime currentDateTime = LocalDateTime.now();       
    String currentMapping = request.getRequestURI();   

    /* =========================================================================
     * [Step 2] 삼분할 다중 페이징 규격 변수 세팅 (Pagination Config)
     * ========================================================================= */
    final int RECORDS_PER_PAGE = 5; 
    final int PAGES_PER_BLOCK = 5;  
    
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
             
            String targetKey = StatusUtil.getStatusKey(dto.getStatus());
            
            if ("status.res.complete".equals(targetKey)) {
                try {
                    LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                    LocalTime eTime = LocalTime.parse(dto.getEndTime());
                    if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                        targetKey = "status.res.finished";
                    }
                } catch (Exception e) {}
            }
            
            m.put("statusKey", targetKey); 
            m.put("rawDto", dto);          
            fullReserveList.add(m);
        }
    }

    if (!fullReserveList.isEmpty()) {
        Collections.sort(fullReserveList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                String k1 = (String) m1.get("statusKey");
                String k2 = (String) m2.get("statusKey");
                 
                int p1 = "status.res.complete".equals(k1) ? 1 : 2;
                int p2 = "status.res.complete".equals(k2) ? 1 : 2;
                 
                if (p1 != p2) {
                    return Integer.compare(p1, p2); 
                }
                 
                String d1 = (String) m1.get("resDate");
                String d2 = (String) m2.get("resDate");
                return d1.compareTo(d2);
            }
        });
    }
     
    int resTotalCount = fullReserveList.size();
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
     
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
     
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
     
    List<Map<String, Object>> resSubList = (resStartIdx < resTotalCount) 
                                         ? fullReserveList.subList(resStartIdx, resEndIdx) : null;


    /* =========================================================================
     * [Step 3-2] 비품 대여 데이터 계산 / 권한별 필터링 / 정렬 (Locale-Independent)
     * ========================================================================= */
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
    
    if (!filteredRentalList.isEmpty()) {
        Collections.sort(filteredRentalList, new Comparator<RentalHistoryDTO>() {
            @Override
            public int compare(RentalHistoryDTO r1, RentalHistoryDTO r2) {
                String k1 = StatusUtil.getStatusKey(r1.getStatus());
                String k2 = StatusUtil.getStatusKey(r2.getStatus());
                
                int p1 = 3; 
                if ("status.rental.renting".equals(k1)) p1 = 0;       
                else if ("status.rental.notreturned".equals(k1)) p1 = 1; 
                else if ("status.rental.wait".equals(k1)) p1 = 2;       
                
                int p2 = 3;
                if ("status.rental.renting".equals(k2)) p2 = 0;
                else if ("status.rental.notreturned".equals(k2)) p2 = 1;
                else if ("status.rental.wait".equals(k2)) p2 = 2;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); 
                }
                
                String d1 = r1.getRentalDate() != null ? r1.getRentalDate().toString() : "";
                String d2 = r2.getRentalDate() != null ? r2.getRentalDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;
    
    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);
    
    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount) ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx) : null;  
            

    /* =========================================================================
     * [Step 3-3] 내 휴가 신청 데이터 수집 / 정렬 / 페이징 (Locale-Independent)
     * ========================================================================= */
    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    
    if (fullLeaveList != null && !fullLeaveList.isEmpty()) {
        Collections.sort(fullLeaveList, new Comparator<LeaveHistoryDTO>() {
            @Override
            public int compare(LeaveHistoryDTO l1, LeaveHistoryDTO l2) {
                String k1 = StatusUtil.getStatusKey(l1.getStatus());
                String k2 = StatusUtil.getStatusKey(l2.getStatus());
                
                int p1 = 2; 
                if ("status.rental.wait".equals(k1)) p1 = 0;       
                else if ("status.leave.complete".equals(k1)) p1 = 1; 
                
                int p2 = 2;
                if ("status.rental.wait".equals(k2)) p2 = 0;
                else if ("status.leave.complete".equals(k2)) p2 = 1;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); 
                }
                
                String d1 = l1.getStartDate() != null ? l1.getStartDate().toString() : "";
                String d2 = l2.getStartDate() != null ? l2.getStartDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    int leaveTotalCount = (fullLeaveList != null) ? fullLeaveList.size() : 0;
    int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / RECORDS_PER_PAGE);
    if (leaveTotalPages == 0) leaveTotalPages = 1;
    
    int leaveStartPage = ((leavePage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int leaveEndPage = Math.min(leaveStartPage + PAGES_PER_BLOCK - 1, leaveTotalPages);
    
    int leaveStartIdx = (leavePage - 1) * RECORDS_PER_PAGE;
    int leaveEndIdx = Math.min(leaveStartIdx + RECORDS_PER_PAGE, leaveTotalCount);
    
    List<LeaveHistoryDTO> leaveList = (fullLeaveList != null && leaveStartIdx < leaveTotalCount) ? fullLeaveList.subList(leaveStartIdx, leaveEndIdx) : null;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="nav.mypage" /></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/myPage.css">
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
                    <% if (filteredRentalList != null) {
                        for (RentalHistoryDTO rDto : filteredRentalList) {
                            String itemTitle = rDto.getTitle() != null ? rDto.getTitle() : "제목 없음";
                    %>
                    {
                        title: '[<fmt:message key="calendar.prefix.rental" />] <%=itemTitle%>',
                        start: '<%=rDto.getRentalDate()%>',
                        backgroundColor: '#1cc88a', 
                        borderColor: '#1cc88a',
                        extendedProps: { 
                                url: 'rentalDetail.do?rentalNo=<%=rDto.getRentalNo()%>' 
                            }
                    },
                    <% } } %>

                    <% if (leaveList != null) {
                        for (LeaveHistoryDTO lDto : leaveList) {
                    %>
                    {
                        title: '[<fmt:message key="nav.leave.apply" />] <%=lDto.getReason()%>',
                        start: '<%=lDto.getStartDate()%>',
                        end: '<%=lDto.getEndDate()%>', 
                        backgroundColor: '#e74a3b', 
                        borderColor: '#e74a3b',
                        extendedProps: { 
                            url: 'leaveDetail.do?leaveNo=<%=lDto.getLeaveNo()%>' 
                        }
                    },
                    <% } } %>
                ],
                eventClick: function(info) {
                    if (info.event.extendedProps && info.event.extendedProps.url) {
                        location.href = info.event.extendedProps.url;
                    }
                }
            });
            calendar.render();
        });
    </script>
<script>
    function returnProcess(rentalNo) { 
        if (confirm("<fmt:message key='alert.return.confirm' />")) 
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; }
    
    function cancelReserve(resNo) { 
        if (confirm("<fmt:message key='alert.cancel.confirm' />")) 
            location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; }
    
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
            window.scrollTo(0, parseInt(savedScrollY)); 
            localStorage.removeItem("main_scroll_y"); 
        }
    });
</script>
</head>
<body>
    
<jsp:include page="header.jsp" />
    
    <div class="container">
        
        <div class="page-header">
            <h2><fmt:message key="nav.mypage" /></h2>
            <div class="btn-group">
                <a href="changePw.jsp" class="btn-pw"><fmt:message key="mypage.btn.changePw" /></a>
            </div>
        </div>
        
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
                                <td><span class="title-link" title="<%=resMap.get("purpose")%>"><%=resMap.get("purpose")%></span></td>
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
                        <th><fmt:message key="table.leave.no" /></th>
                        <th><fmt:message key="table.leave.date" /></th>
                        <th><fmt:message key="table.leave.days" /></th>
                        <th><fmt:message key="table.leave.reason" /></th>
                        <th><fmt:message key="table.leave.status" /></th>
                    </tr>
                </thead>
                <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <%-- ★ [수정] 테이블 열 개수인 5개에 맞춰 colspan="5"로 정상 정정 (기존 7 오타 수정) --%>
                        <tr><td colspan="5" style="padding: 105px 0; color: #6c757d; border-bottom: none;"><fmt:message key="table.leave.empty" /></td></tr>
                    <% } else { 
                           for (LeaveHistoryDTO leave : leaveList) {
                               String statusKey = StatusUtil.getStatusKey(leave.getStatus());
                               
                               String badgeClass = "bg-warning";
                               if ("status.leave.complete".equals(statusKey)) { badgeClass = "bg-success"; }
                               else if ("status.leave.rejected".equals(statusKey)) { badgeClass = "bg-danger"; }
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                            <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                            <td><b><%=leave.getUseDays()%><fmt:message key="table.leave.days.unit" /></b></td>
                            <td><%=leave.getReason()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><fmt:message key="<%=statusKey%>" /></span></td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
            </table>
        </div> 
        
        <div class="pagination-container">
                <% if (leaveStartPage > 1) { %>
                    <%-- ★ [수정] leavePage 파라미터 유도 시 자바 표현식 기호(<%= %>)가 빠졌던 치명적인 오타 복구 --%>
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
</body>
</html>