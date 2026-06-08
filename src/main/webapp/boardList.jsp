<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.groupware.dto.BoardDTO" %>
<%@ page import="com.groupware.dto.EmployeeDTO" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
    int type = (Integer) request.getAttribute("type");
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPages = (Integer) request.getAttribute("totalPages");
    String searchType = (String) request.getAttribute("searchType");
    String keyword = (String) request.getAttribute("keyword");
    if (searchType == null) searchType = "";
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 - GroupWare</title>
<link rel="stylesheet" href="css/board.css">
</head>
<body>
    <jsp:include page="header.jsp" />
	
    <div class="board-container">
        <div class="board-header">
            <h2>게시판</h2>
            <div class="board-tabs">
                <a href="boardList.do?type=1" class="board-tab <%= (type == 1) ? "active" : "" %>">사내 소식</a>
                <a href="boardList.do?type=2" class="board-tab <%= (type == 2) ? "active" : "" %>">자유 게시판</a>
                <a href="boardList.do?type=3" class="board-tab <%= (type == 3) ? "active" : "" %>">건의 게시판</a>
            </div>
        </div>

        <table class="board-table">
            <thead>
                <tr>
                    <th width="80">번호</th>
                    <th>제목</th>
                    <th width="120">작성자</th>
                    <th width="150">작성일</th>
                    <th width="80">조회수</th>
                </tr>
            </thead>
            <tbody>
                <% if (boardList != null && !boardList.isEmpty()) { 
                    for (BoardDTO board : boardList) { %>
                    <tr>
                        <td><%= board.getBoardNo() %></td>
                        <td class="title-cell">
                            <a href="boardDetail.do?boardNo=<%= board.getBoardNo() %>"><%= board.getTitle() %></a>
                            <% if (board.getFileCount() > 0) { %>
                                <span style="font-size:12px; color:#999;">[<%= board.getFileCount() %>]</span>
                            <% } %>
                        </td>
                        <td><%= board.getEmpName() %></td>
                        <td><%= board.getRegDate() %></td>
                        <td><%= board.getHit() %></td>
                    </tr>
                <% } } else { %>
                    <tr>
                        <td colspan="5" style="padding:50px 0; color:#999;">등록된 게시글이 없습니다.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <!-- 페이징 -->
        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a href="boardList.do?type=<%= type %>&page=<%= currentPage - 1 %>&searchType=<%= searchType %>&keyword=<%= keyword %>">&laquo;</a>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
                <a href="boardList.do?type=<%= type %>&page=<%= i %>&searchType=<%= searchType %>&keyword=<%= keyword %>" class="<%= (currentPage == i) ? "active" : "" %>"><%= i %></a>
            <% } %>
            <% if (currentPage < totalPages) { %>
                <a href="boardList.do?type=<%= type %>&page=<%= currentPage + 1 %>&searchType=<%= searchType %>&keyword=<%= keyword %>">&raquo;</a>
            <% } %>
        </div>

        <div class="board-footer">
            <form action="boardList.do" method="get" class="search-form">
                <input type="hidden" name="type" value="<%= type %>">
                <select name="searchType">
                    <option value="title" <%= "title".equals(searchType) ? "selected" : "" %>>제목</option>
                    <option value="content" <%= "content".equals(searchType) ? "selected" : "" %>>내용</option>
                    <option value="author" <%= "author".equals(searchType) ? "selected" : "" %>>작성자</option>
                </select>
                <input type="text" name="keyword" value="<%= keyword %>" placeholder="검색어 입력">
                <button type="submit" class="btn btn-outline">검색</button>
            </form>

            <% if (type == 1) { %>
                <% if (loginEmp != null && "Y".equals(loginEmp.getManager())) { %>
                    <a href="boardWrite.do?type=<%= type %>" class="btn btn-primary">글쓰기</a>
                <% } %>
            <% } else { %>
                <a href="boardWrite.do?type=<%= type %>" class="btn btn-primary">글쓰기</a>
            <% } %>
        </div>
    </div>
</body>
</html>
