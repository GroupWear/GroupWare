<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴가 신청서 상세</title>
    <%-- CSS 파일 경로가 정확한지 확인하세요 (css/leaveDetail.css) --%>
    <link rel="stylesheet" href="css/leaveDetail.css"> 
</head>
<body>
    <div class="detail-container">
        <div class="header-area">
            <h2>🏖️ 휴가 신청서 상세</h2>
            <span class="status-badge status-blue">${leave.status}</span>
        </div>
        
        
	<div class="approval-table">
    <c:forEach begin="1" end="5" var="i">
        <c:if test="${i >= leave.empLevel}">
            <%-- 변수명을 동적으로 만들기 위해 c:set 사용 --%>
            <c:set var="signName" value="sign${i}" />
            <c:set var="signDate" value="sign${i}Date" />
            
            <div class="stamp-box">
                <div class="stamp-step">${i == 5 ? '최종승인' : i += '단계'}</div>
                <div class="stamp-name">${leave[signName] != null ? leave[signName] : '-'}</div>
                <div class="stamp-date">${leave[signDate] != null ? leave[signDate] : '-'}</div>
            </div>
        </c:if>
    </c:forEach>
</div>

        <table class="info-table">
            <tr>
                <th>문서 번호</th><td>${leave.leaveNo}</td>
                <th>기안자 정보</th><td>${leave.empName} (Lv.${leave.empLevel})</td>
            </tr>
            <tr>
                <th>휴가 기간</th><td>${leave.startDate} ~ ${leave.endDate}</td>
                <th>사용 일수</th><td>${leave.useDays} 일</td>
            </tr>
            <tr>
                <th>휴가 사유</th>
                <td colspan="3" class="reason-cell">${leave.reason}</td>
            </tr>
        </table>
<div style="background: #fff3cd; border: 1px solid #ffeeba; padding: 15px; margin-bottom: 20px; text-align: center;">
    <strong>[디버깅 정보]</strong><br>
    현재 문서 상태: ${leave.status} (승인대기여야 함)<br>
    현재 결재 단계(approvalStep): <strong>${leave.approvalStep}</strong><br>
    로그인 사용자 레벨(empLevel): <strong>${loginEmp.empLevel}</strong><br>
    결과: <span style="color: ${loginEmp.empLevel == leave.approvalStep ? 'green' : 'red'}">
          ${loginEmp.empLevel == leave.approvalStep ? '레벨 일치 (버튼 출력 가능)' : '레벨 불일치 (버튼 출력 불가)'}
          </span>
</div>


        <div class="btn-group">
            <button type="button" class="btn-list" onclick="location.href='documentList.do?tab=leave'">목록으로 돌아가기</button>
            
            <%-- 승인 대기 중이고, 내 레벨이 현재 결재 단계와 같으면 버튼 노출 --%>
           <c:if test="${leave.status == '승인대기' && loginEmp.empLevel == leave.approvalStep}">
    
                <form action="leaveApproveProcess.do" method="post" style="display:inline;">
                    <input type="hidden" name="leaveNo" value="${leave.leaveNo}">
                    <input type="hidden" name="step" value="${leave.approvalStep}">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit" class="btn-approve">결재 승인</button>
                </form>
                
                <form action="leaveApproveProcess.do" method="post" style="display:inline;">
                    <input type="hidden" name="leaveNo" value="${leave.leaveNo}">
                    <input type="hidden" name="step" value="${leave.approvalStep}">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit" class="btn-reject">결재 반려</button>
                </form>
            </c:if>
        </div>
    </div>
</body>
</html>