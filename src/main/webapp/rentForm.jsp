<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EquipmentDTO" %>
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

        <!-- 신청자 정보 및 대여 세부 입력 폼 -->
        <div class="form-group">
            <label for="rentCount">신청 수량 (EA)</label>
            <input type="number" id="rentCount" name="rentCount" class="form-control" 
                   min="1" max="${equipment.remainCount}" placeholder="신청할 수량을 입력하세요." required 
                   ${equipment.remainCount == 0 ? 'disabled' : ''}>
        </div>

        <%-- 📌 2. 컨트롤러(RentInsertController)에서 요구하는 필수 날짜 파라미터 2개를 새로 추가했습니다 --%>
        <div class="form-group">
            <label for="rentalDate">대여 시작일</label>
            <input type="date" id="rentalDate" name="rentalDate" class="form-control" required
                   ${equipment.remainCount == 0 ? 'disabled' : ''}>
        </div>

        <div class="form-group">
            <label for="returnDate">반납 예정일</label>
            <input type="date" id="returnDate" name="returnDate" class="form-control" required
                   ${equipment.remainCount == 0 ? 'disabled' : ''}>
        </div>

        <div class="form-group">
            <label for="rentPurpose">대여 사유</label>
            <%-- 📌 3. name 속성을 컨트롤러 규격에 맞춰 "rentPurpose"로 유지했습니다 --%>
            <textarea id="rentPurpose" name="rentPurpose" class="form-control" rows="4" 
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

<script>
/**
 * 서블릿으로 폼을 제출하기 전 최종 유효성 검사를 수행합니다.
 */
function validateForm() {
    const rentCountInput = document.getElementById("rentCount");
    const maxRemainInput = document.getElementById("maxRemain");
    const rentalDateInput = document.getElementById("rentalDate");
    const returnDateInput = document.getElementById("returnDate");
    
    const rentCount = parseInt(rentCountInput.value, 10);
    const maxRemain = parseInt(maxRemainInput.value, 10);
    const sDate = rentalDateInput.value;
    const eDate = returnDateInput.value;

    // 1. 신청 수량 숫자 및 음수값 유효성 검증
    if (isNaN(rentCount) || rentCount <= 0) {
        alert("올바른 신청 수량을 입력해 주세요.");
        rentCountInput.focus();
        return false;
    }

    // 2. 잔여 재고 초과 신청 검증
    if (rentCount > maxRemain) {
        alert("현재 대여 가능한 수량(" + maxRemain + "개)을 초과하여 신청할 수 없습니다.");
        rentCountInput.focus();
        return false;
    }

    // 3. 날짜 입력 누락 방지 가드
    if (!sDate || !eDate) {
        alert("대여 시작일과 반납 예정일을 모두 선택해 주세요.");
        if (!sDate) rentalDateInput.focus();
        else returnDateInput.focus();
        return false;
    }

    // 4. 대여 종료일이 시작일보다 빠른 역전 오류 검증
    if (new Date(sDate) > new Date(eDate)) {
        alert("반납 예정일은 대여 시작일보다 빠를 수 없습니다.");
        returnDateInput.focus();
        return false;
    }

    // 5. 모든 검증 통과 시 최종 제출 컨펌창 팝업
    return confirm("입력하신 내용으로 비품 대여를 신청하시겠습니까?");
}
</script>

</body>
</html>