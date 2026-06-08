<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*, com.groupware.dto.*, com.groupware.dao.*" %>
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

    /* =========================================================================
     * [Step 3] 비즈니스 데이터 연산 및 페이지 조각화 (SubList 처리)
     * ========================================================================= */
    
    // 3-1. 회의실 예약 현황 데이터 정밀 계산
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> fullReserveList = resDao.getMyReservations(currentEmpNo); 
    
    int resPage = 1;
    String resPageParam = request.getParameter("resPage");
    if (resPageParam != null && !resPageParam.isEmpty()) resPage = Integer.parseInt(resPageParam);
    
    int resTotalCount = (fullReserveList != null) ? fullReserveList.size() : 0;
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
    
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
    
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
    List<ReservationDTO> reserveList = (fullReserveList != null && resStartIdx < resTotalCount) ? fullReserveList.subList(resStartIdx, resEndIdx) : null;

    // 3-2. 비품 대여 데이터 계산
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
    
    int rentalPage = 1;
    String rentalPageParam = request.getParameter("rentalPage");
    if (rentalPageParam != null && !rentalPageParam.isEmpty()) rentalPage = Integer.parseInt(rentalPageParam);
    
    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;
    
    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);
    
    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount) ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx) : null;

    // 3-3. 내 휴가 신청 데이터 페이징 연산
    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    
    int leavePage = 1;
    String leavePageParam = request.getParameter("leavePage");
    if (leavePageParam != null && !leavePageParam.isEmpty()) leavePage = Integer.parseInt(leavePageParam);
    
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
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Groupware Main Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js'></script>
	<script>
    	document.addEventListener('DOMContentLoaded', function() {
        	var calendarEl = document.getElementById('calendar');
        	var calendar = new FullCalendar.Calendar(calendarEl, {
            	initialView: 'dayGridMonth',
            	headerToolbar: { left: 'prev,next', center: 'title', right: '' },
            	height: 'auto',
            	dayMaxEvents: 2, // 이벤트가 많을 경우 '더보기'로 처리하여 깔끔하게 유지
            	events: [
                	<% if (fullReserveList != null) {
                    	for (ReservationDTO dto : fullReserveList) {
                        	if (!"취소됨".equals(dto.getStatus())) {
                	%>
                	{
                    	// [디자인] 호실과 시간을 줄바꿈으로 명확히 구분
                    	title: '[<%=dto.getRoomId()%>호]\n<%=dto.getStartTime()%> ~ <%=dto.getEndTime()%>',
                    	start: '<%=dto.getResDate()%>',
                    	extendedProps: {
                        	purpose: '<%=dto.getPurpose()%>'
                    	},
                    	backgroundColor: '#4e73df', // 세련된 블루톤 적용
                    	borderColor: '#4e73df'
                	},
                	<% } } } %>
            	],
            	// 캘린더 일정 클릭 시 상세 정보 알림
            	eventClick: function(info) {
                	alert("▶ 회의실 정보\n" + info.event.title.replace('\n', ' ') + "\n\n" +
                      	"▶ 예약 목적\n" + info.event.extendedProps.purpose);
            	}
        	});
        	calendar.render();
    	});
	</script>
	<script>
        function returnProcess(rentalNo) { 
            if (confirm("해당 비품을 반납 처리하시겠습니까?")) {
                saveScroll(); 
                location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; 
            }
        }
        
        function cancelReserve(resNo) { 
            if (confirm("정말 이 예약을 취소하시겠습니까?")) {
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
                // 'instant'를 사용하여 애니메이션 없이 즉시 이동
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
                <h3>안녕하세요, <b><%=loginEmp.getEmpName()%></b>님!</h3>
                <p><%=loginEmp.getDept()%> 소속</p>
            </div>
            <div class="leave-box">
                <p class="leave-label">잔여 연차 현황</p>
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

			<div class="section-title"> <a href="myReservation.jsp" class="data-link">내 회의실 예약 현황</a></div>
            <div class="table-wrapper">
                <table class="table-res">
                    <thead>
                        <tr>
                            <th>예약 번호</th>
                            <th>회의실</th>
                            <th>예약 일자</th>
                            <th>사용 시간</th>
                            <th>사용 목적</th>
                            <th>상태</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (reserveList == null || reserveList.isEmpty()) { %>
                        <tr><td colspan="7" style="padding: 105px 0; color: #6c757d; border-bottom: none;">예약 내역이 없습니다.</td></tr>
                    <% } else {
                           for (ReservationDTO dto : reserveList) {
                               String displayStatus = dto.getStatus();
                               String statusClass = "bg-primary"; 

                               if ("예약완료".equals(displayStatus)) {
                                   try {
                                       LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                                       LocalTime eTime = LocalTime.parse(dto.getEndTime());
                                       if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                                           displayStatus = "이용 종료";
                                           statusClass = "bg-secondary"; 
                                       }
                                   } catch (Exception e) {}
                               } else if ("취소됨".equals(displayStatus)) {
                                   statusClass = "bg-danger"; 
                               }
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=dto.getResNo()%></td>
                            <td style="font-weight: 600; color: #343a40;"><%=dto.getRoomId()%>호</td>
                            <td><%=dto.getResDate()%></td>
                            <td><%=dto.getStartTime()%> ~ <%=dto.getEndTime()%></td>
                            <td><span class="title-link" title="<%=dto.getPurpose()%>"><%=dto.getPurpose()%></span></td>
                            <td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td>
                            <td>
                                <% if ("예약완료".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button> 
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
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resStartPage-1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')">이전</button>
                <% } %>
                <% for (int i = resStartPage; i <= resEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == resPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=i%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (resEndPage < resTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resEndPage+1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')">다음</button>
                <% } %>
            </div>


            <div class="section-title">
                <a href="myRental.jsp" class="data-link"><%= isManagerMode ? "비품 대여 및 미반납 내역 (관리자)" : "내 비품 대여 현황" %></a>
            </div>
            <div class="table-wrapper">
                <table class="table-rental <%= isManagerMode ? "admin-view" : "" %>">
                    <thead>
                        <tr>
                            <th>기안 번호</th>
                            <% if (isManagerMode) { %> <th>기안자</th> <% } %>
                            <th>기안 제목</th>
                            <th>대여 기간</th>
                            <th>상태</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (rentalList == null || rentalList.isEmpty()) { %>
                        <tr><td colspan="<%= isManagerMode ? 6 : 5 %>" style="padding: 105px 0; color: #6c757d; border-bottom: none;">대여 내역이 없습니다.</td></tr>
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
                                    <%=item.getEmpName()%> <%= isRetiredCreator ? "<br>(퇴사)" : "<br>(본인)" %>
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
                                    <button class="btn-action" onclick="returnProcess('<%=item.getRentalNo()%>')">반납 처리</button>
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
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalStartPage-1%>&leavePage=<%=leavePage%>')">이전</button>
                <% } %>
                <% for (int i = rentalStartPage; i <= rentalEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == rentalPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=i%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (rentalEndPage < rentalTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalEndPage+1%>&leavePage=<%=leavePage%>')">다음</button>
                <% } %>
            </div>


            <div class="section-title"><a href="myLeave.jsp" class="data-link">내 휴가 신청 현황</a></div>
            <div class="table-wrapper">
                <table class="table-leave">
                    <thead>
                        <tr>
                            <th>문서 번호</th>
                            <th>휴가 기간</th>
                            <th>사용 일수</th>
                            <th>사유</th>
                            <th>상태</th>
                        </tr>
                    </thead>
                     <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <tr><td colspan="5" style="padding: 105px 0; color: #6c757d; border-bottom: none;">신청한 휴가 내역이 없습니다.</td></tr>
                    <% } else { 
                           for (LeaveHistoryDTO leave : leaveList) {
                               String badgeClass = "bg-warning";
                               if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                               else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                            <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                            <td><b><%=leave.getUseDays()%>일</b></td>
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
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveStartPage-1%>')">이전</button>
                <% } %>
                <% for (int i = leaveStartPage; i <= leaveEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == leavePage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=i%>')"><%=i%></button>
                <% } %>
                <% if (leaveEndPage < leaveTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveEndPage+1%>')">다음</button>
                <% } %>
            </div>

        </div>
    </div>
    </div>

</body>
</html>