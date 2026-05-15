<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%
EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
if (loginEmp == null) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 오피스 도면 예약</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/officeMap.css?v=3.0">
<script>
    function switchFloor(floor) {
        for (let i = 4; i <= 5; i++) {
            document.getElementById('map' + i).style.display = 'none';
            document.getElementById('tab' + i).classList.remove('active');
        }
        document.getElementById('map' + floor).style.display = 'block';
        document.getElementById('tab' + floor).classList.add('active');
    }
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
                
                <a href="officeMap.jsp" class="nav-btn active-page">오피스 예약</a>
                <a href="leaveForm.do" class="nav-btn">휴가 신청</a>
                <a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
                <a href="documentList.do" class="nav-btn">기안 문서함</a>	
                <a href="myPage.do" class="nav-btn">마이페이지</a>
                <a href="logout.do" class="nav-btn logout">로그아웃</a>
            </div>
        </div>
    </div>

    <div class="dashboard-container">
        <div class="map-wrapper">
            <div class="reservation-header">
                <h3>오피스 예약</h3>
                <p>예약을 진행하실 층수와 회의실을 도면에서 선택해 주세요.</p>
            </div>

            <div class="floor-tabs">
                <%-- 기존 1~3층 탭 주석 처리 상태 유지 --%>
                <%-- <button class="floor-tab active" id="tab1" onclick="switchFloor(1)">1층</button>
                <button class="floor-tab" id="tab2" onclick="switchFloor(2)">2층</button>
                <button class="floor-tab" id="tab3" onclick="switchFloor(3)">3층</button> --%>
                <button class="floor-tab active" id="tab4" onclick="switchFloor(4)">4층</button>
                <button class="floor-tab" id="tab5" onclick="switchFloor(5)">5층</button>
            </div>

            <div id="map4" class="map-container">
                <div class="room-btn" id="room404" onclick="location.href='reserve.do?roomId=404'"></div>
                <div class="room-btn" id="room403" onclick="location.href='reserve.do?roomId=403'"></div>
                <div class="room-btn" id="room402" onclick="location.href='reserve.do?roomId=402'"></div>
                <div class="room-btn" id="room401" onclick="location.href='reserve.do?roomId=401'"></div>
                <div class="room-btn" id="roomInterview" onclick="location.href='reserve.do?roomId=Interview'"></div>
                <div class="room-btn" id="roomConsult" onclick="location.href='reserve.do?roomId=Consult'"></div>
                <div class="room-btn" id="roomMeeting" onclick="location.href='reserve.do?roomId=Meeting'"></div>
            </div>
            
            <div id="map5" class="map-container">
                <div class="room-btn" id="room503" onclick="location.href='reserve.do?roomId=503'"></div>
                <div class="room-btn" id="room502" onclick="location.href='reserve.do?roomId=502'"></div>
                <div class="room-btn" id="room501" onclick="location.href='reserve.do?roomId=501'"></div>
                <div class="room-btn" id="roomMeeting5" onclick="location.href='reserve.do?roomId=Meeting5'"></div>
            </div>
        </div>
    </div>

</body>
</html>