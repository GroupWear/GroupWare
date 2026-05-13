<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.groupware.dto.RentalHistoryDTO"%>
<%@ page import="com.groupware.dto.ReservationDTO"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="java.time.LocalDateTime"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="com.groupware.dto.LeaveHistoryDTO"%>

<%
EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
if (loginEmp == null) {
	response.sendRedirect("index.jsp");
	return;
}
List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
/* List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
List<LeaveHistoryDTO> leaveList = (List<LeaveHistoryDTO>) request.getAttribute("leaveList"); */ // ★ 컨트롤러에서 보내준 데이터
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 마이페이지</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/myPage.css">

<script>
    /* function processReturn(rentalNo) {
        if (confirm("해당 비품을 반납 처리하시겠습니까?")) {
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=mypage';
        }
    } */
    function cancelReserve(resNo) {
        if (confirm("정말 이 예약을 취소하시겠습니까?")) {
            location.href = "cancelReserve.do?resNo=" + resNo + '&from=mypage';
        }
    }
</script>
</head>
<body>

	<div class="container">
		<div class="page-header">
			<h2>마이페이지</h2>
			<div class="btn-group">
				<a href="changePw.jsp" class="btn-pw">비밀번호 변경</a> <a href="main.jsp"
					class="btn-main">시스템 메인으로</a>
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
					<%
					if (reserveList == null || reserveList.isEmpty()) {
					%>
					<tr>
						<td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이
							없습니다.</td>
					</tr>
					<%
					} else {
					for (ReservationDTO dto : reserveList) {
						String displayStatus = dto.getStatus();
						String statusClass = "bg-primary";

						// ★ 추가된 로직: 현재 DB상 '예약완료'여도, 현재 시간이 종료 시간을 넘었는지 실시간 확인
						if ("예약완료".equals(displayStatus)) {
							try {
						LocalDate rDate = ((java.sql.Date) dto.getResDate()).toLocalDate();
						LocalTime eTime = LocalTime.parse(dto.getEndTime());
						LocalDateTime endDateTime = LocalDateTime.of(rDate, eTime);

						// 현재 시간이 종료 시간보다 미래일 경우
						if (LocalDateTime.now().isAfter(endDateTime)) {
							displayStatus = "이용 종료";
							statusClass = "bg-secondary"; // 이용 종료는 회색 배지
						}
							} catch (Exception e) {
						// 날짜 파싱 오류 대비
							}
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
							<!-- ★ 수정된 로직: 실시간 상태가 '예약완료'일 때만 예약 취소 버튼을 노출 --> <%
 if ("예약완료".equals(displayStatus)) {
 %>
							<button class="btn-action btn-cancel"
								onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button> <%
 } else {
 %> <span style="color: #ced4da;">-</span> <%
 }
 %>
						</td>
					</tr>
					<%
					}
					}
					%>
				</tbody>
			</table>
		</div>

		<%-- <div class="section-title">내 비품 대여 현황</div>
		<div class="table-wrapper">
			<table>
				<thead>
					<tr>
						<th>기안 번호</th>
						<th>기안 제목</th>
						<th>비품명</th>
						<th>대여 기간</th>
						<th>상태</th>
						<th>비고</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (myList == null || myList.isEmpty()) {
					%>
					<tr>
						<td colspan="6" style="padding: 40px; color: #6c757d;">비품 대여
							기안 내역이 없습니다.</td>
					</tr>
					<%
					} else {
					for (RentalHistoryDTO item : myList) {
						String status = item.getStatus();
						String badgeClass = "bg-secondary";

						if ("승인대기".equals(status))
							badgeClass = "bg-warning";
						else if ("대여중".equals(status))
							badgeClass = "bg-success";
						else if ("반려됨".equals(status))
							badgeClass = "bg-danger";
						else if ("미반납".equals(status))
							badgeClass = "bg-danger";
					%>
					<tr>
						<td style="color: #6c757d;"><%=item.getRentalNo()%></td>
						<td style="text-align: left; padding-left: 20px;"><%=item.getTitle() != null ? item.getTitle() : "제목 없음"%></td>
						<td style="font-weight: 600; color: #343a40;"><%=item.getEqName()%></td>
						<td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
						<td><span class="status-badge <%=badgeClass%>"><%=status%></span></td>
						<td>
							<%
							if ("대여중".equals(status) || "미반납".equals(status)) {
							%>
							<button class="btn-action btn-return"
								onclick="processReturn('<%=item.getRentalNo()%>')">반납
								처리</button> <%
 } else {
 %> <span style="color: #ced4da;">-</span> <%
 }
 %>
						</td>
					</tr>
					<%
					}
					}
					%>
				</tbody>
			</table>
		</div> --%>
		<!-- 3. 내 휴가 신청 현황 (새로 추가) -->
		<%-- <div class="section-title">내 휴가 신청 현황</div>
		<div class="table-wrapper">
			<table>
				<thead>
					<tr>
						<th>문서 번호</th>
						<th>휴가 기간</th>
						<th>사용 일수</th>
						<th>신청 사유</th>
						<th>결재 상태</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (leaveList == null || leaveList.isEmpty()) {
					%>
					<tr>
						<td colspan="5" style="padding: 40px; color: #6c757d;">신청한 휴가
							내역이 없습니다.</td>
					</tr>
					<%
					} else {
					for (LeaveHistoryDTO leave : leaveList) {
						String badgeClass = "bg-warning";
						if ("승인완료".equals(leave.getStatus()))
							badgeClass = "bg-success";
						else if ("반려됨".equals(leave.getStatus()))
							badgeClass = "bg-danger";
					%>
					<tr>
						<td style="color: #6c757d;"><%=leave.getLeaveNo()%></td>
						<td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td>
						<td><b><%=leave.getUseDays()%>일</b></td>
						<td style="text-align: left; padding-left: 20px;"><%=leave.getReason()%></td>
						<td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td>
					</tr>
					<%
					}
					}
					%>
				</tbody>
			</table>
		</div> --%>
	</div>
	</div>


</body>
</html>