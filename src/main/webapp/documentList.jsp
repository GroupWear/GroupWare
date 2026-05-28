<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="com.groupware.dto.LeaveHistoryDTO"%>
<%@ page import="com.groupware.dto.RentalHistoryDTO"%>
<%
    // 1. 세션 로그인 상태 체크
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. 컨트롤러 등에서 넘겨받은 기안 목록 수령
    List<LeaveHistoryDTO> leaveList = (List<LeaveHistoryDTO>) request.getAttribute("leaveList");
    List<RentalHistoryDTO> eqList = (List<RentalHistoryDTO>) request.getAttribute("docList");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사내 시스템 - 기안 문서함</title>
    <!-- 기존 메인 테마 스타일시트 연결 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.6">
    <!-- 📌 중요: 외부로 분리한 전용 스타일시트 연결 (캐시 방지 파라미터 적용) -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/documentList.css?v=1.1">
</head>
<body>

    <!-- 1. 공통 네비게이션 헤더 바 -->
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

    <!-- 2. 메인 콘텐츠 영역 -->
    <div class="dashboard-container">
        
        <div class="headertitle" style="margin-top: 30px; margin-bottom: 15px;">
            <h2 style="margin: 0; font-size: 22px; font-weight: 700; color: #1e293b;">통합 기안 문서함</h2>
        </div>
        
        <!-- 3. 기안 분류별 탭 버튼 영역 (오피스 제거 후 휴가 신청을 기본 활성화) -->
        <div class="tab-menu-container">
            <button class="tab-btn active" onclick="switchTab(event, 'tab-leave')">휴가 신청 기안</button>
            <button class="tab-btn" onclick="switchTab(event, 'tab-equipment')">비품 대여 신청 기안</button>
        </div>

        <!-- 4. 탭 내부 콘텐츠: 휴가 신청 테이블 (기본 표시) -->
        <div id="tab-leave" class="tab-content active" style="display: block;">
            <div class="table-wrapper">
                <!-- 📌 여기에 style 속성이 직접 들어갔습니다 -->
                <table style="min-width: 900px; table-layout: fixed;">
                    <thead>
					    <tr>
					        <th style="width: 10%;">문서 번호</th> <!-- 15% -> 10% 축소 -->
					        <th style="width: 25%;">휴가 기간</th>
					        <th style="width: 10%;">사용 일수</th> <!-- 15% -> 10% 축소 -->
					        <th style="width: 40%;">휴가 사유</th> <!-- 30% -> 40% 확장 -->
					        <th style="width: 15%;">결재 상태</th> <!-- 15% 유지 (여유 확보) -->
					    </tr>
					</thead>
                    <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <tr><td colspan="5" class="empty-data">신청된 휴가 기안 내역이 없습니다.</td></tr>
                    <% } else { for (LeaveHistoryDTO leave : leaveList) { 
                        String statusClass = "status-blue";
                        if ("반려됨".equals(leave.getStatus())) statusClass = "status-red";
                        else if ("승인완료".equals(leave.getStatus())) statusClass = "status-gray";
                    %>
                        <tr>
                            <td><%= leave.getLeaveNo() %></td>
                            <td><%= leave.getStartDate() %> ~ <%= leave.getEndDate() %></td>
                            <td><b><%= leave.getUseDays() %>일</b></td>
                            <td style="text-align: left; padding-left: 20px;"><%= leave.getReason() %></td>
                            <td><span class="status-badge <%= statusClass %>"><%= leave.getStatus() %></span></td>
                        </tr>
                    <% } } %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 5. 탭 내부 콘텐츠: 비품 대여 테이블 -->
        <div id="tab-equipment" class="tab-content">
		    <div class="table-wrapper">
		         <!-- 📌 여기에 style 속성이 직접 들어갔습니다 -->
                <table style="min-width: 900px; table-layout: fixed;">
		            <thead>
					    <tr>
					        <th style="width: 10%;">기안 번호</th>
					        <th style="width: 20%;">기안 제목</th> <!-- 25% -> 20% 축소 -->
					        <th style="width: 15%;">신청자</th> 
					        <th style="width: 15%;">비품 명칭</th>
					        <th style="width: 10%;">대여 수량</th>
					        <th style="width: 15%;">대여 기간</th>
					        <th style="width: 15%;">결재 상태</th> <!-- 📌 10% -> 15% 확장 완료 -->
					    </tr>
					</thead>
		            <tbody>
		            <% if (eqList == null || eqList.isEmpty()) { %>
		                <tr><td colspan="7" class="empty-data">신청된 비품 대여 기안 내역이 없습니다.</td></tr>
		            <% } else { 
		                for (RentalHistoryDTO eq : eqList) { 
		                    String statusClass = "status-blue";
		                    if ("반려됨".equals(eq.getStatus())) statusClass = "status-red";
		                    else if ("반납완료".equals(eq.getStatus()) || "이용 종료".equals(eq.getStatus())) statusClass = "status-gray";
		            %>
		                <tr>
		                    <td><%= eq.getRentalNo() %></td>
		                    <td style="text-align: left; padding-left: 15px;"><%= eq.getTitle() != null ? eq.getTitle() : "제목 없음" %></td>
		                    
		                    <!-- 📌 RentalDAO.getAllDocumentList()가 JOIN으로 가져온 기안자 사원명을 출력합니다. -->
		                    <td><b><%= eq.getEmpName() != null ? eq.getEmpName() : "미상" %></b></td>
		                    
		                    <td class="emphasize"><%= eq.getEqName() != null ? eq.getEqName() : "미지정 비품" %></td>
		                    <td><b><%= eq.getReqCount() %></b> EA</td>
		                    <td><%= eq.getRentalDate() %> ~ <%= eq.getReturnDate() %></td>
		                    <td><span class="status-badge <%= statusClass %>"><%= eq.getStatus() %></span></td>
		                </tr>
		            <% } } %>
		            </tbody>
		        </table>
		    </div>
		</div>

    <!-- 6. 탭 전환 자바스크립트 제어 -->
    <script>
        function switchTab(event, tabId) {
            // 모든 탭 콘텐츠 숨기기
            const contents = document.querySelectorAll('.tab-content');
            contents.forEach(content => {
                content.style.display = 'none';
                content.classList.remove('active');
            });
            
            // 모든 탭 버튼 비활성화
            const buttons = document.querySelectorAll('.tab-btn');
            buttons.forEach(btn => {
                btn.classList.remove('active');
            });
            
            // 선택한 탭 콘텐츠 표시 및 버튼 활성화
            const targetContent = document.getElementById(tabId);
            if (targetContent) {
                targetContent.style.display = 'block';
                targetContent.classList.add('active');
            }
            event.currentTarget.classList.add('active');
        }
    </script>
</body>
</html>