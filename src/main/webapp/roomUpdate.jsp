<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.RoomDTO" %>
<%
    RoomDTO room = (RoomDTO) request.getAttribute("room");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회의실 정보 수정</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
<style>
    .update-container { width: 500px; margin: 50px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
    .form-group { margin-bottom: 15px; }
    .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
    .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    .btn-save { width: 100%; padding: 12px; background: #343a40; color: #fff; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
</style>
</head>
<body>
<div class="update-container">
    <h2>회의실 정보 수정 (<%=room.getRoomId()%>)</h2>
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
                <option value="Y" <%="Y".equals(room.getHasBeam()) ? "selected" : ""%>>있음(Y)</option>
                <option value="N" <%="N".equals(room.getHasBeam()) ? "selected" : ""%>>없음(N)</option>
            </select>
        </div>
        <div class="form-group">
            <label>예약 가능 여부</label>
            <select name="enable">
                <option value="Y" <%="Y".equals(room.getEnable()) ? "selected" : ""%>>가능</option>
                <option value="N" <%="N".equals(room.getEnable()) ? "selected" : ""%>>점검중(불가)</option>
            </select>
        </div>
        <div class="form-group">
            <label>설명</label>
            <textarea name="description" rows="3"><%=room.getDescription()%></textarea>
        </div>
        <button type="submit" class="btn-save">수정 완료</button>
    </form>
</div>
</body>
</html>