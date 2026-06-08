<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*, com.groupware.dto.*, com.groupware.dao.*" %>
<%
        /* =========================================================================
     * [Step 1] 사용자 세션 체크 및 실시간 정보 동기화 로직
     * ========================================================================= */
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp"); 
        return;
    }
 	// 실시간 연차 갱신 로직
    EmployeeDAO empDao = new EmployeeDAO();
    EmployeeDTO updatedEmp = empDao.getEmployeeByNo(String.valueOf(loginEmp.getEmpNo()));
    if (updatedEmp != null) {
        session.setAttribute("loginEmp", updatedEmp); 
        loginEmp = updatedEmp; 
    }
 	// 페이징 변수 (Controller에서 세팅된 값 사용)
    int currentEmpNo = loginEmp.getEmpNo();              
    boolean isManagerMode = "Y".equals(loginEmp.getManager()); 
    LocalDateTime currentDateTime = LocalDateTime.now();       
    String currentMapping = request.getRequestURI();           

    /* =========================================================================
     * [Step 2] 삼분할 다중 페이징 규격 변수 세팅 (Pagination Config)
     * ========================================================================= */
    final int RECORDS_PER_PAGE = 10; 
    final int PAGES_PER_BLOCK = 10;  
    /* =========================================================================
     * [Step 3] 비즈니스 데이터 연산 및 페이지 조각화 (SubList 처리)
     * ========================================================================= */
    
    	// 3-3. 내 휴가 신청 데이터 페이징 연산
    	LeaveDAO leaveDao = new LeaveDAO();
    	List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    		    
    	int leavePage = 1;
    	String leavePageParam = request.getParameter("leavePage");
    	if (leavePageParam != null && !leavePageParam.isEmpty()) leavePage = Integer.parseInt(leavePageParam);
    		    
    	int leaveTotalCount = (fullLeaveList != null) ? fullLeaveList.size() : 0;
    	int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / RECORDS_PER_PAGE);
    	if (leaveTotalPages == 0) leaveTotalPages = 1;
    		    
    	int leaveStartPage = ((leavePage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    	int leaveEndPage = Math.min(leaveStartPage + PAGES_PER_BLOCK - 1, leaveTotalPages);
    		    
    	int leaveStartIdx = (leavePage - 1) * RECORDS_PER_PAGE;
    	int leaveEndIdx = Math.min(leaveStartIdx + RECORDS_PER_PAGE, leaveTotalCount);
    	List<LeaveHistoryDTO> leaveList = (fullLeaveList != null && leaveStartIdx < leaveTotalCount) ? fullLeaveList.subList(leaveStartIdx, leaveEndIdx) : null;
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <script>
        function returnProcess(rentalNo) { 
            if (confirm("해당 비품을 반납 처리하시겠습니까?")) {
                saveScroll(); 
                location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; 
            }
        }
        
        function cancelReserve(resNo) { 
            if (confirm("정말 이 예약을 취소하시겠습니까?")) {
                saveScroll(); 
                location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; 
            }
        }

        function navigateWithScroll(url) {
            saveScroll();
            location.href = url;
        }
        
        function saveScroll() {
            localStorage.setItem("main_scroll_y", window.scrollY);
        }

        window.addEventListener("DOMContentLoaded", function() {
            var savedScrollY = localStorage.getItem("main_scroll_y");
            if (savedScrollY !== null) {
                window.scrollTo(0, parseInt(savedScrollY)); 
                localStorage.removeItem("main_scroll_y"); 
            }
        });
    </script>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
<div class="section-title"><a href="myLeave.jsp" class="data-link">내 휴가 신청 현황</a></div>
        <div class="table-wrapper">
            <table class="table-leave">
                <thead>
                    <tr>
                        <th>문서 번호</th>
                        <th>휴가 기간</th>
                        <th>사용 일수</th>
                        <th>사유</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <tr><td colspan="5" style="padding: 105px 0; color: #6c757d; border-bottom: none;">신청한 휴가 내역이 없습니다.</td></tr>
                    <% } else { 
                           for (LeaveHistoryDTO leave : leaveList) {
                               String badgeClass = "bg-warning";
                               if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                               else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                            <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                            <td><b><%=leave.getUseDays()%>일</b></td>
                            <td><%=leave.getReason()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
            </table>
        </div> 
        
    <div class="pagination-container">
    		<% if (leaveStartPage > 1) { %>
        		<button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?leavePage=<%=leaveStartPage-1%>')">이전</button>
    		<% } %>
    		<% for (int i = leaveStartPage; i <= leaveEndPage; i++) { %>
        		<button type="button" class="pagination-btn <%= (i == leavePage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?leavePage=<%=i%>')"><%=i%></button>
    		<% } %>
    		<% if (leaveEndPage < leaveTotalPages) { %>
        		<button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?leavePage=<%=leaveEndPage+1%>')">다음</button>
    		<% } %>
	</div>
	</div>
</body>
</html>