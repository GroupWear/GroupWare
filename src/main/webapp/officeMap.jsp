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
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/officeMap.css">
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
        <!-- ★ Groupware 글자 클릭 시 메인으로 이동하도록 수정 -->
		<a href="main.jsp" style="text-decoration: none; color: inherit;"><h2>Groupware</h2></a>
		<div class="nav-buttons">
			<% if ("Y".equals(loginEmp.getManager())) { %>
				<span style="color: #ffc107; font-weight: bold; font-size: 13px; margin-right: 5px;">[관리자]</span>
			<% } %>
			<span style="margin-right: 20px; font-size: 14px; color: #e9ecef;"><b><%=loginEmp.getEmpName()%></b>님</span>

			<% if ("Y".equals(loginEmp.getManager())) { %>
				<a href="adminEqList.do" class="nav-btn admin">재고 관리</a>
				<a href="admin.do" class="nav-btn admin">사원 관리</a>
                <span style="color: #495057;">|</span>
			<% } %>
            
            <!-- ★ 오피스 예약 및 휴가 신청 버튼 스타일 통일 (인라인 스타일 제거) -->
			<a href="officeMap.jsp" class="nav-btn">오피스 예약</a>
			<a href="leaveForm.do" class="nav-btn">휴가 신청</a>
			<a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
			<a href="documentList.do" class="nav-btn">기안 문서함</a>	
			<a href="myPage.do" class="nav-btn">마이페이지</a>
			<a href="logout.do" class="nav-btn logout">로그아웃</a>
		</div>
	</div>

    <div class="map-wrapper">
        <div class="reservation-header">
            <h3>오피스 예약</h3>
            <p>예약을 진행하실 층수와 회의실을 도면에서 선택해 주세요.</p>
        </div>

        <div class="floor-tabs">
            <!-- <button class="floor-tab active" id="tab1" onclick="switchFloor(1)">1층</button>
            <button class="floor-tab" id="tab2" onclick="switchFloor(2)">2층</button>
            <button class="floor-tab" id="tab3" onclick="switchFloor(3)">3층</button> -->
            <button class="floor-tab active" id="tab4" onclick="switchFloor(4)">4층</button>
            <button class="floor-tab" id="tab5" onclick="switchFloor(5)">5층</button>
        </div>

       <!--  <div id="map1" class="map-container">
            <div class="room-btn" id="room104" onclick="location.href='reserve.do?roomId=104'"></div>
            <div class="room-btn" id="room103" onclick="location.href='reserve.do?roomId=103'"></div>
            <div class="room-btn" id="room102" onclick="location.href='reserve.do?roomId=102'"></div>
            <div class="room-btn" id="room101" onclick="location.href='reserve.do?roomId=101'"></div>
            <div class="room-btn" id="roomInterview1" onclick="location.href='reserve.do?roomId=Interview1'"></div>
            <div class="room-btn" id="roomConsult1" onclick="location.href='reserve.do?roomId=Consult1'"></div>
            <div class="room-btn" id="roomMeeting1" onclick="location.href='reserve.do?roomId=Meeting1'"></div>
        </div>
        <div id="map2" class="map-container">
            <div class="room-btn" id="room204" onclick="location.href='reserve.do?roomId=204'"></div>
            <div class="room-btn" id="room203" onclick="location.href='reserve.do?roomId=203'"></div>
            <div class="room-btn" id="room202" onclick="location.href='reserve.do?roomId=202'"></div>
            <div class="room-btn" id="room201" onclick="location.href='reserve.do?roomId=201'"></div>
            <div class="room-btn" id="roomInterview2" onclick="location.href='reserve.do?roomId=Interview2'"></div>
            <div class="room-btn" id="roomConsult2" onclick="location.href='reserve.do?roomId=Consult2'"></div>
            <div class="room-btn" id="roomMeeting2" onclick="location.href='reserve.do?roomId=Meeting2'"></div>
        </div>
        <div id="map3" class="map-container">
            <div class="room-btn" id="room304" onclick="location.href='reserve.do?roomId=304'"></div>
            <div class="room-btn" id="room303" onclick="location.href='reserve.do?roomId=303'"></div>
            <div class="room-btn" id="room302" onclick="location.href='reserve.do?roomId=302'"></div>
            <div class="room-btn" id="room301" onclick="location.href='reserve.do?roomId=301'"></div>
            <div class="room-btn" id="roomInterview3" onclick="location.href='reserve.do?roomId=Interview3'"></div>
            <div class="room-btn" id="roomConsult3" onclick="location.href='reserve.do?roomId=Consult3'"></div>
            <div class="room-btn" id="roomMeeting3" onclick="location.href='reserve.do?roomId=Meeting3'"></div>
        </div> -->
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
            <!-- <div class="room-btn" id="room504" onclick="location.href='reserve.do?roomId=504'"></div> -->
            <div class="room-btn" id="room503" onclick="location.href='reserve.do?roomId=503'"></div>
            <div class="room-btn" id="room502" onclick="location.href='reserve.do?roomId=502'"></div>
            <div class="room-btn" id="room501" onclick="location.href='reserve.do?roomId=501'"></div>
            <!-- <div class="room-btn" id="roomInterview5" onclick="location.href='reserve.do?roomId=Interview5'"></div>
            <div class="room-btn" id="roomConsult5" onclick="location.href='reserve.do?roomId=Consult5'"></div> -->
            <div class="room-btn" id="roomMeeting5" onclick="location.href='reserve.do?roomId=Meeting5'"></div>
        </div>
    </div>

</body>
</html>