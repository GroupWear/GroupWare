<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.LeaveHistoryDTO" %>
<%@ page import="com.groupware.dto.EmployeeDTO" %>
<%@ page import="com.groupware.dao.LeaveDAO" %>
<%
    LeaveHistoryDTO leave = (LeaveHistoryDTO) request.getAttribute("leave");
    if (leave == null) {
%>
    <script> alert("해당 문서 정보를 찾을 수 없습니다."); history.back(); </script>
<%
        return;
    }

    int displayLevel = leave.getEmpLevel();	

    if (displayLevel == 0) {
        LeaveDAO dao = new LeaveDAO();
        displayLevel = dao.getEmpLevelByNo(leave.getEmpNo());
    }
    
    // 로그인 유저 정보 및 결재 권한 확인을 위한 변수
    Integer loginUserLevel = (Integer) request.getAttribute("loginUserLevel");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    boolean isPending = "승인대기".equals(leave.getStatus().trim());
    boolean isMyTurn = (loginUserLevel != null && loginUserLevel == leave.getApprovalStep());
    boolean isNotOwner = (loginEmp != null && loginEmp.getEmpNo() != leave.getEmpNo());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴가 신청 상세</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/leaveDetail.css">
</head>
<body>

<div class="detail-container">
    <div class="header-area">
        <h2>휴가 신청 기안</h2>
        <span class="status-badge <%= "반려됨".equals(leave.getStatus()) ? "status-red" : "status-blue" %>">
            <%= leave.getStatus() %>
        </span>
    </div>

    <%-- 결재선 스탬프 영역 --%>
    <div class="approval-table">
        <%
            int maxStep = 6 - displayLevel;
            String[] signNames = {leave.getSign1(), leave.getSign2(), leave.getSign3(), leave.getSign4(), leave.getSign5()};
            java.sql.Date[] signDates = {leave.getSign1Date(), leave.getSign2Date(), leave.getSign3Date(), leave.getSign4Date(), leave.getSign5Date()};
            
            for (int i = 1; i <= maxStep; i++) {
                String stepTitle = (i == 1) ? "담당" : (i == maxStep ? "5단계 결재관" : i + "단계 결재관");
                String signName = signNames[i-1];
                java.sql.Date signDate = signDates[i-1];
                
                boolean isRejected = (signName != null && signName.contains("반려"));
                boolean isSigned = (signName != null && !isRejected);
        %>
        <div class="stamp-box">
            <div class="stamp-step"><%= stepTitle %></div>
            <div class="stamp-name <%= isSigned ? "signed" : "" %>" style="<%= isRejected ? "color: #ef4444;" : "" %>">
                <%= signName != null ? signName.replaceAll("\\(반려\\)", "") + (isRejected ? " (반)" : "") :"--" %>
            </div>
            <div class="stamp-date"><%= signDate != null ? signDate.toString() : "-" %></div>
        </div>
        <% } %>
    </div>

    <table class="info-table">
        <tr>
            <th>문서 번호</th><td><%= leave.getLeaveNo() %></td>
            <th>기안자 정보</th><td><%= leave.getEmpName() %> (Lv.<%= displayLevel %>)</td>
        </tr>
        <tr>
            <th>휴가 기간</th><td><%= leave.getStartDate() %> ~ <%= leave.getEndDate() %></td>
            <th>사용 일수</th><td><%= leave.getUseDays() %> 일</td>
        </tr>
        <tr>
            <th>휴가 사유</th>
            <td colspan="3" class="reason-cell" style="white-space: pre-wrap;"><%= leave.getReason() %></td>
        </tr>
    </table>

    <div class="btn-group">
        <button type="button" class="btn-list" onclick="location.href='documentList.do?tab=leave'">목록으로 이동</button>
        
        <% if (isPending && isMyTurn && isNotOwner) { %>
            <form action="leaveApproveProcess.do" method="post" style="display:inline;" onsubmit="return confirm('승인하시겠습니까?');">
                <input type="hidden" name="action" value="approve">
                <input type="hidden" name="leaveNo" value="<%= leave.getLeaveNo() %>">
                <button type="submit" class="btn-approve">결재 승인</button>
            </form>
            <form action="leaveApproveProcess.do" method="post" style="display:inline;" onsubmit="return confirm('반려하시겠습니까?');">
                <input type="hidden" name="action" value="reject">
                <input type="hidden" name="leaveNo" value="<%= leave.getLeaveNo() %>">
                <button type="submit" class="btn-reject">결재 반려</button>
            </form>
        <% } else if (isPending) { %>
            <p style="color:#64748b; font-size:14px; margin-top:10px;">
                ※ 현재 결재 권한이 없거나 대기 중입니다.
            </p>
        <% } %>
    </div>
</body>
</html>