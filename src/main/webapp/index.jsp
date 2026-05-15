<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GroupWare</title>
    <link rel="stylesheet" href="css/index.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;800&family=Noto+Sans+KR:wght@400;700&display=swap" rel="stylesheet">
</head>
    <div class="main-container">
        <div class="login-card">
            <header class="brand-header">
                <div class="brand-logo">
                    <span class="bold">Group</span><span class="thin">Ware</span>
                </div>
                </header>
            
            <%-- 
                1. action="login.do": LoginController 서블릿으로 연결
                2. method="post": 보안을 위해 POST 방식 사용 
            --%>
            <form action="login.do" method="post">
                <div class="input-wrap">
                    <%-- 컨트롤러의 getParameter("empNo")와 name이 일치해야 합니다. --%>
                    <input type="text" name="empNo" placeholder="사원번호" required>
                </div>
                <div class="input-wrap">
                    <%-- 컨트롤러의 getParameter("password")와 name이 일치해야 합니다. --%>
                    <input type="password" name="password" placeholder="비밀번호" required>
                </div>

                <%-- 로그인 실패 시 컨트롤러에서 보낸 에러 메시지를 출력합니다. --%>
                <% if(request.getAttribute("msg") != null) { %>
                    <div class="error-msg" style="color: #ff6b6b; font-size: 13px; margin-bottom: 15px; text-align: center;">
                        <%= request.getAttribute("msg") %>
                    </div>
                <% } %>

                <button type="submit" class="login-submit">로그인</button>
            </form>

            <div class="link-footer">
                <a href="join.jsp">계정 등록</a>
                <span class="sep">|</span>
                <a href="findPw.jsp">비밀번호 찾기</a>
            </div>
        </div>
    </div>
</body>
</html>