<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="com.groupware.dao.EmployeeDAO"%>
<%
	EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
	if (loginEmp == null) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	List<EmployeeDTO> empList = (List<EmployeeDTO>) request.getAttribute("reserveList");
	
	// 데이터 가져오기
    EmployeeDAO resDao = new EmployeeDAO();
    List<EmployeeDTO> reserveList = resDao.getAllEmployees();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 사원 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
</head>
<body>
	<div class="container">
		<div class="page-header">
			<h2>전사 직원 관리 (마스터)</h2>
			<a href="main.jsp" class="btn-back">시스템 메인으로</a>
		</div>

		<div class="table-wrapper">
			<table>
				<thead>
					<tr>
						<th>사원 번호</th>
						<th>성명</th>
						<th>권한 레벨 조정</th>
						<th>시스템 권한</th>
						<th>인사 관리 (위임/퇴사)</th>
					</tr>
				</thead>
				<tbody>
					
					<%-- 
					1. 퇴사자 인원 안보이게 -- 처리 (완)
					2. 권한 레벨 조정
							- 직급 강등 안되게
					3. 시스템 권한 
					4. 인사관리
					5. 신규 직원 INSERT 작업시 RETIRED = 'N' DEFAULT
					--%>
					
			
					 	<%
					        // 위쪽 선언부에서 가져온 reserveList 사용
					        if (reserveList != null && !reserveList.isEmpty()) {
					            for (EmployeeDTO emp : reserveList) {
					                // 퇴사자 여부 판별 (예: retired 컬럼/상태값 확인)
					                // Retired 컬럼에서 Y인경우는 퇴사지 N인 경우는 근무자
					                boolean isRetired = "Y".equals(emp.getRetired()); 
					              
					                if(isRetired) 
					                	continue;
					    %>
					        	<tr <%= isRetired ? "style='opacity: 0.6; background-color: #f8f9fa;'" : "" %>> 					        
					            <!-- 1. 사원 번호 -->
					            <td style="color: #6c757d;"><%= emp.getEmpNo() %></td>
					            
					            <!-- 2. 성명 -->
					            <td style="font-weight: 600; color: <%= isRetired ? "#adb5bd" : "#343a40" %>;">
					                <%= emp.getEmpName() %>
					            </td>
					
					            <!-- 3. 권한 레벨 조정 -->
					            <td>
					                <% if (isRetired) { %>
					                    <span style="color: #dc3545; font-weight: bold; font-size: 13px;">퇴사 처리됨</span>
					                <% } else { %>
					                    <form action="adminAction.do" method="post" style="margin: 0; display: flex; justify-content: center; gap: 5px; align-items: center;">
					                        <input type="hidden" name="action" value="updateLevel">
					                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
					                        <select name="newLevel">
					                            <option value="1" <%= emp.getEmpLevel() == 1 ? "selected" : "" %>>1단계 (일반)</option>
					                            <option value="2" <%= emp.getEmpLevel() == 2 ? "selected" : "" %>>2단계</option>
					                            <option value="3" <%= emp.getEmpLevel() == 3 ? "selected" : "" %>>3단계</option>
					                            <option value="4" <%= emp.getEmpLevel() == 4 ? "selected" : "" %>>4단계 (부서장)</option>
					                            <option value="5" <%= emp.getEmpLevel() == 5 ? "selected" : "" %>>5단계 (임원)</option>
					                        </select>
					                        <button type="submit" class="btn-action btn-update">수정</button>
					                    </form>
					                <% } %>
					            </td>
					
					            <!-- 4. 시스템 권한 (최고관리자 여부) -->
					            <td>
					                <% if (isRetired) { %>
					                    <span class="badge-retired" style="color: #999;">접근불가</span>
					                <% } else if ("Y".equals(emp.getManager())) { %>
					                    <span class="badge-manager" style="color: #007bff; font-weight: bold;">최고 관리자</span>
					                <% } else { %>
					                    <span class="badge-normal">일반 사원</span>
					                <% } %>
					            </td>
					
					            <!-- 5. 인사 관리 (위임/퇴사) -->
					            <td>
					                <% if (isRetired) { %>
					                    <span style="color: #adb5bd; font-size: 13px;">-</span>
					                <% } else if (loginEmp != null && emp.getEmpNo() != loginEmp.getEmpNo()) { %>
					                    <form action="adminAction.do" method="post" style="display: inline;">
					                        <input type="hidden" name="action" value="transferManager">
					                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
					                        <button type="submit" class="btn-action btn-transfer" 
					                                onclick="return confirm('관리자 권한을 위임하시겠습니까?');">위임</button>
					                    </form>
					
					                    <form action="adminAction.do" method="post" style="display: inline;">
					                        <input type="hidden" name="action" value="deleteEmp">
					                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
					                        <button type="submit" class="btn-action btn-delete" 
					                                onclick="return confirm('해당 사원을 퇴사 처리하시겠습니까?');">퇴사</button>
					                    </form>
					                <% } else { %>
					                    <span style="color: #007bff; font-size: 12px; font-weight: bold;">본인(마스터)</span>
					                <% } %>
					            </td>
					        </tr>
					    <% 
					            } // for end
					        } else { 
					    %>
					        <tr>
					            <td colspan="5" style="text-align: center; padding: 20px;">등록된 사원이 없습니다.</td>
					        </tr>
					    <% } %>
					
					
				</tbody>
			</table>
		</div>
		<br>
		<hr>
		<br>
		<!-- 관리자 사원 등록 폼 영역 -->
		<div class="insert-box"
			style="background-color: #ffffff; padding: 25px; border-radius: 6px; margin-bottom: 30px; border: 1px solid #e9ecef; border-left: 4px solid #343a40;">
			<h3 style="margin-top: 0;">신규 사원 사전 등록 (초기 세팅)</h3>
			<form action="insertEmp.do" method="post"
				style="display: flex; gap: 10px; align-items: center;">

				<!-- 사번 입력 (DB에서 PK 역할) -->
				<input type="number" name="empNo" placeholder="사번 (숫자)" required
					style="padding: 10px; border: 1px solid #ced4da; border-radius: 4px; width: 120px;">

				<!-- 사원명 입력 -->
				<input type="text" name="empName" placeholder="사원 성명" required
					style="padding: 10px; border: 1px solid #ced4da; border-radius: 4px; flex: 1;">

				<!-- 직급 선택 -->
				<select name="empLevel" required
					style="padding: 10px; border: 1px solid #ced4da; border-radius: 4px;">
					<option value="1">1단계 (일반 사원)</option>
					<option value="2">2단계 (대리)</option>
					<option value="3">3단계 (과장)</option>
					<option value="4">4단계 (차장)</option>
					<option value="5">5단계 (대표/임원)</option>
				</select>

				<!-- 관리자 메뉴 접근 권한 -->
				<select name="manager" required
					style="padding: 10px; border: 1px solid #ced4da; border-radius: 4px;">
					<option value="N">일반 권한 (N)</option>
					<option value="Y">관리자 권한 (Y)</option>
				</select>

				<button type="submit"
					style="padding: 10px 20px; background-color: #343a40; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer;">사원
					등록</button>
			</form>
			<p style="margin: 10px 0 0 0; font-size: 12px; color: #6c757d;">*
				등록 후 해당 사원이 직접 회원가입 메뉴에서 사번을 인증하고 비밀번호를 세팅해야 합니다.</p>
		</div>

	</div>
</body>
</html>