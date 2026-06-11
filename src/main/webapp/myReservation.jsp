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
     * [Step 3-1] 회의실 예약 현황 데이터 수집 / 정렬 / 페이징 (Locale-Independent)
     * ========================================================================= */
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> rawReserveList = resDao.getMyReservations(currentEmpNo); 
    List<Map<String, Object>> fullReserveList = new ArrayList<>();

    if (rawReserveList != null) {
        for (ReservationDTO dto : rawReserveList) {
            Map<String, Object> m = new HashMap<>();
            m.put("resNo", dto.getResNo());
            m.put("roomId", dto.getRoomId());
            m.put("resDate", dto.getResDate() != null ? dto.getResDate().toString() : "");
            m.put("startTime", dto.getStartTime());
            m.put("endTime", dto.getEndTime());
            m.put("purpose", dto.getPurpose());
             
            // 💡 [국가독립적 리팩토링] 한국어 하드코딩 문자열 대신 StatusUtil에서 다국어 '프로퍼티 키'를 먼저 추출
            String targetKey = StatusUtil.getStatusKey(dto.getStatus());
            
            // 💡 [기능유지] 예약완료 상태인 건들 중, 현재 시간이 이용 종료 시간을 넘어섰는지 체크하는 기존 로직 유지
            if ("status.res.complete".equals(targetKey)) {
                try {
                    LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                    LocalTime eTime = LocalTime.parse(dto.getEndTime());
                    if (currentDateTime.isAfter(LocalDateTime.of(rDate, eTime))) {
                        // 조건 충족 시 '이용 종료' 프로퍼티 키로 강제 치환
                        targetKey = "status.res.finished";
                    }
                } catch (Exception e) {}
            }
            
            m.put("statusKey", targetKey); // 정렬과 화면 출력을 위해 한국어 대신 프로퍼티 키를 바인딩
            m.put("rawDto", dto);          // 풀캘린더나 확장 기능을 대비한 원본 백업
            fullReserveList.add(m);
        }
    }

    // 💡 [국가독립적 정렬] 추출된 '다국어 프로퍼티 키' 문자열을 기준으로 우선순위 정렬 수행
    if (!fullReserveList.isEmpty()) {
        Collections.sort(fullReserveList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                String k1 = (String) m1.get("statusKey");
                String k2 = (String) m2.get("statusKey");
                 
                // 예약완료(status.res.complete) 상태에 최우선 가중치(1순위) 부여
                int p1 = "status.res.complete".equals(k1) ? 1 : 2;
                int p2 = "status.res.complete".equals(k2) ? 1 : 2;
                 
                if (p1 != p2) {
                    return Integer.compare(p1, p2); // 1순위: 상태 가중치 비교
                }
                 
                // 2순위: 상태가 동률일 경우 예약일자 오름차순 정렬 (기존 기능 유지)
                String d1 = (String) m1.get("resDate");
                String d2 = (String) m2.get("resDate");
                return d1.compareTo(d2);
            }
        });
    }
     
    // 정렬이 완료된 컬렉션을 규격에 맞춰 페이징 데이터 쪼개기(subList) 진행
    int resTotalCount = fullReserveList.size();
    int resTotalPages = (int) Math.ceil((double) resTotalCount / RECORDS_PER_PAGE);
    if (resTotalPages == 0) resTotalPages = 1; 
     
    int resStartPage = ((resPage - 1) / PAGES_PER_BLOCK) * PAGES_PER_BLOCK + 1;
    int resEndPage = Math.min(resStartPage + PAGES_PER_BLOCK - 1, resTotalPages); 
     
    int resStartIdx = (resPage - 1) * RECORDS_PER_PAGE;
    int resEndIdx = Math.min(resStartIdx + RECORDS_PER_PAGE, resTotalCount);
     
    // 화면 단 <tbody> 루프에 공급할 최종 회의실 리스트 조각 생성
    List<Map<String, Object>> resSubList = (resStartIdx < resTotalCount) 
                                         ? fullReserveList.subList(resStartIdx, resEndIdx) : null;

 %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <script>
    // 회의실 예약 취소 핸들러
    function cancelReserve(resNo) { 
        if (confirm("<fmt:message key="alert.cancel.confirm" />")) {
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
	<div class="section-title"> 
                <a href="myReservation.jsp" class="data-link"><fmt:message key="table.res.title" /></a>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                        <th><fmt:message key="table.res.no" /></th>
                        <th><fmt:message key="table.res.room" /></th>
                        <th><fmt:message key="table.res.date" /></th>
                        <th><fmt:message key="table.res.time" /></th>
                        <th><fmt:message key="table.res.purpose" /></th>
                        <th><fmt:message key="table.res.status" /></th>
                        <th><fmt:message key="table.res.note" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (resSubList == null || resSubList.isEmpty()) { %>
                            <tr><td colspan="7" style="padding: 105px 0; color: #6c757d; border-bottom: none; text-align: center;"><fmt:message key="table.res.empty" /></td></tr>
                        <% } else {
                               for (Map<String, Object> resMap : resSubList) {
                                   String statusKey = (String) resMap.get("statusKey");
                                   String statusClass = "bg-primary"; 
                                   
                                   if ("status.res.finished".equals(statusKey)) {
                                       statusClass = "bg-secondary"; 
                                   } else if ("status.res.canceled".equals(statusKey)) {
                                       statusClass = "bg-danger"; 
                                   }
                        %>
                        <tr>
                            <td style="color: #6c757d;"><%=resMap.get("resNo")%></td>
                            <td style="font-weight: 600; color: #343a40;"><%=resMap.get("roomId")%>호</td>
                            <td><%=resMap.get("resDate")%></td>
                            <td><%=resMap.get("startTime")%> ~ <%=resMap.get("endTime")%></td>
                            <td>
                                <span class="title-link" title="<%=resMap.get("purpose")%>"><%=resMap.get("purpose")%></span>
                            </td>
                            <td><span class="status-badge <%=statusClass%>"><fmt:message key="<%=statusKey%>" /></span></td>
                            <td>
                                <% if ("status.res.complete".equals(statusKey)) { %>
                                    <button class="btn-action" onclick="cancelReserve(<%=resMap.get("resNo")%>)"><fmt:message key="table.res.btn.cancel" /></button> 
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
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resStartPage-1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.prev" /></button>
                <% } %>
                <% for (int i = resStartPage; i <= resEndPage; i++) { %>
                    <button type="button" class="pagination-btn <%= (i == resPage) ? "active" : "" %>" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=i%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><%=i%></button>
                <% } %>
                <% if (resEndPage < resTotalPages) { %>
                    <button type="button" class="pagination-btn" onclick="navigateWithScroll('<%=currentMapping%>?resPage=<%=resEndPage+1%>&rentalPage=<%=rentalPage%>&leavePage=<%=leavePage%>')"><fmt:message key="dashboard.btn.next" /></button>
                <% } %>
            </div>
        </div> 
</body>
</html>