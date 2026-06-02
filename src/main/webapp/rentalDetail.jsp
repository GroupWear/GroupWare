<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="com.groupware.dto.RentalHistoryDTO"%>
<%
    // 1. 세션 로그인 상태 체크
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. 컨트롤러에서 넘겨받은 상세 기안 데이터 수령
    RentalHistoryDTO detail = (RentalHistoryDTO) request.getAttribute("detail");
    if (detail == null) {
%>
    <script>
        alert('존재하지 않거나 삭제된 기안 문서입니다.');
        location.href = 'documentList.do';
    </script>
<%
        return;
    }

    // 📌 [비즈니스 로직 주입]: 기안자가 퇴사자(Lv.0)인데 아직 '승인대기' 상태라면 화면단에서 '반려됨'으로 강제 전환
    boolean isRetiredCreator = (detail.getEmpLevel() == 0);
    String currentStatus = detail.getStatus();
    
    if (isRetiredCreator && "승인대기".equals(currentStatus)) {
        currentStatus = "반려됨"; // 배지 색상 및 아래 버튼 권한 차단용 상태 스위칭
    }

    // 3. 실시간 결재 승인/반려 버튼 노출 권한 검증 (전환된 currentStatus 기준으로 가드 작동)
    int currentStep = detail.getApprovalStep();
    boolean isApprover = "Y".equals(loginEmp.getManager()) && (loginEmp.getManagerLevel() == currentStep);
    boolean isPending = "승인대기".equals(currentStatus); // 이제 퇴사자 기안은 false가 되어 버튼이 안 나옵니다.
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>전자결재 - 비품 대여 신청 기안 상세</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.6">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/rentalDetail.css">
</head>
<body>

    <div class="detail-container">
        
        <div class="detail-header-row">
            <h2 class="detail-title"><%= detail.getTitle() != null ? detail.getTitle() : "제목 없음" %></h2>
            <div>
                <% 
                    String badgeClass = "status-wait";
                    String displayStatusText = currentStatus; // 출력용 텍스트 변수 분리
                    
                    if ("대여중".equals(currentStatus)) {
                        badgeClass = "status-active";
                    } else if ("반려됨".equals(currentStatus)) {
                        badgeClass = "status-reject";
                        // 💡 퇴사자 기안이 반려 상태라면 문구를 커스텀 처리합니다.
                        if (isRetiredCreator) {
                            displayStatusText = "반려됨";
                        }
                    } else if ("반납완료".equals(currentStatus) || "미반납".equals(currentStatus)) {
                        badgeClass = "status-finish";
                    }
                %>
                <span class="status-badge <%= badgeClass %>"><%= displayStatusText %></span>
            </div>
        </div>

         <div class="approval-container">
            <% 
                int creatorLevel = detail.getEmpLevel(); 
                int currentLoginLevel = loginEmp.getEmpLevel();
                int docProgressStep = detail.getApprovalStep();
        
                for (int i = 1; i <= 5; i++) { 
                    String signName = null;
                    java.sql.Date signDate = null;
                    
                    if (i == 1) { signName = detail.getSign1(); signDate = detail.getSign1Date(); }
                    else if (i == 2) { signName = detail.getSign2(); signDate = detail.getSign2Date(); }
                    else if (i == 3) { signName = detail.getSign3(); signDate = detail.getSign3Date(); }
                    else if (i == 4) { signName = detail.getSign4(); signDate = detail.getSign4Date(); }
                    else if (i == 5) { signName = detail.getSign5(); signDate = detail.getSign5Date(); }
        
                    if (i < creatorLevel) {
                        continue; 
                    }
        
                    String stepTitle = (i == creatorLevel) ? "담 당" : i + "차 결재란";
                    
                    // 💡 문서 상태가 완벽하게 '승인대기'이고, 내 결재 차례일 때만 하이라이트 클래스(active-turn)를 부여합니다.
                    boolean isMyTurn = ("승인대기".equals(currentStatus) && i == docProgressStep && i == currentLoginLevel);
                    String activeClass = isMyTurn ? "active-turn" : "";
        
                    boolean isRejectedStamp = (signName != null && signName.contains("반려"));
                    
                    String displaySignName = "";
                    if (signName != null) {
                        if (isRejectedStamp) {
                            String pureName = signName.replaceAll("\\(반려\\)", "").trim();
                            displaySignName = pureName + " (반려)"; 
                        } else {
                            displaySignName = signName;
                        }
                    }
                    
                    // 💡 퇴사자가 기안자(creatorLevel == 0)이고 현재 기안자 칸(i == 1)이라면 서명 도장 스타일을 입히지 않습니다.
                    String stampClass = "";
                    if (signName != null && !isRejectedStamp && !(creatorLevel == 0 && i == 1)) {
                        stampClass = "signed";
                    }
            %>
                <div class="stamp-box <%= activeClass %>">
                <div class="stamp-step"><%= stepTitle %></div>
                
                <%-- 💡 [결재란 이름 출력부]: 퇴사자일 경우 도장 없이 텍스트만 깔끔히 노출 --%>
                <div class="stamp-name <%= stampClass %>" style="<%= isRejectedStamp ? "color: #ef4444;" : "" %>">
                    <%
                        if (creatorLevel == 0 && i == 1) {
                    %>
                        <span style="font-weight: 600; color: #64748b;">퇴사자</span>
                    <%
                        } else {
                    %>
                        <%= displaySignName %>
                    <%
                        }
                    %>
                </div>
                
                <%-- 💡 [결재란 날짜 출력부 수정]: 퇴사자 기안의 첫 번째 칸(i == 1)일 때는 날짜 대신 빈칸('-') 표시 --%>
                <div class="stamp-date">
                    <%
                        if (creatorLevel == 0 && i == 1) {
                    %>
                        -
                    <%
                        } else {
                    %>
                        <%= signDate != null ? signDate.toString() : "-" %>
                    <%
                        }
                    %>
                </div>
            </div>
            <% 
                } 
            %>
        </div>

        <table class="info-table">
            <colgroup>
                <col style="width: 20%;">
                <col style="width: 30%;">
                <col style="width: 20%;">
                <col style="width: 30%;">
            </colgroup>
            
            <tr>
                <th>문서 번호</th>
                <td><%= detail.getRentalNo() %></td>
                <th>기안자 정보</th>
                <td>
                    <%
                        // 💡 퇴사자(Lv.0) 여부를 판별하여 출력 텍스트를 명확하게 분기합니다.
                        if (detail.getEmpLevel() == 0) { 
                    %>
                        <b style="color: #94a3b8;">퇴사자</b>
                    <% 
                        } else { 
                    %>
                        <b><%= detail.getEmpName() != null ? detail.getEmpName() : "미상" %></b> 
                        <span style="font-size: 13px; color: #64748b; font-weight: 500;">(직급 레벨: <%= detail.getEmpLevel() %>)</span>
                    <% 
                        } 
                    %>
                </td>
            </tr>
            <tr>
                <th>신청 비품명</th>
                <td class="emphasize-text"><%= detail.getEqName() != null ? detail.getEqName() : "미지정 비품" %></td>
                <th>신청 수량</th>
                <td><b><%= detail.getReqCount() %></b> EA</td>
            </tr>
            <tr>
                <th>대여 시작일</th>
                <td><%= detail.getRentalDate() %></td>
                <th>반납 예정일</th>
                <td><%= detail.getReturnDate() %></td>
            </tr>
            <tr>
                <th>현재 결재 단계</th>
                <td>
                    <% 
                        int appStep = detail.getApprovalStep();
                        
                        if ("반려됨".equals(currentStatus)) { 
                            if (isRetiredCreator) {
                    %>
                                <b style="color: #ef4444;">퇴사로 인해 반려됨</b>
                    <% 
                            } else { 
                    %>
                                <b style="color: #ef4444;"><%= appStep %>차 결재에서 반려됨</b>
                    <% 
                            }
                        } else if (appStep > 5 || "반납완료".equals(currentStatus) || "대여중".equals(currentStatus)) { 
                    %>
                            <b style="color: #16a34a;">최종 승인 완료</b>
                    <% 
                        } else { 
                    %>
                            <b class="emphasize-text"><%= appStep %>차</b> 결재 대기 중
                    <% 
                        } 
                    %>
                </td>
                <th>비품 마스터 재고</th>
                <td>남은 수량: <%= detail.getRemainCount() %> EA / 총 보유: <%= detail.getTotalCount() %> EA</td>
            </tr>
             <tr>
                <th>대여 사유</th>
                <td colspan="3" style="text-align: left; padding: 15px; line-height: 1.6; background: #fafafa; white-space: pre-wrap;"><%= detail.getContent() != null ? detail.getContent() : "작성된 사유가 없습니다." %></td>
            </tr>
        </table>

        <div class="btn-group">
            <a href="documentList.do?tab=equipment" class="btn btn-back">목록으로 이동</a>
            
            <% if (isPending && currentStep == loginEmp.getEmpLevel()) { %>
                
                <form action="processApproval.do" method="post" style="display: inline;" onsubmit="return confirm('이 기안서에 최종 서명(승인) 하시겠습니까?');">
                    <input type="hidden" name="rentalNo" value="<%= detail.getRentalNo() %>">
                    <input type="hidden" name="eqNo" value="<%= detail.getEqNo() %>">
                    <input type="hidden" name="step" value="<%= currentStep %>">
                    <input type="hidden" name="isApprove" value="true">
                    <button type="submit" class="btn btn-approve">결재 승인</button>
                </form>
                
                <form action="processApproval.do" method="post" style="display: inline;" onsubmit="return confirm('이 기안을 반려하시겠습니까? 선점된 비품 재고가 즉시 환원됩니다.');">
                    <input type="hidden" name="rentalNo" value="<%= detail.getRentalNo() %>">
                    <input type="hidden" name="eqNo" value="<%= detail.getEqNo() %>">
                    <input type="hidden" name="step" value="<%= currentStep %>">
                    <input type="hidden" name="isApprove" value="false">
                    <button type="submit" class="btn btn-reject">결재 반려</button>
                </form>
                
            <% } %>
        </div>
        
    </div>

</body>
</html>