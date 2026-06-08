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
    
    // 3-1. 회의실 예약 현황 데이터 정밀 계산
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> fullReserveList = resDao.getMyReservations(currentEmpNo); 
    
    int resPage = 1;
    String resPageParam = request.getParameter("resPage");
    if (resPageParam != null && !resPageParam.isEmpty()) resPage = Integer.parseInt(resPageParam);
    
    int resTotalCount = (fullReserveList != null) ? fullReserveList.size() : 0;
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
    
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
    
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
    List<ReservationDTO> reserveList = (fullReserveList != null && resStartIdx < resTotalCount) ? fullReserveList.subList(resStartIdx, resEndIdx) : null;
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <script>
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
<div class="section-title"> <a href="myReservation" class="data-link">내 회의실 예약 현황</a></div>
            <div class="table-wrapper">
                <table class="table-res">
                    <thead>
                        <tr>
                            <th>예약 번호</th>
                            <th>회의실</th>
                            <th>예약 일자</th>
                            <th>사용 시간</th>
                            <th>사용 목적</th>
                            <th>상태</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (reserveList == null || reserveList.isEmpty()) { %>
                        <tr><td colspan="7" style="padding: 105px 0; color: #6c757d; border-bottom: none;">예약 내역이 없습니다.</td></tr>
                    <% } else {
                           for (ReservationDTO dto : reserveList) {
                               String displayStatus = dto.getStatus();
                               String statusClass = "bg-primary"; 

                               if ("예약완료".equals(displayStatus)) {
                                   try {
                                       LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                                       LocalTime eTime = LocalTime.parse(dto.getEndTime());
                                       if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                                           displayStatus = "이용 종료";
                                           statusClass = "bg-secondary"; 
                                       }
                                   } catch (Exception e) {}
                               } else if ("취소됨".equals(displayStatus)) {
                                   statusClass = "bg-danger"; 
                               }
                    %>
                        <tr>
                            <td style="color: #6c757d;"><%=dto.getResNo()%></td>
                            <td style="font-weight: 600; color: #343a40;"><%=dto.getRoomId()%>호</td>
                            <td><%=dto.getResDate()%></td>
                            <td><%=dto.getStartTime()%> ~ <%=dto.getEndTime()%></td>
                            <td><span class="title-link" title="<%=dto.getPurpose()%>"><%=dto.getPurpose()%></span></td>
                            <td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td>
                            <td>
                                <% if ("예약완료".equals(displayStatus)) { %>
                                    <button class="btn-action" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button> 
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
    <% if (resStartPage > 1) { %>
        <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resStartPage-1%>')">이전</button>
    <% } %>
    
    <% for (int i = resStartPage; i <= resEndPage; i++) { %>
        <button type="button" class="pagination-btn <%= (i == resPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=i%>')"><%=i%></button>
    <% } %>
    
    <% if (resEndPage < resTotalPages) { %>
        <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resEndPage+1%>')">다음</button>
    <% } %>
</div>
	</div>
</body>
</html>