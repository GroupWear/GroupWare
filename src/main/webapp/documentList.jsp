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
    
 	// 컨트롤러에서 넘어온 활성화 타겟 탭 정보 확인
    String activeTab = (String) request.getAttribute("activeTab");
    boolean isEqTab = "equipment".equals(activeTab); // 상세화면에서 복귀했는지 여부
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
    <div class="dashboard-container" style="padding-top: 80px;"> <!-- 💡 헤더바(65px) 공간 확보용 탑 패딩 주입 -->
        
        <!-- 📌 [순서 보정 1]: 대제목이 무조건 검색바보다 먼저 렌더링되어야 합니다. -->
        <div class="headertitle" style="margin-bottom: 20px;">
            <h2 style="margin: 0; font-size: 22px; font-weight: 700; color: #1e293b;">통합 기안 문서함</h2>
        </div>
        
         <!-- 3. 실시간 통합 검색바 컴포넌트 (버튼 클릭 전용 제어형) -->
        <div class="search-bar-container" style="position: static; padding: 0 0 20px 0;">
            <div class="search-form">
                <!-- 검색 분류 선택 -->
                <select id="searchType" class="search-select" onchange="toggleSearchInput()">
                    <option value="all">전체 검색</option>
                    <option value="title">기안 제목</option>
                    <option value="empName">신청자 (비품)</option>
                    <option value="status">결재 상태</option>
                </select>
                
                <!-- 📌 [수정]: onkeyup 속성을 지우고, 엔터(Enter) 쳤을 때만 작동하도록 onkeydown 가드를 심었습니다. -->
                <input type="text" id="keyword" class="search-input" placeholder="검색어를 입력한 후 우측 검색 버튼을 누르거나 엔터를 치세요..." 
                       onkeydown="if(event.keyCode==13) { filterDocuments(); return false; }">
                
                <!-- 결재 상태 고유 드롭다운 버튼 (선택 시 즉시 검색) -->
                <select id="statusSelect" class="search-select search-input" style="display: none; flex-grow: 1;" onchange="filterDocuments()">
                    <option value="">-- 결재 상태를 선택하세요 --</option>
                    <option value="승인대기">승인대기</option>
                    <option value="대여중">대여중</option>
                    <option value="미반납">미반납</option>
                    <option value="반납완료">반납완료</option>
                    <option value="반려됨">반려됨</option>
                </select>
                
                <!-- 📌 진짜 주인공인 검색 버튼 -->
                <button type="button" class="btn-search" onclick="filterDocuments()">검색</button>
            </div>
        </div>
        
        <!-- 3. 기안 분류별 탭 버튼 영역 (isEqTab 결과에 따라 active 위치 자동 분기) -->
		<div class="tab-menu-container">
		    <button class="tab-btn <%= !isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-leave')">휴가 신청 기안</button>
		    <button class="tab-btn <%= isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-equipment')">비품 대여 신청 기안</button>
		</div>

        <!-- 4. 탭 내부 콘텐츠: 휴가 신청 테이블 -->
		<div id="tab-leave" class="tab-content <%= !isEqTab ? "active" : "" %>" style="display: <%= !isEqTab ? "block" : "none" %>;">
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
                        <!-- 👍 수정 후: data-doc-id 속성에 고유 문서 번호를 바인딩해 줍니다 -->
						<tr data-doc-id="leave_<%= leave.getLeaveNo() %>">
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
        <div id="tab-equipment" class="tab-content" style="display: none;">
            <!-- 📌 table-wrapper가 찌그러지지 않도록 부모 폭을 100%로 확실히 명시합니다. -->
            <div class="table-wrapper" style="width: 100%; max-width: none;">
                
                <!-- 📌 테이블에 class="eq-table"을 주입하고 width: 100%로 시원하게 폅니다. -->
                <table class="eq-table" style="width: 100%; table-layout: fixed;">
                    <thead>
                        <tr>
                            <th style="width: 10%; text-align: center;">기안 번호</th>
                            <th style="width: 35%; text-align: center;">기안 제목</th>
                            <th style="width: 15%; text-align: center;">신청자</th> 
                            <th style="width: 10%; text-align: center;">대여 수량</th>
                            <th style="width: 15%; text-align: center;">대여 기간</th>
                            <th style="width: 15%; text-align: center;">결재 상태</th>
                        </tr>
                    </thead>
		            <tbody>
					    <% 
					        if (eqList == null || eqList.isEmpty()) { 
					    %>
					        <tr><td colspan="6" class="empty-data">신청된 비품 대여 기안 내역이 없습니다.</td></tr>
					    <% 
					        } else { 
					            boolean hasVisibleData = false; // 직급 필터링 후 내 화면에 표시될 데이터가 1개라도 있는지 체크하는 플래그
					            
					            // 💡 [내 직급 레벨 수령]
					            int myLevel = loginEmp.getEmpLevel(); 
					            
					            for (RentalHistoryDTO eq : eqList) { 
					                // 💡 [기안자 직급 레벨 수령]: DTO 내부의 empLevel 변수와 매핑됩니다.
					                int targetLevel = eq.getEmpLevel(); 
					
					                // 💡 [핵심 권한 체크 가드 조건문]
					                // 1. 내가 최고관리자이거나 ("Y")
					                // 2. 내가 이 기안의 작성자 본인이거나 (eq.getEmpNo() 사번 비교)
					                // 3. 내 직급 레벨이 기안자 직급 레벨보다 크거나 같을(myLevel >= targetLevel) 때만 노출
					                if ("Y".equals(loginEmp.getManager()) || 
					                    loginEmp.getEmpNo() == eq.getEmpNo() || 
					                    myLevel >= targetLevel) {
					                    
					                    hasVisibleData = true; // 조건에 맞아 화면에 노출된 데이터가 존재함을 기록
					                    
					                    // 배지 스타일 매핑 로직
					                    String statusClass = "status-blue";
					                    if ("반려됨".equals(eq.getStatus())) statusClass = "status-red";
					                    else if ("반납완료".equals(eq.getStatus()) || "이용 종료".equals(eq.getStatus())) statusClass = "status-gray";
					                    
					                    // 현재 내 결재 차례인지 여부 판별
					                    boolean isMyApprovalTurn = "승인대기".equals(eq.getStatus()) && (eq.getApprovalStep() == loginEmp.getEmpLevel());
					    %>
					        <tr data-doc-id="rent_<%= eq.getRentalNo() %>">
					            <td><%= eq.getRentalNo() %></td>
					            
					            <td class="td-title">
					                <a href="rentalDetail.do?rentalNo=<%= eq.getRentalNo() %>" class="title-link">
					                    <%= eq.getTitle() != null ? eq.getTitle() : "제목 없음" %>
					                </a>
					            </td>
					            
					            <!-- 💡 DTO 규격에 맞춰 이름과 직급 레벨(LV)을 안전하게 결합 출력합니다 -->
					            <td>
					                <b><%= eq.getEmpName() != null ? eq.getEmpName() : "미상" %></b>
					                <span style="font-size: 11px; color: #64748b; font-weight: 500; margin-left: 2px;">
					                    (Lv.<%= eq.getEmpLevel() %>)
					                </span>
					            </td>
					            
					            <td class="td-qty">
					                <span class="qty-wrap"><b><%= eq.getReqCount() %></b>&nbsp;EA</span>
					            </td>
					            
					            <td><%= eq.getRentalDate() %> ~ <%= eq.getReturnDate() %></td>
					            
					            <td style="vertical-align: middle; padding: 10px 0;">
					                <span class="status-badge <%= statusClass %>" style="margin-bottom: 4px;"><%= eq.getStatus() %></span>
					                
					                <% if (isMyApprovalTurn) { %>
					                    <div class="approval-blink" style="font-size: 11px; color: #ef4444; font-weight: 800; margin-top: 3px;">
					                        결재 바랍니다
					                    </div>
					                <% } %>
					            </td>
					        </tr>
					    <% 
					                } // 💡 권한 가드 IF문 종료
					            } // 💡 FOR 반복문 종료 
					            
					            // 💡 [예외 차단 가드]: 내 직급이 낮아 상급자들의 기안이 전부 필터링되어 리스트가 텅 비었을 때
					            if (!hasVisibleData) {
					    %>
					        <tr><td colspan="6" class="empty-data">조회 가능한 기안 내역이 없습니다.</td></tr>
					    <% 
					            }
					        } 
					    %>
				    </tbody>
		        </table>
		    </div>
		</div>
		
		<style>
		.approval-blink {
		    animation: alert-flash 0.8s infinite alternate ease-in-out;
		}
		
		@keyframes alert-flash {
		    from {
		        opacity: 0.3;
		        transform: scale(0.98);
		    }
		    to {
		        opacity: 1;
		        transform: scale(1);
		    }
		}
		</style>

    <!-- 6. 탭 전환, 드롭다운 및 고성능 필터/스크롤 엔진 (최종 완결본) -->
	<script>
	let currentMatchIndex = -1;
	let matchRows = []; // 검색 조건에 매칭된 tr 엘리먼트들을 담을 배열
	let lastKeyword = ""; // 이전 검색어를 저장하여 새 검색인지 연속 이동인지 판별
	
	function switchTab(event, tabId) {
	    const contents = document.querySelectorAll('.tab-content');
	    contents.forEach(content => { content.style.display = 'none'; content.classList.remove('active'); });
	    
	    const buttons = document.querySelectorAll('.tab-btn');
	    buttons.forEach(btn => { btn.classList.remove('active'); });
	    
	    const targetContent = document.getElementById(tabId);
	    if (targetContent) { 
	        targetContent.style.display = 'block'; 
	        targetContent.classList.add('active'); 
	        
	        // 💡 [핵심 안전장치]: 탭이 전환되거나 초기 로드될 때 숨겨진 행들을 강제로 전부 노출시킵니다.
	        const rows = targetContent.querySelectorAll('tbody tr');
	        rows.forEach(row => { row.style.display = ""; });
	    }
	    if (event && event.currentTarget) event.currentTarget.classList.add('active');
	    
	    // 탭 전환 시 검색 인터페이스 완전 리셋 및 전체 화면 노출
	    document.getElementById("searchType").value = "all";
	    document.getElementById("keyword").value = "";
	    document.getElementById("statusSelect").value = "";
	    toggleSearchInput();
	    
	    currentMatchIndex = -1;
	    matchRows = [];
	    lastKeyword = "";
	}
	
	function toggleSearchInput() {
	    const searchType = document.getElementById("searchType").value;
	    const keywordInput = document.getElementById("keyword");
	    const statusSelect = document.getElementById("statusSelect");
	    
	    if (searchType === "status") {
	        keywordInput.style.display = "none"; statusSelect.style.display = "block"; keywordInput.value = "";
	    } else {
	        keywordInput.style.display = "block"; statusSelect.style.display = "none"; statusSelect.value = "";
	    }
	}
	
	// 📌 [순수 필터링 및 고성능 부드러운 스크롤 엔진]
	function filterDocuments() {
	    const searchType = document.getElementById("searchType").value;
	    let keyword = (searchType === "status") ? document.getElementById("statusSelect").value : document.getElementById("keyword").value;
	    
	    if (!keyword) keyword = "";
	    keyword = keyword.trim().toLowerCase();
	
	    // 검색어가 바뀐 경우 인덱스 및 타겟 배열 초기화
	    if (keyword !== lastKeyword) {
	        currentMatchIndex = -1;
	        matchRows = [];
	        lastKeyword = keyword;
	    }
	
	    const activeTab = document.querySelector('.tab-content.active');
	    if (!activeTab) return;
	    
	    const rows = activeTab.querySelectorAll('tbody tr');
	    
	    // DOM 탐색 및 조건 매칭 데이터 수집 (최초 1회 또는 검색어 변경 시에만 배열 빌드)
	    if (matchRows.length === 0 && keyword !== "") {
	        rows.forEach(row => {
	            if (row.querySelector('.empty-data')) return; // 데이터 없음 행 방어
	            
	            const aTag = row.querySelector('td a');
	            const bTag = row.querySelector('td b');
	            const badge = row.querySelector('.status-badge');
	            const cells = row.getElementsByTagName('td');
	            
	            // 공백 및 유니코드 리스크 완벽 제거 가공
	            const leaveReasonText = cells && cells[3] ? cells[3].innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
	            const titleText = aTag ? aTag.innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
	            const empNameText = bTag ? bTag.innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
	            const statusText = badge ? badge.innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
	            const fullText = row.innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase();
	            
	            let isMatch = false;
	            
	            if (searchType === "all") {
	                isMatch = fullText.includes(keyword);
	            } else if (searchType === "title") {
	                if (titleText.includes(keyword) || (titleText === "" && leaveReasonText.includes(keyword))) isMatch = true;
	            } else if (searchType === "empName") {
	                if (empNameText.includes(keyword)) isMatch = true;
	            } else if (searchType === "status") {
	                if (statusText.includes(keyword) || (fullText.includes("결재 바랍니다") && "결재 바랍니다".includes(keyword))) {
	                    isMatch = true;
	                }
	            }
	            
	            if (isMatch) {
	                matchRows.push(row);
	            }
	        });
	    }
	
	    // 레이아웃 상태 제어 (화면 노출/숨김)
	    rows.forEach(row => {
	        if (row.querySelector('.empty-data')) return;
	        
	        if (keyword === "") {
	            row.style.display = ""; // 검색어 비어있으면 전체 노출
	        } else {
	            if (matchRows.includes(row)) {
	                row.style.display = "";
	            } else {
	                row.style.display = "none";
	            }
	        }
	    });
	
	    // 📌 [순차 스크롤 무빙 시스템 작동]
	    if (keyword !== "") {
	        if (matchRows.length > 0) {
	            currentMatchIndex = (currentMatchIndex + 1) % matchRows.length;
	
	            setTimeout(() => {
	                matchRows[currentMatchIndex].scrollIntoView({
	                    behavior: "smooth",
	                    block: "center"
	                });
	            }, 50);
	
	            if (searchType !== "status") {
	                document.getElementById("keyword").select();
	            }
	        } else {
	            alert("일치하는 기안 내역을 찾을 수 없습니다.");
	            currentMatchIndex = -1;
	            matchRows = [];
	        }
	    }
	}
	
	// 📌 [상세 정보 연동]: 목록의 행을 클릭했을 때 서블릿 상세페이지로 이동시키는 함수
	function goToDetail(rentalNo) {
	    if (!rentalNo) {
	        alert("유효하지 않은 문서 번호입니다.");
	        return;
	    }
	    location.href = "rentalDetail.do?rentalNo=" + rentalNo;
	}
	
	// 📌 [신규 보정]: 브라우저 로딩 시 파라미터(?tab=equipment)를 정밀 분석하여 화면 복구
	window.addEventListener('DOMContentLoaded', function() {
	    const urlParams = new URLSearchParams(window.location.search);
	    const tabParam = urlParams.get('tab');
	    
	    // 파라미터가 비품대여(equipment)이거나 해시가 붙어있을 때 작동
	    if (tabParam === 'equipment' || window.location.hash === '#equipment') {
	        switchTab(null, 'tab-equipment');
	        
	        // 버튼 탭 스타일 동적 활성화 (클래스 매칭 방어 코드)
	        const eqTabBtn = document.querySelector('.tab-btn[onclick*="tab-equipment"]');
	        if (eqTabBtn) eqTabBtn.classList.add('active');
	    }
	});
	</script>
</body>
</html>