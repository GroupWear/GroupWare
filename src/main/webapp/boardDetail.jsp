<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO" %>
<%@ page import="com.groupware.dto.BoardDTO" %>
<%@ page import="com.groupware.dto.BoardFileDTO" %>
<%@ page import="java.util.List" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<BoardFileDTO> fileList = (List<BoardFileDTO>) request.getAttribute("fileList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= (board != null) ? board.getTitle() : "" %> - GroupWare</title>
<link rel="stylesheet" href="css/board.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="board-container">
        <div class="board-header">
            <h2>
                <% 
                if (board != null) {
                    int bType = board.getBoardType();
                    if (bType == 1) out.print("사내 소식");
                    else if (bType == 2) out.print("자유 게시판");
                    else if (bType == 3) out.print("건의 게시판");
                }
                %>
            </h2>
        </div>

        <% if (board != null) { %>
        <div class="detail-view">
            <h3 style="padding:20px 15px; margin:0; border-bottom:1px solid #ddd;"><%= board.getTitle() %></h3>
            <div class="detail-info">
                <span>작성자: <b><%= board.getEmpName() %></b></span>
                <span>작성일: <%= board.getRegDate() %> | 조회수: <%= board.getHit() %></span>
            </div>
            
            <div class="detail-content"><%= board.getContent() %></div>

            <% if (fileList != null && !fileList.isEmpty()) { %>
                <div class="file-list">
                    <p style="margin-top:0; font-weight:600;">첨부파일</p>
                    <% for (BoardFileDTO file : fileList) { %>
                        <div class="file-item">
                            <span class="icon">📁</span>
                            <a href="fileDownload.do?fileNo=<%= file.getFileNo() %>"><%= file.getOrgName() %></a>
                            <span style="color:#999; font-size:12px;">(<%= file.getFileSize() / 1024 %> KB)</span>
                        </div>
                    <% } %>
                </div>
            <% } %>

            <div style="margin-top:30px; display:flex; justify-content: space-between;">
                <a href="boardList.do?type=<%= board.getBoardType() %>" class="btn btn-outline">목록으로</a>
                
                <div>
                    <% if (loginEmp != null && (board.getEmpNo() == loginEmp.getEmpNo() || "Y".equals(loginEmp.getManager()))) { %>
                        <a href="boardUpdate.do?boardNo=<%= board.getBoardNo() %>" class="btn btn-outline">수정</a>
                        <a href="javascript:void(0);" onclick="deleteConfirm(<%= board.getBoardNo() %>);" class="btn btn-outline" style="color:#e74c3c;">삭제</a>
                    <% } %>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <script>
        function deleteConfirm(boardNo) {
            if(confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
                location.href = 'boardDelete.do?boardNo=' + boardNo;
            }
        }
    </script>
</body>
</html>
