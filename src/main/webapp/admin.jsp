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
	<!-- 1. 상단 네비게이션 헤더 (최상단 고정 고유 레이어) -->
<jsp:include page="header.jsp" />
    
    <!-- 2. 페이지 대제목 영역 (고정 영역에서 완전히 제외하여 최상단 헤더 밑에 배치) -->
	<div class="container">
	
 		<!-- <div class="page-header">
			<h2>전사 직원 관리 (마스터)</h2>
			<a href="main.jsp" class="btn-back">시스템 메인으로</a>
		</div>
		페이지 대제목 영역 (고정 영역에서 완전히 제외하여 최상단 헤더 밑에 배치)
    	<div class="table-container" style="margin-top: 30px; margin-bottom: 0;">
	        <div class="headertitle">
	            <h2 style="margin: 0; font-size: 26px; font-weight: 700; color: #1e293b;">비품 대여 신청</h2>
	        </div>
    	</div> -->
	    <div class="table-title-area">
        	<h2>전사 직원 관리 (마스터)</h2>
        </div>
		<div class="table-wrapper">
			<table>
				<thead>
					<tr>
						<th>사원 번호</th>
						<th>성명</th>
						<th>권한 레벨 조정</th>
						<th>시스템 권한</th>
						<th>인사 관리 (위임/퇴사/위임변경/위임취소)</th>
						<th>부서</th>
					</tr>
				</thead>
				<tbody>
					
					<%-- 
					1. 퇴사자 인원 안보이게 -- 처리 (완)
					2. 권한 레벨 조정
							- 직급 강등 안되게 -- 처리 (완)
					3. 시스템 권한 -- 처리 (완)
					4. 인사관리( 위임 부분 ) 
						- 사장 제외하고 위임 변경 가능해야함
						- 위임 취소하는 부분 만들어야함
						- 위임은 
					5. 신규 직원 INSERT 작업시 RETIRED = 'N' DEFAULT -- 처리 (완)
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
					                    <form action="adminAction.do" method="post" style="margin: 0; display: flex; justify-content: center; gap: 5px; align-items: center;" onsubmit="return confirm('<%= emp.getEmpName() %> 사원의 직급을 수정하시겠습니까?');">
					                        <input type="hidden" name="action" value="updateLevel">
					                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
					                        <select name="newLevel">
											    <% 
											        int currentLevel = emp.getEmpLevel(); 
											        // 1단계부터 5단계까지 반복문을 돌리는 코드
											        for(int i = 1; i <= 5; i++) {
											            // 현재 직급보다 크거나 같을 때만 옵션을 생성
											            if (i >= currentLevel) {
											    %>
											                <option value="<%= i %>" <%= currentLevel == i ? "selected" : "" %>>
											                    <%= i %>단계 <%= (i==1?"(일반)": i==4?"(부서장)": i==5?"(임원)":"") %>
											                </option>
											    <% 
											            }
											        } 
											    %>
											</select>
					                        <% 
					                        	
					                        %>
					                        <button type="submit" class="btn-action btn-update">수정</button>
					                    </form>
					                <% } %>
					            </td>
					
					            <!-- 4. 시스템 권한 (최고관리자 여부) -->
					            <td>
					                <% if (isRetired) { %>
					                    <span class="badge-retired" style="color: #999;">접근불가</span>
					                <% } else if (emp.getEmpLevel() == 5) { %>
					                    <span class="badge-manager" style="color: #007bff; font-weight: bold;">최고 관리자</span>
					                <% }else if ("Y".equals(emp.getManager())) { %>
				                    <span class="badge-manager" style="color: #007bff; font-weight: bold;">중간 관리자</span>
				                	<% } else { %>
					                    <span class="badge-normal">일반 사원</span>
					                <% } %>
					            </td>
								<!-- 5. 인사 관리 (위임/퇴사/위임변경/위임취소) -->
								<td>
								    <% 
								        if (isRetired) { 
								    %>
								        <span style="color: #adb5bd; font-size: 13px;">-</span>
								    <% 
								        } else if (loginEmp != null && emp.getEmpNo() == loginEmp.getEmpNo()) { 
								    %>
								        <span style="color: #007bff; font-size: 12px; font-weight: bold;">본인(마스터)</span>
								    <% 
								        } else { 
								            // 사장(5단계) 여부 확인
								            boolean isCEO = (emp.getEmpLevel() == 5);
								            // 현재 최고 관리자 권한 여부 확인 (Manager 컬럼이 'Y'인지)
								            boolean isManager = "Y".equals(emp.getManager());
								            // 총 Manager 직원 수
								            //int isGetManagerCount = emp.getCount_manager();
								    %>
								        <div style="display: flex; gap: 5px; justify-content: center; align-items: center;">
								            
								            <% if (!isCEO) { // 1. 사장은 모든 권한 수정 대상에서 제외 %>
								                
								                <% if (!isManager) { // 2. 최고관리자가 아닌 일반 인원들 %>
								                    <!-- 위임 버튼 if 들어가서 처리 해보자 -->
								                    <form action="adminAction.do" method="post" style="display: inline;" >
								                        <input type="hidden" name="action" value="transferManager">
								                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
								                        <button type="submit" class="btn-action btn-transfer" 
								                                onclick="return confirm('이 사원에게 관리자 권한을 부여하시겠습니까?');">위임</button>
								                    </form>
								                    
								                    <!-- 퇴사 처리 (사장 제외 공통) -->
									                <form action="adminAction.do" method="post" style="display: inline;">
									                    <input type="hidden" name="action" value="deleteEmp">
									                    <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
									                    <button type="submit" class="btn-action btn-delete" 
									                            onclick="return confirm('해당 사원을 퇴사 처리하시겠습니까?');">퇴사</button>
									                </form>
								                
								
								                <% } 
								                else { // 3. 이미 최고관리자인 인원 (사장은 아님) %>
								                    <!-- 위임 취소 버튼 -->
								                    <%-- <form action="adminAction.do" method="post" style="display: inline;">
								                        <input type="hidden" name="action" value="cancelManager">
								                        <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
								                        <button type="submit" class="btn-action" 
								                                style="background-color: #6c757d; color: white;" 
								                                onclick="return confirm('이 사원의 관리자 권한을 박탈(취소)하시겠습니까?');">위임취소</button>
								                    </form> --%>
								                <% } %>
								
								                
								
								            <% } else { %>
								                <!-- 사장(Level 5)인 경우 표시 -->
								                <span style="color: #dc3545; font-size: 12px; font-weight: bold;">수정 불가(대표)</span>
								            <% } %>
								            
								        </div>
								    <% } %>
								</td>
								<!-- 6. 부서 -->
								<td>
								    <% if (isRetired) { %>
								        <span style="color: #dc3545; font-weight: bold; font-size: 13px;">퇴사 처리됨</span>
								    <% } else { %>
								        <form action="adminAction.do" method="post" style="margin: 0; display: flex; justify-content: center; gap: 5px; align-items: center;" onsubmit="return confirm('<%= emp.getEmpName() %> 사원의 부서를 수정하시겠습니까?');">
								            <input type="hidden" name="action" value="updateDept">
								            <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
								            
								            <select name="newDept" style="padding: 4px; border: 1px solid #ced4da; border-radius: 4px;">
								                <% 
								                    // 현재 사원의 부서명 또는 부서코드를 가져옴 (Getter명은 DTO에 맞게 수정하세요)
								                    String currentDept = emp.getDept(); 
								                %>
								                <option value="경영지원팀" <%= "경영지원팀".equals(currentDept) ? "selected" : "" %>>경영지원팀</option>
												<option value="기획팀"     <%= "기획팀".equals(currentDept) ? "selected" : "" %>>기획팀</option>
												<option value="재무팀"     <%= "재무팀".equals(currentDept) ? "selected" : "" %>>재무팀</option>
												<option value="영업팀"     <%= "영업팀".equals(currentDept) ? "selected" : "" %>>영업팀</option>
												<option value="개발팀"     <%= "개발팀".equals(currentDept) ? "selected" : "" %>>개발팀</option>
								            </select>
								            <button type="submit" class="btn-action btn-update">수정</button>
								        </form>
								    <% } %>
								</td>
					            <%-- <!-- 5. 인사 관리 (위임/퇴사) -->
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
					            </td> --%>
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

				<!-- 관리자 부서 추가 -->
		        <select name="dept" style="padding: 10px; border: 1px solid #ced4da; border-radius: 4px;">
		                <option value="경영지원팀">경영지원팀</option>
						<option value="기획팀">기획팀</option>
						<option value="재무팀">재무팀</option>
						<option value="영업팀">영업팀</option>
						<option value="개발팀">개발팀</option>
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