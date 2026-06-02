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

<jsp:include page="header.jsp" />

    <%-- 🛠️ [교정 1] 컨테이너가 좌우로 꽉 차도록 max-width 마진 제한을 해제하고 전체 정렬 싱크를 맞춤 --%>
    <div class="dashboard-container" style="padding-top: 20px; max-width: 100%; width: 100%; box-sizing: border-box;"> 
        
        <div class="headertitle" style="margin-bottom: 15px;">
            <h2 style="margin: 0; font-size: 22px; font-weight: 700; color: #1e293b;">통합 기안 문서함</h2>
        </div>
        
        <%-- 검색바 영역 --%>
        <div class="search-bar-container" style="width: 100%; max-width: 100%; box-sizing: border-box;">
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
        
        <%-- 탭 메뉴 영역 --%>
        <div class="tab-menu-container" style="width: 100%; max-width: 100%;">
            <button class="tab-btn <%= !isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-leave')">휴가 신청 기안</button>
            <button class="tab-btn <%= isEqTab ? "active" : "" %>" onclick="switchTab(event, 'tab-equipment')">비품 대여 신청 기안</button>
        </div>

        <%-- 탭 1: 휴가 신청 기안 콘텐츠 --%>
        <div id="tab-leave" class="tab-content <%= !isEqTab ? "active" : "" %>" style="display: <%= !isEqTab ? "block" : "none" %>; width: 100%;">
            <%-- 🛠️ [교정 2] 테이블 래퍼 카드가 웅크러들지 않고 100% 확장되도록 width 인라인 강제 지정 --%>
            <div class="table-wrapper" style="width: 100% !important; max-width: 100% !important; box-sizing: border-box; background: #ffffff; border-radius: 8px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
                <table style="width: 100%; table-layout: fixed; border-collapse: collapse;">
                    <thead>
                        <tr>
                            <th style="width: 12%;">문서 번호</th> 
                            <th style="width: 25%;">휴가 기간</th>
                            <th style="width: 12%;">사용 일수</th> 
                            <th style="width: 39%;">휴가 사유</th> 
                            <th style="width: 12%;">결재 상태</th> 
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
                        <tr data-doc-id="leave_<%= leave.getLeaveNo() %>">
                            <td><%= leave.getLeaveNo() %></td>
                            <td><%= leave.getStartDate() %> ~ <%= leave.getEndDate() %></td>
                            <td><b><%= leave.getUseDays() %>일</b></td>
                            
                            <%-- 🛠️ [교정 3] 휴가 사유 칸이 좁아 터지지 않게 말줄임 속성을 풀고 여백을 최적화 --%>
                            <td style="text-align: left; padding: 14px 20px; white-space: normal; word-break: break-all;">
                                <a href="javascript:void(0);" 
                                   onclick="goToDetail('leave', '<%= leave.getLeaveNo() %>')"
                                   class="title-link" 
                                   style="color: #6366f1; font-weight: 600; text-decoration: none; cursor: pointer; display: block; width: 100%;"
                                   onmouseover="this.style.textDecoration='underline'; this.style.color='#0284c7';"
                                   onmouseout="this.style.textDecoration='none'; this.style.color='#6366f1';">
                                     <%= leave.getReason() != null ? leave.getReason() : "사유 없음" %>
                                </a>
                            </td>
                            
                            <td><span class="status-badge <%= statusClass %>"><%= leave.getStatus() %></span></td>
                        </tr>
                    <% } } %>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- 탭 2: 비품 대여 신청 기안 콘텐츠 --%>
        <div id="tab-equipment" class="tab-content" style="display: none; width: 100%;">
            <%-- 🛠️ [교정 4] 비품 탭 테이블 래퍼 카드 역시 동일하게 가로 폭 100% 수평 동기화 --%>
            <div class="table-wrapper" style="width: 100% !important; max-width: 100% !important; box-sizing: border-box; background: #ffffff; border-radius: 8px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
                <table class="eq-table" style="width: 100%; table-layout: fixed; border-collapse: collapse;">
                    <thead>
                        <tr>
                            <th style="width: 12%;">기안 번호</th>
                            <th style="width: 34%;">기안 제목</th>
                            <th style="width: 15%;">기안자</th> 
                            <th style="width: 12%;">대여 수량</th>
                            <th style="width: 15%;">대여 기간</th>
                            <th style="width: 12%;">결재 상태</th>
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
                            boolean isAdmin = "Y".equals(loginEmp.getManager());
                            
                            for (RentalHistoryDTO eq : eqList) { 
                                int targetLevel = eq.getEmpLevel(); 
                                boolean isRetiredCreator = (targetLevel == 0);
                                boolean isMyDoc = (loginEmp.getEmpNo() == eq.getEmpNo());
                                String currentStatus = eq.getStatus();
                                
                                if (isRetiredCreator && "승인대기".equals(currentStatus)) {
                                    currentStatus = "반려됨";
                                }
                                
                                boolean isPermitted = false;
                                if (isRetiredCreator) {
                                    if (isAdmin) isPermitted = true;
                                } else if ("반려됨".equals(currentStatus)) {
                                    if (isMyDoc || isAdmin) isPermitted = true;
                                } else {
                                    if (isAdmin || isMyDoc || myLevel >= targetLevel) isPermitted = true;
                                }
                                
                                if (!isPermitted) continue;
                                
                                hasVisibleData = true; 
                                String statusClass = "status-blue";
                                String displayStatusText = currentStatus; 
                                
                                if ("반려됨".equals(currentStatus)) { 
                                    statusClass = "status-red";
                                } else if ("반납완료".equals(currentStatus) || "이용 종료".equals(currentStatus)) { 
                                    statusClass = "status-gray";
                                }
                                
                                boolean isMyApprovalTurn = "승인대기".equals(currentStatus) && (eq.getApprovalStep() == loginEmp.getEmpLevel());
                                String displayName = eq.getEmpName() != null ? eq.getEmpName() : "미상";
                    %>
                        <tr data-doc-id="rent_<%= eq.getRentalNo() %>">
                            <td><%= eq.getRentalNo() %></td>
                            
                            <%-- 기안 제목 줄임 및 잘림 방지 스타일 보정 --%>
                            <td class="td-title" style="text-align: left; padding: 14px 20px; white-space: normal; word-break: break-all;">
                                <a href="rentalDetail.do?rentalNo=<%= eq.getRentalNo() %>" 
                                   class="title-link" 
                                   style="color: #6366f1; font-weight: 600; text-decoration: none; cursor: pointer; display: block; width: 100%;"
                                   onmouseover="this.style.textDecoration='underline'; this.style.color='#0284c7';"
                                   onmouseout="this.style.textDecoration='none'; this.style.color='#6366f1';">
                                     <%= eq.getTitle() != null ? eq.getTitle() : "제목 없음" %>
                                </a>
                            </td>
                            
                            <td>
                                <% if (isRetiredCreator) { %>
                                    <b style="color: #64748b;"><%= displayName %></b>
                                    <span style="font-size: 11px; color: #94a3b8; font-weight: 500; margin-left: 2px;">(퇴사자)</span>
                                <% } else { %>
                                    <b><%= displayName %></b>
                                    <span style="font-size: 11px; color: #64748b; font-weight: 500; margin-left: 2px;">
                                        (Lv.<%= targetLevel %>)
                                    </span>
                                <% } %>
                            </td>
                            
                            <td class="td-qty">
                                <span class="qty-wrap"><b><%= eq.getReqCount() %></b>&nbsp;EA</span>
                             </td>
                            
                            <td><%= eq.getRentalDate() %> ~ <%= eq.getReturnDate() %></td>
                            
                            <%-- 결재 배지 열 --%>
                            <td>
                                <span class="status-badge <%= statusClass %>" style="display: inline-block;"><%= displayStatusText %></span>
                                <% if (isMyApprovalTurn) { %>
                                    <div class="approval-blink">
                                        결재 바랍니다
                                    </div>
                                <% } %>
                            </td>
                        </tr>
                    <% 
                            } // for(eqList) 루프 닫기
                            
                            if (!hasVisibleData) {
                    %>
                        <tr><td colspan="6" class="empty-data">조회 가능한 기안 내역이 없습니다.</td></tr>
                    <% 
                            }
                        } // else 블록 닫기
                    %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
        
    <style>
    /* 블링크 애니메이션 */
    .approval-blink {
        font-size: 11px !important;
        color: #ef4444 !important;
        font-weight: 800 !important;
        margin-top: 5px !important;
        animation: alert-flash 0.8s infinite alternate ease-in-out;
    }
    @keyframes alert-flash {
        from { opacity: 0.3; transform: scale(0.98); }
        to { opacity: 1; transform: scale(1); }
    }
    /* 테이블 내부 텍스트 링크 가독성 증대 기본 스타일 세팅 */
    .table-wrapper table tbody tr td a.title-link {
        display: block !important;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    </style>

    <script>
    /* [기존 필터링/스크롤/탭 스크립트 로직 완벽히 유지됨 - 수정 없음] */
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