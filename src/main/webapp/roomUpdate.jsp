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
<html>
<head>
<meta charset="UTF-8">
<title>회의실 정보 수정</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/roomUpdate.css">
</head>
<body>

<div class="update-container">
    <h2>회의실 정보 수정</h2>
    
    <div class="info-box">
        <h3>회의실 ID: <%=room.getRoomId()%></h3>
    </div>

    <form action="roomUpdateProcess.do" method="post">
        <input type="hidden" name="roomId" value="<%=room.getRoomId()%>">
        
        <div class="form-group">
            <label>회의실 명칭</label>
            <input type="text" name="roomName" value="<%=room.getRoomName()%>" required>
        </div>
        
        <div class="form-group">
            <label>수용 인원</label>
            <input type="number" name="capacity" value="<%=room.getCapacity()%>" required>
        </div>
        
        <div class="form-group">
            <label>빔프로젝터 유무</label>
            <select name="hasBeam">
                <option value="Y" <%="Y".equals(room.getHasBeam()) ? "selected" : ""%>>있음 (Y)</option>
                <option value="N" <%="N".equals(room.getHasBeam()) ? "selected" : ""%>>없음 (N)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label>예약 가능 여부</label>
            <select name="enable">
                <option value="Y" <%="Y".equals(room.getEnable()) ? "selected" : ""%>>가능</option>
                <option value="N" <%="N".equals(room.getEnable()) ? "selected" : ""%>>점검중 (불가)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label>설명</label>
            <textarea name="description" rows="3"><%=room.getDescription()%></textarea>
        </div>
        
        <div class="btn-group">
            <button type="submit" class="btn-action btn-save">수정 완료</button>
            <a href="adminEqList.do" class="btn-action btn-cancel">취소</a>
        </div>
    </form>
</div>

</body>
</html>
