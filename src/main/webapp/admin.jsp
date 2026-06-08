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
<script type="text/javascript">

	//전역 변수 설정: 스크롤 이동 상태 관리용
	let lastKeyword = "";
	let currentMatchIndex = -1;
	
	function searchAndScroll() {
	    const searchType = document.getElementById("searchType").value;
	    const keyword = document.getElementById("searchKeyword").value.trim();
	    const lowerKeyword = keyword.toLowerCase();
	
	    if (keyword === "") {
	        alert("검색어를 입력해주세요.");
	        return;
	    }
	
	    // 1. 새로운 키워드로 검색 시 기존 하이라이트 스타일 전부 제거 (기존 마크 태그 해제)
	    if (lastKeyword !== lowerKeyword) {
	        // 이전 검색 시 생성된 custom-highlight 스팬 태그를 순수 텍스트로 환원
	        document.querySelectorAll(".custom-highlight-span").forEach(span => {
	            const parent = span.parentNode;
	            if (parent) {
	                parent.replaceChild(document.createTextNode(span.textContent), span);
	                parent.normalize(); // 쪼개진 텍스트 노드 하나로 병합
	            }
	        });
	
	        // 부서 셀 하이라이트 클래스 및 배경색 리셋
	        document.querySelectorAll(".custom-target-highlight").forEach(td => {
	            td.classList.remove("custom-target-highlight");
	            td.style.backgroundColor = "";
	        });
	
	        // 2. 전체 행을 돌며 조건에 맞는 데이터 매칭 및 글자 자체에 스타일 부여
	        const rows = document.querySelectorAll(".table-wrapper tbody tr");
	        rows.forEach((row) => {
	            const tds = row.querySelectorAll("td");
	            if (tds.length >= 6) {
	                const empNoTd = tds[0];
	                const empNameTd = tds[1];
	                const deptTd = tds[5];
	
	                // [사원 번호 검색]
	                if (searchType === "all" || searchType === "emNo") {
	                    highlightSpecificText(empNoTd, keyword);
	                }
	                
	                // [성명 검색]
	                if (searchType === "all" || searchType === "emName") {
	                    highlightSpecificText(empNameTd, keyword);
	                }
	                
	                // [부서 검색] 내부 select 구조를 깨지 않기 위해 TD 통째로 스타일 적용
	                if (searchType === "all" || searchType === "emDept") {
	                    const selectEl = deptTd.querySelector("select");
	                    let deptText = selectEl ? selectEl.options[selectEl.selectedIndex].text : deptTd.textContent.trim();
	
	                    if (deptText.toLowerCase().includes(lowerKeyword)) {
	                        deptTd.classList.add("custom-target-highlight");
	                        deptTd.style.backgroundColor = "#fef08a"; 
	                    }
	                }
	            }
	        });
	
	        lastKeyword = lowerKeyword;
	        currentMatchIndex = -1; 
	    }
	
	    // 3. 스크롤 이동 대상 요소들을 리스트로 수집 (.custom-highlight-span 과 .custom-target-highlight)
	    const allTargets = document.querySelectorAll(".custom-highlight-span, .custom-target-highlight");
	    
	    if (allTargets.length > 0) {
	        currentMatchIndex++;
	        if (currentMatchIndex >= allTargets.length) {
	            currentMatchIndex = 0; 
	        }
	
	        // 4. 잡힌 타깃 위치로 화면을 부드럽게 스크롤
	        allTargets[currentMatchIndex].scrollIntoView({
	            behavior: "smooth",
	            block: "center"
	        });
	
	        document.getElementById("searchKeyword").select();
	    } else {
	        alert("일치하는 사원 또는 부서 항목을 찾을 수 없습니다.");
	        document.getElementById("searchKeyword").focus();
	        lastKeyword = "";
	        currentMatchIndex = -1;
	    }
	}
	
	// 엘리먼트 내부에서 딱 '그 글자만' 찾아내어 안전하게 스타일용 SPAN을 감싸는 함수
	function highlightSpecificText(element, keyword) {
	    if (!element || !keyword) return;
	
	    const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT, null, false);
	    const textNodes = [];
	    while (walker.nextNode()) {
	        textNodes.push(walker.currentNode);
	    }
	
	    // 텍스트 노드 역순으로 순회하면서 키워드 매칭 영역 스팬 래핑 (JSP 컴파일 간섭 우회)
	    textNodes.forEach(node => {
	        const text = node.nodeValue;
	        const lowerText = text.toLowerCase();
	        const lowerKeyword = keyword.toLowerCase();
	        let index = lowerText.indexOf(lowerKeyword);
	
	        if (index >= 0) {
	            const range = document.createRange();
	            range.setStart(node, index);
	            range.setEnd(node, index + keyword.length);
	
	            const highlightSpan = document.createElement("span");
	            highlightSpan.className = "custom-highlight-span";
	            highlightSpan.style.backgroundColor = "#fef08a";
	            highlightSpan.style.color = "#000";
	            highlightSpan.style.fontWeight = "bold";
	            highlightSpan.style.padding = "2px 4px";
	            highlightSpan.style.borderRadius = "4px";
	
	            range.surroundContents(highlightSpan);
	        }
	    });
	}


</script>
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
        <div class="search-bar-container" 
    		 style="max-width: 1160px; width: 100%; padding: 10px 0 15px 0; margin: 0 auto; box-sizing: border-box; position: sticky; top: 65px; z-index: 99; background-color: #f4f7fa;">
			    <div class="search-form" 
			         style="display: flex; gap: 10px; background: #ffffff; padding: 12px; border-radius: 12px; border: 1px solid #e2e8f0; box-shadow: 0 4px 12px -2px rgba(0, 0, 0, 0.06); align-items: center; width: 100%; box-sizing: border-box;">
			        
			        <select id="searchType" class="search-select" 
			                style="width: 130px; height: 42px; padding: 0 10px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 14px; color: #475569; outline: none; background-color: #ffffff; cursor: pointer; font-family: -apple-system, BlinkMacSystemFont, sans-serif;">
			            <option value="all">전체 검색</option>
			            <option value="emNo">사원번호</option>
			            <option value="emName">이름</option>
			            <option value="emDept">부서</option>
			        </select>
			        
			        <input type="text" id="searchKeyword" class="search-input" placeholder="이동할 비품명 또는 번호 입력..." autocomplete="off" 
			               onkeyup="if(event.key === 'Enter') searchAndScroll()"
			               onfocus="this.style.borderColor='#6366f1'" 
			               onblur="this.style.borderColor='#cbd5e1'"
			               style="flex-grow: 1; height: 42px; padding: 0 15px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 14px; box-sizing: border-box; outline: none; background-color: #ffffff; color: #334155; font-family: -apple-system, BlinkMacSystemFont, sans-serif; transition: border-color 0.15s ease;">
			        
			        <button type="button" class="btn-search" onclick="searchAndScroll()"
			                onmouseover="this.style.backgroundColor='#4f46e5'" 
			                onmouseout="this.style.backgroundColor='#6366f1'"
			                style="height: 42px; padding: 0 24px; background-color: #6366f1; color: #ffffff; border: none; border-radius: 8px; font-weight: bold; font-size: 14px; cursor: pointer; white-space: nowrap; font-family: -apple-system, BlinkMacSystemFont, sans-serif; transition: background-color 0.15s ease;">
			            검색 및 이동
			        </button>
			        
			    </div>
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