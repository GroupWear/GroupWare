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
     * [Step 2] 페이징 규격 및 비즈니스 데이터 연산
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

/*     RentalDAO rentalDao = new RentalDAO();
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
    } */
    
    /* =========================================================================
     * [Step 3-2] 비품 대여 데이터 계산 / 권한별 필터링 / 정렬 (Locale-Independent)
     * ========================================================================= */
    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> rawRentalList = isManagerMode ? rentalDao.getAllDocumentList() : rentalDao.getMyRentalList(currentEmpNo);
    List<RentalHistoryDTO> filteredRentalList = new ArrayList<>();
    
    // 💡 [기능유지] 관리자 모드 시 퇴사자 계정 분기 및 본인 작성 글 필터링 복잡 로직 원본 유지
    if (rawRentalList != null) {
        for (RentalHistoryDTO item : rawRentalList) {
            if (isManagerMode) {
                boolean isRetired = (item.getEmpLevel() == 0); 
                boolean isMyDoc = (item.getEmpNo() == currentEmpNo);
                // 퇴사자이면서 현재 진행 중(대여중/미반납)이거나, 내가 올린 결재 문서가 아니라면 스킵
                boolean isTargetRetired = isRetired && ("대여중".equals(item.getStatus()) || "미반납".equals(item.getStatus()));
                if (!isTargetRetired && !isMyDoc) continue; 
            }
            filteredRentalList.add(item);
        }
    }
    
    // 💡 [국가독립적 정렬] 비품 대여 상태별 정렬 가중치를 StatusUtil Key 기반으로 점수화
    if (!filteredRentalList.isEmpty()) {
        Collections.sort(filteredRentalList, new Comparator<RentalHistoryDTO>() {
            @Override
            public int compare(RentalHistoryDTO r1, RentalHistoryDTO r2) {
                String k1 = StatusUtil.getStatusKey(r1.getStatus());
                String k2 = StatusUtil.getStatusKey(r2.getStatus());
                
                // 가중치 스코어링 (낮을수록 리스트의 최상단에 배치됨)
                int p1 = 3; // 기본값 (반납완료, 반려됨 등 종료 상태)
                if ("status.rental.renting".equals(k1)) p1 = 0;       // 대여중 (0순위)
                else if ("status.rental.notreturned".equals(k1)) p1 = 1; // 미반납 (1순위)
                else if ("status.rental.wait".equals(k1)) p1 = 2;       // 승인대기 (2순위)
                
                int p2 = 3;
                if ("status.rental.renting".equals(k2)) p2 = 0;
                else if ("status.rental.notreturned".equals(k2)) p2 = 1;
                else if ("status.rental.wait".equals(k2)) p2 = 2;
                
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 상태 가중치 정렬
                }
                
                // 2순위: 상태 점수가 완전히 같으면 대여 시작일(RentalDate) 기준 최신순(내림차순) 정렬
                String d1 = r1.getRentalDate() != null ? r1.getRentalDate().toString() : "";
                String d2 = r2.getRentalDate() != null ? r2.getRentalDate().toString() : "";
                return d2.compareTo(d1); 
            }
        });
    }
    
    // 비품 대여 페이징 쪼개기 블록 연산
    int rentalTotalCount = filteredRentalList.size();
    int rentalTotalPages = (int) Math.ceil((double) rentalTotalCount / RECORDS_PER_PAGE);
    if (rentalTotalPages == 0) rentalTotalPages = 1;
    
    int rentalStartPage = ((rentalPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int rentalEndPage = Math.min(rentalStartPage + PAGES_PER_BLOCK - 1, rentalTotalPages);
    
    int rentalStartIdx = (rentalPage - 1) * RECORDS_PER_PAGE;
    int rentalEndIdx = Math.min(rentalStartIdx + RECORDS_PER_PAGE, rentalTotalCount);
    
    // 화면 단에 노출될 최종 5개 행 서브리스트
    List<RentalHistoryDTO> rentalList = (rentalStartIdx < rentalTotalCount) ? filteredRentalList.subList(rentalStartIdx, rentalEndIdx) : null;  
            

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
<script>
//비품 반납 핸들러
function returnProcess(rentalNo) { 
    if (confirm("<fmt:message key="alert.return.confirm" />")) {
        saveScroll(); 
        location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; 
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
                <a href="myRental.jsp" class="data-link">
                    <fmt:message key="<%= isManagerMode ? \"table.rental.title.admin\" : \"table.rental.title.user\" %>" />
                </a>
            </div>
            <div class="table-wrapper">
                <table class="table-rental <%= isManagerMode ? "admin-view" : "" %>">
                    <thead>
                        <tr>
                            <th style="width: 10%;"><fmt:message key="table.rental.no" /></th>
                            <% if (isManagerMode) { %> <th style="width: 15%;"><fmt:message key="table.rental.writer" /></th> <% } %>
                            <th><fmt:message key="table.rental.title" /></th>
                            <th style="width: 25%;"><fmt:message key="table.rental.date" /></th>
                            <th style="width: 12%;"><fmt:message key="table.rental.status" /></th>
                            <th style="width: 12%;"><fmt:message key="table.rental.note" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (rentalList == null || rentalList.isEmpty()) { %>
                        <tr><td colspan="<%= isManagerMode ? 6 : 5 %>" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.rental.empty" /></td></tr>
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
                                    <%=item.getEmpName()%> 
                                    <fmt:message key="<%= isRetiredCreator ? \"table.rental.retired\" : \"table.rental.myself\" %>" var="empType"/>
                                    <br>(${empType})
                                </td>
                            <% } %>
                            <td>
                                <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" style="text-decoration: none; display: block;">
                                    <span class="title-link" style="color: #6366f1; font-weight: 600;" title="<%=item.getTitle()%>">
                                        <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                                    </span>
                                </a>
                            </td>
                            <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                            <td><span class="status-badge <%=badgeClass%>"><fmt:message key="<%= StatusUtil.getStatusKey(displayStatus) %>" /></span></td>
                            <td>
                                <% if ("대여중".equals(displayStatus) || "미반납".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="returnProcess('<%=item.getRentalNo()%>')"><fmt:message key="table.rental.btn.return" /></button>
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
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalStartPage-1%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = rentalStartPage; i <= rentalEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == rentalPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=i%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (rentalEndPage < rentalTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resPage%>&rentalPage=<%=rentalEndPage+1%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>
	</div>
</body>
</html>