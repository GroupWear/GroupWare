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
	//핵심 변수 선언 (누락되었던 변수들)
	int currentEmpNo = loginEmp.getEmpNo();
    boolean isManagerMode = "Y".equals(loginEmp.getManager());
    String currentMapping = request.getRequestURI();


    /* =========================================================================
     * [Step 2] 페이징 규격 및 비즈니스 데이터 연산
     * ========================================================================= */
    final int RECORDS_PER_PAGE = 10;
    final int PAGES_PER_BLOCK = 10;

    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> rawRentalList = isManagerMode
            ? rentalDao.getAllDocumentList()
            : rentalDao.getMyRentalList(currentEmpNo);
    
    List<RentalHistoryDTO> filteredRentalList = new ArrayList<>();
    if (rawRentalList != null) {
        for (RentalHistoryDTO item : rawRentalList) {
            if (isManagerMode) {
                boolean isRetired = (item.getEmpLevel() == 0);
                boolean isMyDoc = (item.getEmpNo() == currentEmpNo);
                boolean isTargetRetired = isRetired && ("대여중".equals(item.getStatus()) || "미반납".equals(item.getStatus()));
                if (!isTargetRetired && !isMyDoc) continue;
            }
            filteredRentalList.add(item);
        }
    }
    /* =========================================================================
     * [Step 3] 페이징 계산 (중복 선언 제거됨)
     * ========================================================================= */
    int rentalPage = 1;
    String rentalPageParam = request.getParameter("rentalPage");
    if (rentalPageParam != null && !rentalPageParam.isEmpty()) {
        try {
            rentalPage = Integer.parseInt(rentalPageParam);
        } catch (NumberFormatException e) {
            rentalPage = 1;
        }
    }

    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;

    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);

    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount)
            ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx)
            : null;
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
			location.href = 'returnProcess.do?rentalNo=' + rentalNo
					+ '&from=main';
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
		<div class="section-title">
                <a href="myRental.jsp" class="data-link"><%= isManagerMode ? "비품 대여 및 미반납 내역 (관리자)" : "내 비품 대여 현황" %></a>
            </div>
            <div class="table-wrapper">
                <table class="table-rental <%= isManagerMode ? "admin-view" : "" %>">
                    <thead>
                        <tr>
                            <th>기안 번호</th>
                            <% if (isManagerMode) { %> <th>기안자</th> <% } %>
                            <th>기안 제목</th>
                            <th>대여 기간</th>
                            <th>상태</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (rentalList == null || rentalList.isEmpty()) { %>
                        <tr><td colspan="<%= isManagerMode ? 6 : 5 %>" style="padding: 105px 0; color: #6c757d; border-bottom: none;">대여 내역이 없습니다.</td></tr>
                    <% } else {
                           for (RentalHistoryDTO item : rentalList) {
                               String displayStatus = item.getStatus();
                               boolean isRetiredCreator = (item.getEmpLevel() == 0);
                               
                               String badgeClass = "bg-secondary";
                               if ("승인대기".equals(displayStatus)) badgeClass = "bg-warning";
                               else if ("대여중".equals(displayStatus)) badgeClass = "bg-success";
                               else if ("반려됨".equals(displayStatus) || "미반납".equals(displayStatus)) badgeClass = "bg-danger";
                    %>
                        <tr>
                            <td><%=item.getRentalNo()%></td>
                            <% if (isManagerMode) { %>
                                <td style="font-weight: 600; color: #64748b;">
                                    <%=item.getEmpName()%> <%= isRetiredCreator ? "<br>(퇴사)" : "<br>(본인)" %>
                                </td>
                            <% } %>
                            <td>
                                <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" 
                                   style="text-decoration: none; color: #6366f1; font-weight: 600; display: block;">
                                    <span class="title-link" title="<%=item.getTitle()%>">
                                        <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                                    </span>
                                </a>
                            </td>
                            <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><%=displayStatus%></span></td>
                            <td>
                                <% if ("대여중".equals(displayStatus) || "미반납".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="returnProcess('<%=item.getRentalNo()%>')">반납 처리</button>
                                <% } else { %>
                                    <span style="color: #ced4da;">-</span>
                                <% } %>
                            </td>
                        </tr>
                    <%     } 
                       } %>
                    </tbody>
                </table>
            </div> 
            
            <div class="pagination-container">
    <% if (rentalStartPage > 1) { %>
        <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?rentalPage=<%=rentalStartPage-1%>')">이전</button>
    <% } %>
    
    <% for (int i = rentalStartPage; i <= rentalEndPage; i++) { %>
        <button type="button" class="pagination-btn <%= (i == rentalPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?rentalPage=<%=i%>')"><%=i%></button>
    <% } %>
    
    <% if (rentalEndPage < rentalTotalPages) { %>
        <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?rentalPage=<%=rentalEndPage+1%>')">다음</button>
    <% } %>
</div>
	</div>
</body>
</html>