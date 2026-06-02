<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.groupware.dto.RentalHistoryDTO"%>
<%@ page import="com.groupware.dto.ReservationDTO"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="java.time.LocalDateTime"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="com.groupware.dto.LeaveHistoryDTO"%>

<%
    // 로그인 체크 세션없으면 로그인페이지로 이동
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    // 데이터 가져오기 (Controller나 Action에서 이미 바인딩해서 넘어온 데이터를 수집)
    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
    List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
    List<LeaveHistoryDTO> myLeaveList = (List<LeaveHistoryDTO>) request.getAttribute("leaveList"); 
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 마이페이지</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/myPage.css">

<script>
    function returnProcess(rentalNo) { 
        if (confirm("해당 반납 처리하시겠습니까?")) 
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; }
    function cancelReserve(resNo) { 
        if (confirm("정말 이 예약을 취소하시겠습니까?")) 
            location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; }
</script>
</head>
<body>
    
    <div class="header">
        <div class="header-inner">
            <a href="main.jsp" class="logo-area">
                <span class="logo-group">Group</span><span class="logo-ware">Ware</span>
            </a>
            
            <div class="nav-buttons">
                <span class="user-profile-info">
                    <% if ("Y".equals(loginEmp.getManager())) { %>
                        <span class="admin-tag">ADMIN</span>
                    <% } %>
                    <b><%=loginEmp.getEmpName()%></b>님
                </span>

                <% if ("Y".equals(loginEmp.getManager())) { %>
                    <a href="adminEqList.do" class="nav-btn admin-special">재고 관리</a>
                    <a href="admin.do" class="nav-btn admin-special">사원 관리</a>
                <% } %>

                <a href="officeMap.jsp" class="nav-btn">오피스 예약</a>
                <a href="leaveForm.do" class="nav-btn">휴가 신청</a>
                <a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
                <a href="documentList.do" class="nav-btn">기안 문서함</a>
                <a href="myPage.do" class="nav-btn">마이페이지</a>
                <a href="logout.do" class="nav-btn logout">로그아웃</a>
            </div>
        </div>
    </div>
    
    <div class="container">
        <div class="page-header">
            <h2>마이페이지</h2>
            <div class="btn-group">
                <a href="changePw.jsp" class="btn-pw">비밀번호 변경</a>
            </div>
        </div>

        <div class="section-title">내 회의실 예약 현황</div>
        <div class="table-wrapper">
            <table>
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
                    <tr>
                        <td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이 없습니다.</td>
                    </tr>
                    <%
                        } else {
                        for (ReservationDTO dto : reserveList) {
                            String displayStatus = dto.getStatus();
                            String statusClass = "bg-primary";

                            if ("예약완료".equals(displayStatus)) {
                                try {
                                    LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
                                    LocalTime eTime = LocalTime.parse(dto.getEndTime());
                                    LocalDateTime endDateTime = LocalDateTime.of(rDate, eTime);

                                    if (LocalDateTime.now().isAfter(endDateTime)) {
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
                        <td><%=dto.getPurpose()%></td>
                        <td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td>
                        <td>
                            <% if ("예약완료".equals(displayStatus)) { %>
                                <button class="btn-action btn-cancel" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button> 
                            <% } else { %> 
                                <span style="color: #ced4da;">-</span> 
                            <% } %>
                        </td>
                    </tr>
                    <%}} %>
                </tbody>
            </table>
        </div>

        <div class="section-title">내 비품 대여 현황</div>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>기안 번호</th>
                        <th>기안 제목</th>
                        <th>대여 기간</th>
                        <th>상태</th>
                        <th>비고</th>
                    </tr>
                </thead>
                <tbody>
                <% 
                    boolean hasData = false; 
                    if (myList != null && !myList.isEmpty()) {
                        for (RentalHistoryDTO item : myList) {
                            
                            // 현재 로그인한 내 사원번호 데이터만 뜨도록 필터링 유지
                            if (item.getEmpNo() != loginEmp.getEmpNo()) {
                                continue;
                            }
                            
                            String displayStatus = item.getStatus();
                            hasData = true; 
                            
                            String badgeClass = "bg-secondary";
                            if ("승인대기".equals(displayStatus)) {
                                badgeClass = "bg-warning";
                            } else if ("대여중".equals(displayStatus)) {
                                badgeClass = "bg-success";
                            } else if ("반려됨".equals(displayStatus) || "미반납".equals(displayStatus)) {
                                badgeClass = "bg-danger";
                            }
                %>
                <tr>
                    <td><%=item.getRentalNo()%></td>
                    
                    <%-- 💡 [myPage.jsp 기안 제목 동기화 완료]: 가운데 정렬 및 main.jsp와 일치하는 통합 링크 스타일 구현 --%>
                    <td style="text-align: center; padding: 10px 15px;">
                        <a href="rentalDetail.do?rentalNo=<%=item.getRentalNo()%>" 
                           class="title-link" 
                           style="color: #6366f1; font-weight: 600; text-decoration: none; cursor: pointer; display: inline-block;"
                           onmouseover="this.style.textDecoration='underline'; this.style.color='#0284c7';"
                           onmouseout="this.style.textDecoration='none'; this.style.color='#6366f1';">
                            <%=item.getTitle() != null ? item.getTitle() : "제목 없음"%>
                        </a>
                    </td>
                    
                    <td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
                    <td><span class="status-badge <%=badgeClass%>"><%=displayStatus%></span></td>
                    <td>
                        <% 
                            boolean isEligibleForReturn = "대여중".equals(displayStatus) || "미반납".equals(displayStatus);
                            if (isEligibleForReturn) { 
                        %>
                            <button class="btn-action btn-cancel" onclick="returnProcess('<%=item.getRentalNo()%>')">
                                반납 처리
                            </button>
                        <% } else { %> - <% } %>
                    </td>
                </tr>
                <% 
                        } 
                    } 
                    
                    if (!hasData) { 
                %>
                    <tr><td colspan="5" style="padding: 40px; color: #6c757d;">대여 내역이 없습니다.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div> 

        <div class="section-title">내 휴가 신청 현황</div>
        <div class="table-wrapper">
            <table>
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
                <% if (myLeaveList == null || myLeaveList.isEmpty()) { %>
                    <tr>
                        <td colspan="5" style="padding: 40px; color: #6c757d;">신청한 휴가 내역이 없습니다.</td>
                    </tr>
                <% } else { for (LeaveHistoryDTO leave : myLeaveList) {
                    String badgeClass = "bg-warning";
                    if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                    else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                %>
                <tr>
                    <td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
                    <td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
                    <td><b><%=leave.getUseDays()%>일</b></td>
                    <td style="text-align: left; padding-left: 20px;"><%=leave.getReason()%></td>
                    <td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td>
                </tr>
                <% } } %>
                </tbody>
            </table>
        </div> 
    </div>

</body>
</html>