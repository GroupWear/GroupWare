<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.time.*, com.groupware.dto.*, com.groupware.dao.*, com.groupware.util.StatusUtil" %>
<%
    // [1순위 고정] 자바 로직이 돌기 전, 요청과 응답 스트림의 구멍을 UTF-8로 선제 타격해서 열어둡니다.
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- [2순위 고정] JSTL이 브라우저 언어헤더를 읽어 동적으로 프로퍼티를 선택할 때, 깨지지 않도록 인코딩 필터 주입 --%>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="resources.message" scope="session" />

<%
        /* =========================================================================
     * [Step 1] 사용자 세션 체크 및 실시간 정보 동기화 로직
     * ========================================================================= */
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp"); 
        return;
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
    // 현재 요청된 페이지 파라미터 받기 (기본값 1)
    int resPage = 1;
    String resPageParam = request.getParameter("resPage");
    if (resPageParam != null && !resPageParam.isEmpty()) resPage = Integer.parseInt(resPageParam);

    int rentalPage = 1;
    String rentalPageParam = request.getParameter("rentalPage");
    if (rentalPageParam != null && !rentalPageParam.isEmpty()) rentalPage = Integer.parseInt(rentalPageParam);

    int leavePage = 1;
    String leavePageParam = request.getParameter("leavePage");
    if (leavePageParam != null && !leavePageParam.isEmpty()) leavePage = Integer.parseInt(leavePageParam);

    /* =========================================================================
     * [Step 3-3] 내 휴가 신청 데이터 수집 / 정렬 / 페이징 (Locale-Independent)
     * ========================================================================= */
    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> fullLeaveList = leaveDao.getMyLeaveList(currentEmpNo); 
    
    // 💡 [국가독립적 정렬] 휴가 데이터 정렬 기준을 StatusUtil Key 기반으로 스위칭
    if (fullLeaveList != null && !fullLeaveList.isEmpty()) {
        Collections.sort(fullLeaveList, new Comparator<LeaveHistoryDTO>() {
            @Override
            public int compare(LeaveHistoryDTO l1, LeaveHistoryDTO l2) {
                String k1 = StatusUtil.getStatusKey(l1.getStatus());
                String k2 = StatusUtil.getStatusKey(l2.getStatus());
                
                int p1 = 2; // 기본값 (반려됨 등 최하단)
                if ("status.rental.wait".equals(k1)) p1 = 0;       // 승인대기 (최상단)
                else if ("status.leave.complete".equals(k1)) p1 = 1; // 승인완료 (중간)
                
                int p2 = 2;
                if ("status.rental.wait".equals(k2)) p2 = 0;
                else if ("status.leave.complete".equals(k2)) p2 = 1;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 휴가 승인 상태 우선순위 정렬
                }
                
                // 2순위: 상태가 같으면 휴가 시작일(StartDate) 기준 최신날짜 순(내림차순) 정렬
                String d1 = l1.getStartDate() != null ? l1.getStartDate().toString() : "";
                String d2 = l2.getStartDate() != null ? l2.getStartDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    // 휴가 데이터 페이징 쪼개기 블록 연산
    int leaveTotalCount = (fullLeaveList != null) ? fullLeaveList.size() : 0;
    int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / RECORDS_PER_PAGE);
    if (leaveTotalPages == 0) leaveTotalPages = 1;
    
    int leaveStartPage = ((leavePage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int leaveEndPage = Math.min(leaveStartPage + PAGES_PER_BLOCK - 1, leaveTotalPages);
    
    int leaveStartIdx = (leavePage - 1) * RECORDS_PER_PAGE;
    int leaveEndIdx = Math.min(leaveStartIdx + RECORDS_PER_PAGE, leaveTotalCount);
    
    // 최종 출력용 휴가 리스트 조각 생성
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
<div class="section-title"><a href="myLeave.jsp" class="data-link"><fmt:message key="table.leave.title" /></a></div>
            <div class="table-wrapper">
                <table class="table-leave">
                    <thead>
                        <tr>
                            <th style="width: 10%;"><fmt:message key="table.leave.no" /></th>
                            <th style="width: 30%;"><fmt:message key="table.leave.date" /></th>
                            <th style="width: 15%;"><fmt:message key="table.leave.days" /></th>
                            <th><fmt:message key="table.leave.reason" /></th>
                            <th style="width: 15%;"><fmt:message key="table.leave.status" /></th>
                        </tr>
                    </thead>
                     <tbody>
                    <% if (leaveList == null || leaveList.isEmpty()) { %>
                        <tr><td colspan="5" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.leave.empty" /></td></tr>
                    <% } else { 
                               for (LeaveHistoryDTO leave : leaveList) {
                                   String badgeClass = "bg-warning";
                                   if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                                   else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                            <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                            <td><b><%=leave.getUseDays()%><fmt:message key="table.leave.days.unit" /></b></td>
                            <td><span class="title-link" title="<%=leave.getReason()%>"><%=leave.getReason()%></span></td>
                            <td><span class="status-badge <%=badgeClass%>"><fmt:message key="<%= StatusUtil.getStatusKey(leave.getStatus()) %>" /></span></td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
                </table>
            </div> 
            
            <div class="pagination-container">
                <% if (leaveStartPage > 1) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveStartPage-1%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = leaveStartPage; i <= leaveEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == leavePage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=i%>')"><%=i%></button>
                <% } %>
                <% if (leaveEndPage < leaveTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalPage%>&leavePage=<%=leaveEndPage+1%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>
	</div>
</body>
</html>