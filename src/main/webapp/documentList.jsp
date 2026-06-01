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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.6">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/documentList.css?v=1.1">
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

    <div class="dashboard-container" style="padding-top: 80px;"> <div class="headertitle" style="margin-bottom: 20px;">
            <h2 style="margin: 0; font-size: 22px; font-weight: 700; color: #1e293b;">통합 기안 문서함</h2>
        </div>
        
         <div class="search-bar-container" style="position: static; padding: 0 0 20px 0;">
            <div class="search-form">
                <select id="searchType" class="search-select" onchange="toggleSearchInput()">
                    <option value="all">전체 검색</option>
                    <option value="title">기안 제목</option>
                    <option value="empName">신청자 (비품)</option>
                    <option value="status">결재 상태</option>
                </select>
                
                <input type="text" id="keyword" class="search-input" placeholder="검색어를 입력한 후 우측 검색 버튼을 누르거나 엔터를 치세요..." 
                       onkeydown="if(event.keyCode==13) { filterDocuments(); return false; }">
                
                <select id="statusSelect" class="search-select search-input" style="display: none; flex-grow: 1;" onchange="filterDocuments()">
                    <option value="">-- 결재 상태를 선택하세요 --</option>
                    <option value="승인대기">승인대기</option>
                    <option value="대여중">대여중</option>
                    <option value="미반납">미반납</option>
                    <option value="반납완료">반납완료</option>
                    <option value="반려됨">반려됨</option>
                </select>
                
                <button type="button" class="btn-search" onclick="filterDocuments()">검색</button>
            </div>
        </div>
        
        <div class="tab-menu-container">
            <button class="tab-btn <%= !isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-leave')">휴가 신청 기안</button>
            <button class="tab-btn <%= isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-equipment')">비품 대여 신청 기안</button>
        </div>

        <div id="tab-leave" class="tab-content <%= !isEqTab ? "active" : "" %>" style="display: <%= !isEqTab ? "block" : "none" %>;">
            <div class="table-wrapper">
                <table style="min-width: 900px; table-layout: fixed;">
                    <thead>
                        <tr>
                            <th style="width: 10%;">문서 번호</th> 
                            <th style="width: 25%;">휴가 기간</th>
                            <th style="width: 10%;">사용 일수</th> 
                            <th style="width: 40%;">휴가 사유</th> 
                            <th style="width: 15%;">결재 상태</th> 
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
                        <tr data-doc-id="leave_<%= leave.getLeaveNo() %>" onclick="goToDetail('leave', '<%= leave.getLeaveNo() %>')" style="cursor: pointer;">
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

         <div id="tab-equipment" class="tab-content" style="display: none;">
            <div class="table-wrapper" style="width: 100%; max-width: none;">
                <table class="eq-table" style="width: 100%; table-layout: fixed;">
                    <thead>
                        <tr>
                            <th style="width: 10%; text-align: center;">기안 번호</th>
                            <th style="width: 35%; text-align: center;">기안 제목</th>
                            <th style="width: 15%; text-align: center;">기안자</th> 
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
					            boolean hasVisibleData = false; 
					            int myLevel = loginEmp.getEmpLevel(); 
					            
					            for (RentalHistoryDTO eq : eqList) { 
					                int targetLevel = eq.getEmpLevel(); 
					                
					                if ("Y".equals(loginEmp.getManager()) || 
					                    loginEmp.getEmpNo() == eq.getEmpNo() || 
					                    myLevel >= targetLevel) {
					                    
					                    hasVisibleData = true; 
					                    
					                    // 💡 [화면단 상태 보정]: 퇴사자인데 아직 '승인대기' 상태라면 '반려됨'으로 우회
					                    boolean isRetiredCreator = (targetLevel == 0);
					                    String currentStatus = eq.getStatus();
					                    
					                    if (isRetiredCreator && "승인대기".equals(currentStatus)) {
					                        currentStatus = "반려됨";
					                    }
					                    
					                    // 1. 보정된 상태(currentStatus)를 기준으로 배지 스타일 클래스 매핑
					                    String statusClass = "status-blue";
					                    String displayStatusText = currentStatus; // 화면에 그려질 실제 상태 텍스트
					                    
					                    if ("반려됨".equals(currentStatus)) { 
					                        statusClass = "status-red";
					                        // 퇴사자로 인한 자동 반려 건일 경우 텍스트 치환
					                        if (isRetiredCreator) {
					                            displayStatusText = "반려됨";
					                        }
					                    } else if ("반납완료".equals(currentStatus) || "이용 종료".equals(currentStatus)) { 
					                        statusClass = "status-gray";
					                    }
					                    
					                    // 2. 보정된 상태를 기반으로 결재 알림창 노출 여부 제어 (반려 상태가 되었으므로 퇴사자 건은 자동 false 처리됨)
					                    boolean isMyApprovalTurn = "승인대기".equals(currentStatus) && (eq.getApprovalStep() == loginEmp.getEmpLevel());
					                    
					                    String displayName = eq.getEmpName() != null ? eq.getEmpName() : "미상";
					                    String nameStyle = ""; 
					                    
					                    if (isRetiredCreator) {
					                        displayName = "퇴사자";
					                        nameStyle = "color: #94a3b8"; 
					                    }
					    %>
					        <tr data-doc-id="rent_<%= eq.getRentalNo() %>" onclick="goToDetail('rental', '<%= eq.getRentalNo() %>')", style="cursor: pointer;">
					            <td><%= eq.getRentalNo() %></td>
					            
					            <td class="td-title" style="text-align: center; padding: 10px 15px;">
								    <a href="rentalDetail.do?rentalNo=<%= eq.getRentalNo() %>" 
								       class="title-link" 
								       style="color: #6366f1; font-weight: 600; text-decoration: none; cursor: pointer; display: inline-block;"
								       onmouseover="this.style.textDecoration='underline'; this.style.color='#0284c7';"
								       onmouseout="this.style.textDecoration='none'; this.style.color='#6366f1';">
								        <%= eq.getTitle() != null ? eq.getTitle() : "제목 없음" %>
								    </a>
								</td>
					            
					            <td>
					                <b style="<%= nameStyle %>"><%= displayName %></b>
					                
					                <% if (!isRetiredCreator) { %>
					                    <span style="font-size: 11px; color: #64748b; font-weight: 500; margin-left: 2px;">
					                        (Lv.<%= targetLevel %>)
					                    </span>
					                <% } %>
					            </td>
					            
					            <td class="td-qty">
					                <span class="qty-wrap"><b><%= eq.getReqCount() %></b>&nbsp;EA</span>
					            </td>
					            
					            <td><%= eq.getRentalDate() %> ~ <%= eq.getReturnDate() %></td>
					            
					            <td style="vertical-align: middle; padding: 10px 0;">
					                <!-- 📌 '퇴사로 인한 반려' 빨간색 배지 출력부 -->
					                <span class="status-badge <%= statusClass %>" style="margin-bottom: 4px;"><%= displayStatusText %></span>
					                
					                <% if (isMyApprovalTurn) { %>
					                    <div class="approval-blink" style="font-size: 11px; color: #ef4444; font-weight: 800; margin-top: 3px;">
					                        결재 바랍니다
					                    </div>
					                <% } %>
					            </td>
					        </tr>
					    <% 
					                } 
					            } 
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

    <script>
    let currentMatchIndex = -1;
    let matchRows = []; 
    let lastKeyword = ""; 
    
    function switchTab(event, tabId) {
        const contents = document.querySelectorAll('.tab-content');
        contents.forEach(content => { content.style.display = 'none'; content.classList.remove('active'); });
        
        const buttons = document.querySelectorAll('.tab-btn');
        buttons.forEach(btn => { btn.classList.remove('active'); });
        
        const targetContent = document.getElementById(tabId);
        if (targetContent) { 
            targetContent.style.display = 'block'; 
            targetContent.classList.add('active'); 
            
            const rows = targetContent.querySelectorAll('tbody tr');
            rows.forEach(row => { row.style.display = ""; });
        }
        if (event && event.currentTarget) event.currentTarget.classList.add('active');
        
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
    
    function filterDocuments() {
        const searchType = document.getElementById("searchType").value;
        let keyword = (searchType === "status") ? document.getElementById("statusSelect").value : document.getElementById("keyword").value;
        
        if (!keyword) keyword = "";
        keyword = keyword.trim().toLowerCase();
    
        if (keyword !== lastKeyword) {
            currentMatchIndex = -1;
            matchRows = [];
            lastKeyword = keyword;
        }
    
        const activeTab = document.querySelector('.tab-content.active');
        if (!activeTab) return;
        
        const rows = activeTab.querySelectorAll('tbody tr');
        
        if (matchRows.length === 0 && keyword !== "") {
            rows.forEach(row => {
                if (row.querySelector('.empty-data')) return; 
                
                const titleSpan = row.querySelector('.title-link');
                const bTag = row.querySelector('td b');
                const badge = row.querySelector('.status-badge');
                const cells = row.getElementsByTagName('td');
                
                const leaveReasonText = cells && cells[3] ? cells[3].innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
                const titleText = titleSpan ? titleSpan.innerText.replace(/\u00a0/g, " ").replace(/\s+/g, " ").toLowerCase().trim() : "";
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
    
        rows.forEach(row => {
            if (row.querySelector('.empty-data')) return;
            
            if (keyword === "") {
                row.style.display = ""; 
            } else {
                if (matchRows.includes(row)) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            }
        });
    
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
    
    // 📌 [다형성 분기 엔진 엔진 교정]: 타입 판별 후 서블릿 바인딩 주소를 명확하게 파싱합니다.
    function goToDetail(type, no) {
        if (!no) {
            alert("유효하지 않은 문서 번호입니다.");
            return;
        }
        
        if (type === 'leave') {
            location.href = "leaveDetail.do?leaveNo=" + no;
        } else if (type === 'rental') {
            location.href = "rentalDetail.do?rentalNo=" + no;
        }
    }
    
    window.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const tabParam = urlParams.get('tab');
        
        if (tabParam === 'equipment' || window.location.hash === '#equipment') {
            switchTab(null, 'tab-equipment');
            
            const eqTabBtn = document.querySelector('.tab-btn[onclick*="tab-equipment"]');
            if (eqTabBtn) eqTabBtn.classList.add('active');
        }
    });
    </script>
</body>
</html>