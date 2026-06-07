<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EquipmentDTO" %>
<%-- 1. 상단에 오늘 날짜를 포맷팅하는 자바 코드를 한 줄 심어줍니다 (없다면 추가) --%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<%
    // 서버 기준 오늘 날짜를 yyyy-MM-dd (예: 2026-06-01) 형태로 강제 추출
    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<%-- 📌 1. useBean 선언부를 JSP 최상단으로 끌어올려 EL 태그와 스크립틀릿이 정상 동작하도록 고쳤습니다 --%>
<jsp:useBean id="equipment" type="com.groupware.dto.EquipmentDTO" scope="request"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>사내 시스템 - 비품 대여 신청서</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/rentForm.css">
</head>
<body>

<div class="form-container">
    <h2>📋 비품 대여 신청서</h2>

    <!-- 컨트롤러에서 에러 메시지가 넘어온 경우 예외 처리 -->
    <% if (request.getAttribute("errorMsg") != null) { %>
        <div class="error-box">
            <%= request.getAttribute("errorMsg") %>
        </div>
    <% } %>

    <!-- 대여 정보를 입력받아 처리할 서블릿(/rentInsert.do)으로 전송 -->
    <form action="rentInsert.do" method="post" onsubmit="return validateForm()">
        
        <!-- 대여 대상 비품 번호를 hidden 필드로 서버에 전송 -->
        <input type="hidden" id="eqNo" name="eqNo" value="${equipment.eqNo}" />
        <!-- 검증을 위한 잔여 수량 스크립트용 hidden 필드 -->
        <input type="hidden" id="maxRemain" value="${equipment.remainCount}" />

        <!-- 비품 요약 정보 표 -->
        <table class="info-table">
            <tr>
                <th>비품 번호</th>
                <td>${equipment.eqNo}</td>
            </tr>
            <tr>
                <th>비품 명칭</th>
                <td><strong>${equipment.eqName}</strong></td>
            </tr>
            <tr>
                <th>대여 가능 수량</th>
                <td>
                    <span class="${equipment.remainCount == 0 ? 'text-danger' : 'remain-count'}">
                        ${equipment.remainCount}
                    </span>
                    <span class="total-count">
                        / ${equipment.totalCount} EA
                    </span>
    
                    <% if (equipment.getRemainCount() == 0) { %>
                        <span class="text-danger">(현재 대여 가능한 재고가 없습니다)</span>
                    <% } %>
                </td>
            </tr>
        </table>
         <!-- 💡 [신규 추가]: 대여 신청 수량 입력 상자 (1200px 철벽 락 연동) -->
        <div class="form-group">
            <label for="rentCount">신청 수량 (EA)</label>
            <!-- min="1"로 음수 입력을 막고, max="${equipment.remainCount}"로 잔여 재고를 초과하지 못하게 잠금 처리합니다. -->
            <input type="number" id="rentCount" name="rentCount" class="form-control" 
                   value="1" min="1" max="${equipment.remainCount}" required
                   ${equipment.remainCount == 0 ? 'disabled' : ''}>
        </div>

        <%-- 2. 기존 대여 시작일 및 반납 예정일 태그를 아래 코드로 교체합니다. --%>
		<div class="form-group">
		    <label for="rentalDate">대여 시작일</label>
		    <!-- 💡 [핵심 교정]: min 속성에 자바 변수 <%=todayStr%>를 직접 주입하여 HTML 로딩 전 과거 날짜를 물리적으로 봉인합니다. -->
		    <input type="date" id="rentalDate" name="rentalDate" class="form-control" 
		           min="<%= todayStr %>" 
		           required ${equipment.remainCount == 0 ? 'disabled' : ''}>
		</div>
		
		<div class="form-group">
		    <label for="returnDate">반납 예정일</label>
		    <!-- 💡 반납 예정일 역시 오늘 이전은 절대로 고를 수 없도록 기본 최하단 베이스라인을 설정합니다. -->
		    <input type="date" id="returnDate" name="returnDate" class="form-control" 
		           min="<%= todayStr %>" 
		           required ${equipment.remainCount == 0 ? 'disabled' : ''}>
		</div>

        <div class="form-group">
            <label for="rentPurpose">대여 사유</label>
            <%-- 📌 3. name 속성을 컨트롤러 규격에 맞춰 "content"로 유지했습니다 --%>
            <textarea id="rentPurpose" name="content" class="form-control" rows="4" 
                      placeholder="대여 목적 및 사유를 상세히 입력해 주세요." required
                      ${equipment.remainCount == 0 ? 'disabled' : ''}></textarea>
        </div>

        <!-- 버튼 영역 (CSS .btn-area 구조 반영) -->
        <div class="btn-area">
            <button type="submit" class="btn btn-submit" ${equipment.remainCount == 0 ? 'disabled' : ''}>
                ${equipment.remainCount == 0 ? '대여 불가 (재고 소진)' : '신청서 제출'}
            </button>
            <button type="button" class="btn btn-cancel" onclick="location.href='equipmentList.do'">취소</button>
        </div>
        
    </form>
</div>

<!-- =========================================================================
     🚨 [철벽 통합 검증 스크립트]: 과거 날짜 우회 전송 전면 차단 빌트인
     ========================================================================= -->
<script>
function validateForm() {
    const rentalDateInput = document.getElementById("rentalDate");
    const returnDateInput = document.getElementById("returnDate");
    
    const sDate = rentalDateInput.value;
    const eDate = returnDateInput.value;

    // 💡 서버 가드 기준 오늘 날짜 강제 바인딩 (2026-06-01)
    const todayStr = "<%= todayStr %>"; 

    // 1. 날짜 입력 누락 방지 가드
    if (!sDate || !eDate) {
        alert("대여 시작일과 반납 예정일을 모두 선택해 주세요.");
        if (!sDate) rentalDateInput.focus();
        else returnDateInput.focus();
        return false;
    }

    // 🚨 [과거 날짜 강제 우회 제출 박멸]: 수동 타이핑 꼼수 전송 시 전면 차단
    if (sDate < todayStr) {
        alert("대여 시작일은 오늘 이전 날짜를 선택할 수 없습니다.");
        rentalDateInput.focus();
        return false;
    }

    // 2. 대여 종료일이 시작일보다 빠른 역전 오류 검증
    if (sDate > eDate) {
        alert("반납 예정일은 대여 시작일보다 빠를 수 없습니다.");
        returnDateInput.focus();
        return false;
    }

    return confirm("입력하신 내용으로 비품 대여를 신청하시겠습니까?");
}

/* 실시간 대여일 기준 반납 달력 최소 선택지 연동 락 장치 */
document.addEventListener("DOMContentLoaded", function() {
    const rentalInput = document.getElementById("rentalDate");
    const returnInput = document.getElementById("returnDate");
    
    if (rentalInput && returnInput) {
        rentalInput.addEventListener("change", function() {
            if (this.value) {
                returnInput.min = this.value; 
                if (returnInput.value && returnInput.value < this.value) {
                    returnInput.value = this.value;
                }
            }
        });
    }
});
</script>

</body>
</html>