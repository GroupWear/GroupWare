<%@ page import="com.groupware.dao.EquipmentDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.groupware.dto.EquipmentDTO" %>
<%@ page import="com.groupware.dto.EmployeeDTO" %> 

<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    List<EquipmentDTO> eqList = (List<EquipmentDTO>) request.getAttribute("eqList");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사내 시스템 - 비품 대여 신청</title>
    <!-- 경로 뒤에 ?v=1.1 이나 임의의 숫자를 붙여서 저장하세요 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/equipmentList.css?v=1.1">
</head>
<body>
    
    <!-- 1. 상단 네비게이션 헤더 (최상단 고정 고유 레이어) -->
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
    
    <!-- 2. 페이지 대제목 영역 (고정 영역에서 완전히 제외하여 최상단 헤더 밑에 배치) -->
    <div class="table-container" style="margin-top: 30px; margin-bottom: 0;">
        <div class="headertitle">
            <h2 style="margin: 0; font-size: 22px; font-weight: 700; color: #1e293b;">비품 대여 신청</h2>
        </div>
    </div>
    
    <!-- 3. 실시간 고정형 검색 바 영역 (헤더 바로 하단인 top: 65px 지점에 단독 고정) -->
	<div class="search-bar-container">
	    <div class="search-form">
	        <select id="searchType" class="search-select">
	            <option value="all">전체 검색</option>
	            <option value="eqName">비품 명칭</option>
	            <option value="eqNo">비품 번호</option>
	        </select>
	        <input type="text" id="searchKeyword" class="search-input" placeholder="이동할 비품명 또는 번호 입력..." autocomplete="off" onkeyup="if(event.key === 'Enter') searchAndScroll()">
	        <button type="button" class="btn-search" onclick="searchAndScroll()">검색 및 이동</button>
	    </div>
	</div>

    <!-- 4. 메인 비품 목록 테이블 영역 (이하 기존 코드 유지) -->
    <div class="table-container" style="margin-top: 10px;">
        <table class="eq-table">
            <thead>
                <tr>
                    <th style="width: 15%; text-align: center;">비품 번호</th>
                    <th style="width: 45%; text-align: center;">비품 명칭</th>
                    <th style="width: 25%; text-align: center;">대여 가능 수량 (잔여/전체)</th>
                    <th style="width: 15%; text-align: center;">신청</th>
                </tr>
            </thead>
            <tbody>
                <% if (eqList != null && !eqList.isEmpty()) {
                    for (EquipmentDTO eq : eqList) {
                %>
                    <tr class="eq-row">
                        <!-- 비품 번호 출력 -->
                        <td class="eq-no" style="text-align: center;"><%= eq.getEqNo() %></td>
                        
                        <!-- 비품 명칭 출력 -->
                        <td class="eq-name" style="text-align: center;"><%= eq.getEqName() %></td>
                        
                        <!-- 잔여 수량 조건별 강조 출력 -->
                        <td style="text-align: center;" class="eq-count <%= eq.getRemainCount() == 0 ? "text-danger" : "" %>">
                            <b><%= eq.getRemainCount() %></b> / <%= eq.getTotalCount() %> EA
                        </td>
                        
                        <!-- 작동 상태 버튼 출력 -->
                        <td style="text-align: center;">
                            <% if (eq.getRemainCount() > 0) { %>
                                <button class="btn-rent" onclick="location.href='rentForm.do?eqNo=<%= eq.getEqNo() %>'">대여 신청</button>
                            <% } else { %>
                                <button class="btn-disabled" disabled>재고 소진</button>
                            <% } %>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr>
                        <td colspan="4" style="text-align: center; color: #64748b; padding: 50px; font-size: 15px;">등록된 비품 목록이 없습니다.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

</body>
</html>

<script>
// 전역 변수 설정: 원본 텍스트, 이전 검색어, 현재 포커스 인덱스 기록
const originalTexts = new Map();
let lastKeyword = "";
let currentMatchIndex = -1;

window.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 각 셀의 순수 원본 텍스트만 메모리에 기록
    document.querySelectorAll('.eq-no, .eq-name').forEach((td, index) => {
        td.setAttribute('data-search-id', index);
        originalTexts.set(index.toString(), td.textContent.trim());
    });
});

function searchAndScroll() {
    const searchType = document.getElementById("searchType").value;
    const keyword = document.getElementById("searchKeyword").value.trim();
    const lowerKeyword = keyword.toLowerCase();

    if (keyword === "") {
        alert("검색어를 입력해주세요.");
        return;
    }

    // [핵심 로직] 새로운 단어를 검색한 경우: 마킹을 새로 하고 인덱스를 초기화함
    if (lastKeyword !== lowerKeyword) {
        // 1. 기존 노란 하이라이트 마킹 전체 초기화
        document.querySelectorAll('.eq-no, .eq-name').forEach(td => {
            const id = td.getAttribute('data-search-id');
            if (originalTexts.has(id)) {
                td.innerHTML = originalTexts.get(id); 
            }
        });

        // 2. 루프를 돌며 일치하는 모든 항목에 하이라이트 마킹 표시
        const rows = document.querySelectorAll(".eq-row");
        rows.forEach(row => {
            const eqNoTd = row.querySelector(".eq-no");
            const eqNameTd = row.querySelector(".eq-name");

            if (searchType === "all" || searchType === "eqNo") {
                markCellIfMatch(eqNoTd, lowerKeyword);
            }
            if (searchType === "all" || searchType === "eqName") {
                markCellIfMatch(eqNameTd, lowerKeyword);
            }
        });

        // 상태 기록 갱신
        lastKeyword = lowerKeyword;
        currentMatchIndex = -1; 
    }

    // 3. 화면에 생성된 모든 마킹 요소 리스트를 수집
    const allMarks = document.querySelectorAll(".mark-highlight");

    if (allMarks.length > 0) {
        // 다음 인덱스로 이동 (마지막 항목에 도달했다면 다시 0번째 첫 항목으로 회귀)
        currentMatchIndex++;
        if (currentMatchIndex >= allMarks.length) {
            currentMatchIndex = 0; 
        }

        // 4. 선택된 순서의 다음(Next) 마킹 위치로 부드럽게 스크롤 이동
        allMarks[currentMatchIndex].scrollIntoView({
            behavior: "smooth",
            block: "center"
        });

        // 5. 사용자가 편하게 다음 검색을 이어나가도록 텍스트창 블록 지정 (기존 유지)
        document.getElementById("searchKeyword").select();
    } else {
        alert("일치하는 비품 항목을 찾을 수 없습니다.");
        document.getElementById("searchKeyword").focus();
        lastKeyword = "";
        currentMatchIndex = -1;
    }
}

// 텍스트 매칭 및 실제 태그 치환 함수 (숫자 보존 정규식 방식 적용)
function markCellIfMatch(td, lowerKeyword) {
    if (!td) return false;
    
    const originalText = td.textContent.trim();
    const lowerText = originalText.toLowerCase();
    const index = lowerText.indexOf(lowerKeyword);

    if (index >= 0) {
        const escapedKeyword = lowerKeyword.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
        const regex = new RegExp(escapedKeyword, "gi");
        td.innerHTML = originalText.replace(regex, `<span class="mark-highlight">$&</span>`);
        return true; 
    }
    return false; 
}
</script>