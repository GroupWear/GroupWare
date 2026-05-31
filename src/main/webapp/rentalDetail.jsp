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

    // 3. 실시간 결재 승인/반려 버튼 노출 권한 검증
    int currentStep = detail.getApprovalStep();
    // 현재 로그인한 사용자가 관리자('Y')이고, 본인의 관리자 레벨(ManagerLevel)이 기안서의 현재 결재 단계와 일치할 때만 활성화
    boolean isApprover = "Y".equals(loginEmp.getManager()) && (loginEmp.getManagerLevel() == currentStep);
    boolean isPending = "승인대기".equals(detail.getStatus());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>전자결재 - 비품 대여 신청 기안 상세</title>
    <!-- 공통 메인 스타일 연결 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=1.6">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/rentalDetail.css">
</head>
<body>

    <div class="detail-container">
        
        <!-- 상단 헤더 영역 -->
        <div class="detail-header-row">
            <h2 class="detail-title"><%= detail.getTitle() != null ? detail.getTitle() : "제목 없음" %></h2>
            <div>
                <% 
                    String badgeClass = "status-wait";
                    if ("대여중".equals(detail.getStatus())) badgeClass = "status-active";
                    else if ("반려됨".equals(detail.getStatus())) badgeClass = "status-reject";
                    else if ("반납완료".equals(detail.getStatus()) || "미반납".equals(detail.getStatus())) badgeClass = "status-finish";
                %>
                <span class="status-badge <%= badgeClass %>"><%= detail.getStatus() %></span>
            </div>
        </div>

          <!-- 📌 핵심 구현 1: 기안자 레벨 기준 고정 결재선 (반려 레이아웃 및 띄어쓰기 보정형) -->
        <div class="approval-container">
            <% 
                // 이 '문서를 신청한 기안자'의 직급 레벨을 기준점으로 삼습니다.
                int creatorLevel = detail.getEmpLevel(); 
                
                // 현재 로그인한 관리자의 레벨 (실시간 결재 대기 강조용)
                int currentLoginLevel = loginEmp.getEmpLevel();
                // 문서의 현재 결재 승인 단계 위치 (1~5)
                int docProgressStep = detail.getApprovalStep();

                for (int i = 1; i <= 5; i++) { 
                    String signName = null;
                    java.sql.Date signDate = null;
                    
                    if (i == 1) { signName = detail.getSign1(); signDate = detail.getSign1Date(); }
                    else if (i == 2) { signName = detail.getSign2(); signDate = detail.getSign2Date(); }
                    else if (i == 3) { signName = detail.getSign3(); signDate = detail.getSign3Date(); }
                    else if (i == 4) { signName = detail.getSign4(); signDate = detail.getSign4Date(); }
                    else if (i == 5) { signName = detail.getSign5(); signDate = detail.getSign5Date(); }

                    // 💡 [규칙 1]: 기안자(신청자)의 직급보다 낮은 하급자 결재란은 화면에서 제외합니다.
                    if (i < creatorLevel) {
                        continue; 
                    }

                    // 💡 [규칙 2]: 기안자 본인의 결재 시작 단계는 '담당'으로 고정, 상급자 단계는 'X차 결재란' 표시
                    String stepTitle = (i == creatorLevel) ? "담 당" : i + "차 결재란";
                    
                    // 💡 [UI 디테일 추가]: 실시간 결재 승인 도장 차례 강조 제어
                    boolean isMyTurn = ("승인대기".equals(detail.getStatus()) && i == docProgressStep && i == currentLoginLevel);
                    
                    String boxStyle = "";
                    if (isMyTurn) {
                        boxStyle = "border: 2px solid #6366f1; transform: scale(1.03); box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);";
                    }
                    
                    String stepStyle = (i == creatorLevel) ? "background: #f1f5f9; color: #334155; font-weight:700;" : "";
                    if (isMyTurn) {
                        stepStyle = "background: #6366f1; color: #ffffff; font-weight:700;";
                    }

                    // 📌 [신규 보정 1]: 서명 텍스트에 '반려'가 포함되어 있는지 실시간 체크
                    boolean isRejectedStamp = (signName != null && signName.contains("반려"));
                    
                    // 📌 [신규 보정 2]: '홍글로(반려)' 구조를 파싱하여 '홍글로 (반려)' 형태로 공백 1칸 강제 주입
                    String displaySignName = "";
                    if (signName != null) {
                        if (isRejectedStamp) {
                            String pureName = signName.replaceAll("\\(반려\\)", "").trim();
                            displaySignName = pureName + " (반려)"; // 이름과 괄호 사이 공백 1칸 적용
                        } else {
                            displaySignName = signName;
                        }
                    }
                    
                    // 📌 [신규 보정 3]: '반려' 상태가 아닐 때만 signed 클래스를 붙여 빨간색 (인) 도장을 활성화합니다.
                    String stampClass = "";
                    if (signName != null && !isRejectedStamp) {
                        stampClass = "signed";
                    }
            %>
                <div class="stamp-box" style="<%= boxStyle %>">
                    <div class="stamp-step" style="<%= stepStyle %>"><%= stepTitle %></div>
                    <!-- stampClass 분기를 통해 반려 문서의 (인) 표시를 완벽히 격리 억제합니다. -->
                    <div class="stamp-name <%= stampClass %>" style="<%= isRejectedStamp ? "color: #ef4444;" : "" %>">
                        <%= displaySignName %>
                    </div>
                    <div class="stamp-date">
                        <%= signDate != null ? signDate.toString() : "-" %>
                    </div>
                </div>
            <% 
                } 
            %>
        </div>

        <!-- 📌 핵심 구현 2: 신청서 상세 공문 내용 테이블 -->
        <table class="info-table">
            <tr>
                <th>문서 번호</th>
                <td><%= detail.getRentalNo() %></td>
                <th>기안자 정보</th>
                <td><b><%= detail.getEmpName() != null ? detail.getEmpName() : "미상" %></b> (직급 레벨: <%= detail.getEmpLevel() %>)</td>
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
                        String docStatus = detail.getStatus();
                        
                        if ("반려됨".equals(docStatus)) { 
                    %>
                        <!-- 💡 [신규 추가]: 문서가 반려 상태일 때 어느 단계에서 최종 거절되었는지 직관적으로 표시 -->
                        <b style="color: #ef4444;"><%= appStep %>차 결재에서 반려됨</b>
                    <% } else if (appStep > 5 || "반납완료".equals(docStatus) || "대여중".equals(docStatus)) { %>
                        <b style="color: #16a34a;">최종 승인 완료</b>
                    <% } else { %>
                        <b class="emphasize-text"><%= appStep %>차</b> 결재 대기 중
                    <% } %>
                </td>
                <th>비품 마스터 재고</th>
                <td>남은 수량: <%= detail.getRemainCount() %> EA / 총 보유: <%= detail.getTotalCount() %> EA</td>
            </tr>
             <!-- 📌 [신규 추가]: 대여 사유 출력 행 (2칸을 병합하여 넓게 표시) -->
	        <tr>
	            <th>대여 사유</th>
	            <td colspan="3" style="text-align: left; padding: 15px; line-height: 1.6; background: #fafafa; white-space: pre-wrap;"><%= detail.getContent() != null ? detail.getContent() : "작성된 사유가 없습니다." %></td>
	        </tr>
        </table>

        <!-- 📌 핵심 구현 3: 목록 이동 버튼과 실시간 결재 승인/반려 액션 버튼 한 줄 배치 -->
        <div class="btn-group">
            <!-- 언제나 노출되는 목록 복귀 버튼 (비품 탭 선택 파라미터 연동 유지) -->
            <a href="documentList.do?tab=equipment" class="btn btn-back">목록으로 이동</a>
            
            <%-- 💡 [권한 및 상태 검증]: 현재 문서가 '승인대기' 상태이고, 
                 로그인한 임직원의 등급 레벨과 문서의 결재 대기 단계가 완벽히 일치할 때만 승인/반려 버튼을 나란히 노출합니다. --%>
            <% if (isPending && currentStep == loginEmp.getEmpLevel()) { %>
                
                <!-- 결재 승인 Form (목록으로 이동 오른쪽에 가로 정렬 배치) -->
                <form action="processApproval.do" method="post" style="display: inline;" onsubmit="return confirm('이 기안서에 최종 서명(승인) 하시겠습니까?');">
                    <input type="hidden" name="rentalNo" value="<%= detail.getRentalNo() %>">
                    <input type="hidden" name="eqNo" value="<%= detail.getEqNo() %>">
                    <input type="hidden" name="step" value="<%= currentStep %>">
                    <input type="hidden" name="isApprove" value="true">
                    <button type="submit" class="btn btn-approve">결재 승인</button>
                </form>
                
                <!-- 결재 반려 Form (결재 승인 오른쪽에 가로 정렬 배치) -->
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