<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="main-category">
    <ul>
        <li><a href="main.do">홈</a></li>
        <li><a href="notice.do">공지사항</a></li>
        <li><a href="approval.do">결재 관리</a></li>
        <li><a href="myPage.jsp">마이페이지</a></li>
        <% if("Y".equals(session.getAttribute("isAdmin"))) { %>
            <li><a href="admin.do">관리자 페이지</a></li>
        <% } %>
    </ul>
</nav>