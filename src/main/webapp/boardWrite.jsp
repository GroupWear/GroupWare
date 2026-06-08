<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.BoardDTO" %>
<%@ page import="com.groupware.dto.BoardFileDTO" %>
<%@ page import="java.util.List" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<BoardFileDTO> fileList = (List<BoardFileDTO>) request.getAttribute("fileList");
    Integer typeAttr = (Integer) request.getAttribute("type");
    int type = (typeAttr != null) ? typeAttr : (board != null ? board.getBoardType() : 1);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= (board == null) ? "글쓰기" : "글 수정" %> - GroupWare</title>
<link rel="stylesheet" href="css/board.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="board-container">
        <div class="board-header">
            <h2>
                <% if (board == null) { %>
                    <% if (type == 1) out.print("사내 소식 작성");
                       else if (type == 2) out.print("자유 게시글 작성");
                       else if (type == 3) out.print("건의 사항 작성"); %>
                <% } else { %>
                    게시글 수정
                <% } %>
            </h2>
        </div>

        <form action="<%= (board == null) ? "boardWrite.do" : "boardUpdate.do" %>" method="post" enctype="multipart/form-data" class="write-form">
            <input type="hidden" name="type" value="<%= type %>">
            <% if (board != null) { %>
                <input type="hidden" name="boardNo" value="<%= board.getBoardNo() %>">
            <% } %>

            <table>
                <tr>
                    <th>제목</th>
                    <td><input type="text" name="title" value="<%= (board != null) ? board.getTitle() : "" %>" required placeholder="제목을 입력하세요"></td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td><textarea name="content" required placeholder="내용을 입력하세요"><%= (board != null) ? board.getContent() : "" %></textarea></td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td>
                        <% if (fileList != null && !fileList.isEmpty()) { %>
                            <div style="margin-bottom:10px;">
                                <p style="margin:0 0 5px 0; font-size:13px; color:#666;">기존 파일 (삭제하려면 체크)</p>
                                <% for (BoardFileDTO file : fileList) { %>
                                    <div style="margin-bottom:5px;">
                                        <label style="font-size:14px;">
                                            <input type="checkbox" name="delFiles" value="<%= file.getFileNo() %>"> <%= file.getOrgName() %>
                                        </label>
                                    </div>
                                <% } %>
                            </div>
                        <% } %>
                        <input type="file" name="files" multiple>
                        <p style="margin:5px 0 0 0; font-size:12px; color:#999;">여러 파일을 선택할 수 있습니다. (최대 10MB)</p>
                    </td>
                </tr>
            </table>

            <div style="margin-top:30px; text-align:center; display:flex; gap:10px; justify-content: center;">
                <button type="submit" class="btn btn-primary"><%= (board == null) ? "등록하기" : "수정완료" %></button>
                <a href="javascript:history.back();" class="btn btn-outline">취소</a>
            </div>
        </form>
    </div>
</body>
</html>
