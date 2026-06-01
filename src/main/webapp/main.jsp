<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.groupware.dto.RentalHistoryDTO"%>
<%@ page import="com.groupware.dto.ReservationDTO"%>
<%@ page import="com.groupware.dto.LeaveHistoryDTO"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="com.groupware.dao.ReservationDAO"%>
<%@ page import="com.groupware.dao.RentalDAO"%>
<%@ page import="com.groupware.dao.LeaveDAO"%>
<%@ page import="com.groupware.dao.EmployeeDAO"%>
<%@ page import="java.time.LocalDateTime"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%
    // 로그인 체크 세션없으면 로그인페이지로 이동
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

    // 데이터 가져오기
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> reserveList = resDao.getMyReservations(loginEmp.getEmpNo());

    // [데이터 수집]: 관리자면 전체 내역을 땡겨온 뒤 아래 HTML 반복문에서 퇴사자 및 관리자 본인 기안을 필터링합니다.
    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> myList = null;
    
    if ("Y".equals(loginEmp.getManager())) {
        myList = rentalDao.getAllDocumentList(); 
    } else {
        myList = rentalDao.getMyRentalList(loginEmp.getEmpNo());
    }

    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> myLeaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo()); 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Groupware Dashboard</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.5">

<script>
    function returnProcess(rentalNo) { 
        if (confirm("해당 반납 처리하시겠습니까?")) 
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; }
    function cancelReserve(resNo) { 
        if (confirm("정말 이 예약을 취소하시겠습니까?")) 
            location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; }
</script>
</head>
<body>

    <div class="header">
        <div class="header-inner">
            <a href="main.jsp" class="logo-area">
                <span class="logo-group">Group</span><span class="logo-ware">Ware</span>
            </a>
            
            <div class="nav-buttons">
                <span class="user-profile-info">
                    <% if ("Y".equals(loginEmp.getManager())) { %>
                        <span class="admin-tag">ADMIN</span>
                    <% } %>
                    <b><%=loginEmp.getEmpName()%></b>님
                </span>

                <% if ("Y".equals(loginEmp.getManager())) { %>
                    <a href="adminEqList.do" class="nav-btn admin-special">재고 관리</a>
                    <a href="admin.do" class="nav-btn admin-special">사원 관리</a>
                <% } %>

                <a href="officeMap.jsp" class="nav-btn">오피스 예약</a>
                <a href="leaveForm.do" class="nav-btn">휴가 신청</a>
                <a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
                <a href="documentList.do" class="nav-btn">기안 문서함</a>
                <a href="myPage.do" class="nav-btn">마이페이지</a>
                <a href="logout.do" class="nav-btn logout">로그아웃</a>
            </div>
        </div>
    </div>

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
        <div class="section-title">내 회의실 예약 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>예약 번호</th><th>회의실</th><th>예약 일자</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>비고</th></tr></thead>
                <tbody>
                <% if (reserveList == null || reserveList.isEmpty()) { %>
                    <tr>
                        <td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이 없습니다.</td>
                    </tr>
                <%
                    } else {
                    for (ReservationDTO dto : reserveList) {
                        String displayStatus = dto.getStatus();
                        String statusClass = "bg-primary";

                        if ("예약완료".equals(displayStatus)) {
                            try {
                                LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                                LocalTime eTime = LocalTime.parse(dto.getEndTime());
                                LocalDateTime endDateTime = LocalDateTime.of(rDate, eTime);

                                if (LocalDateTime.now().isAfter(endDateTime)) {
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
                        <td><%=dto.getPurpose()%></td>
                        <td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td>
                        <td>
                            <% if ("예약완료".equals(displayStatus)) { %>
                                <button class="btn-action btn-cancel" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button> 
                            <% } else { %> 
                                <span style="color: #ced4da;">-</span> 
                            <% } %>
                        </td>
                    </tr>
                    <%}} %>
                </tbody>
            </table>
        </div>

        <div class="section-title"><%= "Y".equals(loginEmp.getManager()) ? "비품 대여 및 미반납 내역 (관리자)" : "내 비품 대여 현황" %></div>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>기안 번호</th>
                        <% if ("Y".equals(loginEmp.getManager())) { %>
                            <th>기안자</th> 
                        <% } %>
                        <th>기안 제목</th>
                        <th>대여 기간</th>
                        <th>상태</th>
                        <th>비고</th>
                    </tr>
                </thead>
                <tbody>
                <% 
                    boolean hasData = false; 
                    if (myList != null && !myList.isEmpty()) {
                        for (RentalHistoryDTO item : myList) {
                            boolean isRetiredCreator = (item.getEmpLevel() == 0);
                            String displayStatus = item.getStatus();
                            boolean isAdmin = "Y".equals(loginEmp.getManager());
                            
                            boolean isMyDoc = (item.getEmpNo() == loginEmp.getEmpNo());
                            
                            if (isAdmin) {
                                boolean isTargetRetired = isRetiredCreator && ("대여중".equals(displayStatus) || "미반납".equals(displayStatus));
                                if (!isTargetRetired && !isMyDoc) {
                                    continue; 
                                }
                            }
                            
                            hasData = true;
                            
                            String badgeClass = "bg-secondary";
                            if ("승인대기".equals(displayStatus)) {
                                badgeClass = "bg-warning";
                            } else if ("대여중".equals(displayStatus)) {
                                badgeClass = "bg-success";
                            } else if ("반려됨".equals(displayStatus) || "미반납".equals(displayStatus)) {
                                badgeClass = "bg-danger";
                            }
                %>
                <tr>
                    <td><%=item.getRentalNo()%></td>
                    <% if (isAdmin) { %>
                        <td style="font-weight: 600; color: #64748b;">
                            <%=item.getEmpName()%> <%= isRetiredCreator ? "<br>(퇴사)" : "<br>(본인)" %>
                        </td>
                    <% } %>
                    
                    <%-- 💡 [기안 제목 정렬 변경]: text-align을 center로 수정하고, 양쪽 균형을 위해 padding을 좌우 동일하게 조정했습니다. --%>
                    <td style="text-align: center; padding: 10px 15px;">
                        <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" 
                           style="text-decoration: none; color: #6366f1; font-weight: 600; cursor: pointer; display: inline-block;"
                           onmouseover="this.style.textDecoration='underline'; this.style.color='#0284c7';"
                           onmouseout="this.style.textDecoration='none'; this.style.color='#6366f1';">
                            <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                        </a>
                    </td>
                    
                    <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                    <td><span class="status-badge <%=badgeClass%>"><%=displayStatus%></span></td>
                    <td>
                        <% 
                            boolean isEligibleForReturn = "대여중".equals(displayStatus) || "미반납".equals(displayStatus);
                            if (isEligibleForReturn) { 
                        %>
                            <button class="btn-action btn-cancel" onclick="returnProcess('<%=item.getRentalNo()%>')">
                                반납 처리
                            </button>
                        <% } else { %>
                            <span style="color: #ced4da;">-</span>
                        <% } %>
                    </td>
                </tr>
                <% 
                        } 
                    } 
                    
                    if (!hasData) { 
                %>
                    <tr><td colspan="<%= "Y".equals(loginEmp.getManager()) ? 6 : 5 %>" style="padding: 40px; color: #6c757d;">대여 내역이 없습니다.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div> 

        <div class="section-title">내 휴가 신청 현황</div>
        <div class="table-wrapper">
            <table>
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
                <% if (myLeaveList == null || myLeaveList.isEmpty()) { %>
                    <tr>
                        <td colspan="5" style="padding: 40px; color: #6c757d;">신청한 휴가 내역이 없습니다.</td>
                    </tr>
                <% } else { for (LeaveHistoryDTO leave : myLeaveList) {
                    String badgeClass = "bg-warning";
                    if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                    else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                %>
                <tr>
                    <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                    <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                    <td><b><%=leave.getUseDays()%>일</b></td>
                    <td style="text-align: left; padding-left: 20px;"><%=leave.getReason()%></td>
                    <td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td>
                </tr>
                <% } } %>
                </tbody>
            </table>
        </div> 
    </div>
  </div>

</body>
</html>