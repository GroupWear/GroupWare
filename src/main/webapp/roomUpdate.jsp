<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.RoomDTO" %>
<%@ page import="com.groupware.dto.EmployeeDTO" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    RoomDTO room = (RoomDTO) request.getAttribute("room");
    if (room == null) {
        response.sendRedirect("adminEqList.do");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>사내 시스템 - 회의실 정보 수정</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/roomUpdate.css">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="form-container">
    <h2>📋 회의실 정보 수정</h2>
    
    <div class="info-box">
        <h3>회의실 ID: <%=room.getRoomId()%></h3>
    </div>

    <form action="roomUpdateProcess.do" method="post" onsubmit="return confirm('수정된 내용을 저장하시겠습니까?');">
        <input type="hidden" name="roomId" value="<%=room.getRoomId()%>">
        
        <div class="form-group">
            <label for="roomName">회의실 명칭</label>
            <input type="text" id="roomName" name="roomName" class="form-control" 
                   value="<%=room.getRoomName()%>" required>
        </div>
        
        <div class="form-group">
            <label for="capacity">수용 인원 (명)</label>
            <input type="number" id="capacity" name="capacity" class="form-control" 
                   value="<%=room.getCapacity()%>" min="1" required>
        </div>
        
        <div class="form-group">
            <label for="hasBeam">빔프로젝터 유무</label>
            <select id="hasBeam" name="hasBeam" class="form-control">
                <option value="Y" <%="Y".equals(room.getHasBeam()) ? "selected" : ""%>>있음 (Y)</option>
                <option value="N" <%="N".equals(room.getHasBeam()) ? "selected" : ""%>>없음 (N)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="enable">예약 가능 여부</label>
            <select id="enable" name="enable" class="form-control">
                <option value="Y" <%="Y".equals(room.getEnable()) ? "selected" : ""%>>가능 (정상 운영)</option>
                <option value="N" <%="N".equals(room.getEnable()) ? "selected" : ""%>>점검 중 (예약 불가)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="description">회의실 상세 설명</label>
            <textarea id="description" name="description" class="form-control" 
                      rows="4" placeholder="회의실에 대한 부가 설명을 입력하세요."><%=room.getDescription()%></textarea>
        </div>
        
        <div class="btn-area">
            <button type="submit" class="btn btn-submit">수정 내용 저장</button>
            <a href="adminEqList.do" class="btn btn-cancel">취소</a>
        </div>
    </form>
</div>

</body>
</html>
