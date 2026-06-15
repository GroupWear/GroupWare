<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // [1순위 고정] 자바 로직이 돌기 전, 요청과 응답 스트림의 구멍을 UTF-8로 선제 타격해서 열어둡니다.
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- [2순위 고정] JSTL이 브라우저 언어헤더를 읽어 동적으로 프로퍼티를 선택할 때, 깨지지 않도록 인코딩 필터 주입 --%>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="resources.message" scope="session" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%-- ★ 타이틀 다국어화 --%>
    <title>Groupware - <fmt:message key="pw.reset.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/findPw.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <%-- ★ 상단 타이틀 및 설명 다국어화 --%>
        <h2 class="auth-title"><fmt:message key="pw.reset.header" /></h2>
        <p style="text-align: center; color: #666; margin-bottom: 20px;">
            <fmt:message key="pw.reset.description" />
        </p>
        
        <form action="resetPw.do" method="post"> 
            <%-- ★ 중요: 앞 단계(CheckUserController)에서 세션에 저장한 사번을 읽어옵니다 --%>
            <input type="hidden" name="empNo" value="<%= session.getAttribute("resetEmpNo") %>">
            
            <%-- ★ 새 비밀번호 입력 폼 다국어화 --%>
            <div class="form-group">
                <label><fmt:message key="pw.reset.newLabel" /></label>
                <fmt:message key="pw.reset.newPlaceholder" var="newPwPlaceholder"/>
                <input type="password" name="newPw" class="form-input" placeholder="${newPwPlaceholder}" required>
            </div>
            
            <%-- ★ 새 비밀번호 확인 폼 다국어화 --%>
            <div class="form-group">
                <label><fmt:message key="pw.reset.confirmLabel" /></label>
                <fmt:message key="pw.reset.confirmPlaceholder" var="confirmPwPlaceholder"/>
                <input type="password" name="newPwConfirm" class="form-input" placeholder="${confirmPwPlaceholder}" required>
            </div>
            
            <%-- ★ 버튼 텍스트 다국어화 --%>
            <button type="submit" class="btn-confirm" style="width: 100%; margin-top: 10px;">
                <fmt:message key="pw.reset.btnSubmit" />
            </button>
            <a href="index.jsp" class="btn-cancel" style="width: 100%; margin-top: 10px;">
                <fmt:message key="pw.reset.btnCancel" />
            </a>
        </form>
    </div>
</body>
</html>