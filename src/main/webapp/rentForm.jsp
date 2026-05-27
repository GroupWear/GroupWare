<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        
        <!-- 1. 대여 대상 비품 번호를 hidden 필드로 서버에 전송 -->
        <input type="hidden" id="eqNo" name="eqNo" value="${equipment.eqNo}" />
        <!-- 2. 검증을 위한 잔여 수량 스크립트용 hidden 필드 -->
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
                    <%-- CSS 스타일 규칙(remain-count, text-danger)에 맞게 EL 삼항연산자 적용 --%>
                    <span class="${equipment.remainCount == 0 ? 'text-danger' : 'remain-count'}">
                        ${equipment.remainCount}
                    </span>
                    <span class="total-count">
                        / ${equipment.totalCount} EA
                    </span>
    
                    <%-- 롬복 객체 속성을 읽어 자바 조건문으로 제어합니다 --%>
                    <jsp:useBean id="equipment" type="com.groupware.dto.EquipmentDTO" scope="request"/>
                    <% if (equipment.getRemainCount() == 0) { %>
                        <span class="text-danger">(현재 대여 가능한 재고가 없습니다)</span>
                    <% } %>
                </td>
            </tr>
        </table>

        <!-- 신청자 정보 및 대여 세부 입력 폼 -->
        <div class="form-group">
            <label for="rentCount">신청 수량 (EA)</label>
            <!-- HTML5 검증: min을 1로, max를 현재 remainCount로 지정하여 초과 입력 방지 -->
            <input type="number" id="rentCount" name="rentCount" class="form-control" 
                   min="1" max="${equipment.remainCount}" placeholder="신청할 수량을 입력하세요." required 
                   ${equipment.remainCount == 0 ? 'disabled' : ''}>
        </div>

        <div class="form-group">
            <label for="rentPurpose">대여 사유</label>
            <textarea id="rentPurpose" name="rentPurpose" class="form-control" rows="4" 
                      placeholder="대여 목적 및 사유를 상세히 입력해 주세요." required
                      ${equipment.remainCount == 0 ? 'disabled' : ''}></textarea>
        </div>

        <!-- 버튼 영역 (CSS .btn-area 구조 반영) -->
        <div class="btn-area">
            <%-- 재고가 0개이면 신청하기 버튼을 비활성화(disabled) 합니다. --%>
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
    
    const rentCount = parseInt(rentCountInput.value, 10);
    const maxRemain = parseInt(maxRemainInput.value, 10);

    // 1. 숫자가 맞는지 검증
    if (isNaN(rentCount) || rentCount <= 0) {
        alert("올바른 신청 수량을 입력해 주세요.");
        rentCountInput.focus();
        return false;
    }

    // 2. 잔여 재고보다 많이 신청하는지 최종 검증
    if (rentCount > maxRemain) {
        alert("현재 대여 가능한 수량(" + maxRemain + "개)을 초과하여 신청할 수 없습니다.");
        rentCountInput.focus();
        return false;
    }

    // 3. 최종 제출 확인 대화상자
    return confirm("입력하신 내용으로 비품 대여를 신청하시겠습니까?");
}
</script>

</body>
</html>