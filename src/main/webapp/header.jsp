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

    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> myList = rentalDao.getMyRentalList(loginEmp.getEmpNo());

    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> myLeaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo()); 
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css?v=1.5">
<title>Insert title here</title>
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
                <a href="boardList.do" class="nav-btn">게시판</a>
                <a href="addressBook.do" class="nav-btn">주소록</a>
                <a href="myPage.do" class="nav-btn">마이페이지</a>
                <a href="logout.do" class="nav-btn logout">로그아웃</a>
            </div>
        </div>
    </div>
</body>
</html>
