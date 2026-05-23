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

    /* RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> myList = rentalDao.getMyRentalList(loginEmp.getEmpNo());

    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> myLeaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo()); */
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Groupware Dashboard</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.5">

<script>
    /* function processReturn(rentalNo) { if (confirm("반납 처리하시겠습니까?")) location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; } */
    function cancelReserve(resNo) { if (confirm("예약을 취소하시겠습니까?")) location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; }
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

        <div class="section-title">내 회의실 예약 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>예약 번호</th><th>회의실</th><th>예약 일자</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>비고</th></tr></thead>
                <tbody>
                <% if (reserveList == null || reserveList.isEmpty()) { %>
                    <tr><td colspan="7" class="empty-data">예약 내역이 없습니다.</td></tr>
                <% } else { for (ReservationDTO dto : reserveList) {
                    String displayStatus = dto.getStatus(); String statusClass = "status-blue";
                    if ("예약완료".equals(displayStatus)) {
                        try {
                            LocalDateTime endDateTime = LocalDateTime.of(((java.sql.Date) dto.getResDate()).toLocalDate(), LocalTime.parse(dto.getEndTime()));
                            if (LocalDateTime.now().isAfter(endDateTime)) { displayStatus = "이용 종료"; statusClass = "status-gray"; }
                        } catch (Exception e) {}
                    } else if ("취소됨".equals(displayStatus)) { statusClass = "status-red"; }
                %>
                    <tr>
                        <td><%=dto.getResNo()%></td>
                        <td class="emphasize"><%=dto.getRoomId()%>호</td>
                        <td><%=dto.getResDate()%></td>
                        <td><%=dto.getStartTime()%> ~ <%=dto.getEndTime()%></td>
                        <td><%=dto.getPurpose()%></td>
                        <td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td>
                        <td>
                            <% if ("예약완료".equals(displayStatus)) { %>
                                <button class="btn-action btn-cancel" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button>
                            <% } else { %>-<% } %>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <%-- <div class="section-title">내 비품 대여 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>기안 번호</th><th>기안 제목</th><th>비품명</th><th>대여 기간</th><th>상태</th><th>비고</th></tr></thead>
                <tbody>
                <% if (myList == null || myList.isEmpty()) { %>
                    <tr><td colspan="6" class="empty-data">대여 내역이 없습니다.</td></tr>
                <% } else { for (RentalHistoryDTO item : myList) {
                    String status = item.getStatus(); String badgeClass = "bg-secondary";
                    if ("승인대기".equals(status)) badgeClass = "bg-warning";
                    else if ("대여중".equals(status) || "미반납".equals(status)) badgeClass = "bg-success";
                    else if ("반려됨".equals(status)) badgeClass = "bg-danger";
                %>
                    <tr><td><%=item.getRentalNo()%></td><td style="text-align: left; padding-left: 20px;"><%=item.getTitle() != null ? item.getTitle() : "제목 없음"%></td><td class="emphasize"><%=item.getEqName()%></td><td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td><td><span class="status-badge <%=badgeClass%>"><%=status%></span></td><td>
                    <% if ("대여중".equals(status) || "미반납".equals(status)) { %><button class="btn-action btn-return" onclick="processReturn('<%=item.getRentalNo()%>')">반납 처리</button><% } else { %>-<% } %>
                    </td></tr>
                <% } } %>
                </tbody>
            </table>
        </div> --%>

        <%-- <div class="section-title">내 휴가 신청 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>문서 번호</th><th>휴가 기간</th><th>사용 일수</th><th>사유</th><th>상태</th></tr></thead>
                <tbody>
                <% if (myLeaveList == null || myLeaveList.isEmpty()) { %>
                    <tr><td colspan="5" class="empty-data">휴가 신청 내역이 없습니다.</td></tr>
                <% } else { for (LeaveHistoryDTO leave : myLeaveList) {
                    String badgeClass = "bg-warning";
                    if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                    else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                %>
                    <tr><td><%=leave.getLeaveNo()%></td><td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td><td><b><%=leave.getUseDays()%>일</b></td><td style="text-align: left; padding-left: 20px;"><%=leave.getReason()%></td><td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td></tr>
                <% } } %>
                </tbody>
            </table>
        </div> --%>
    </div>

</body>
</html>