<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GroupWare - 로그인</title>

<link rel="stylesheet" href="css/index.css?v=1"> <%-- ?v=1을 붙이면 새로고침을 강제합니다 --%>

<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@300;800&family=Noto+Sans+KR:wght@400;700&display=swap"
	rel="stylesheet">
</head>
<body>

	<div class="main-container">

		<div class="login-card">

			<header class="brand-header">
				<div class="brand-logo">
					<span class="bold">Group</span><span class="thin">Ware</span>
				</div>
			</header>

			<%-- 
                로그인 폼 설정 
                1. action="login.do": 사용자가 입력한 데이터를 LoginController 서블릿으로 전송
                2. method="post": 아이디와 비밀번호가 주소창에 노출되지 않도록 보안 처리
            --%>
			<form action="login.do" method="post">

				<div class="input-wrap">
					<%-- 
                        name="empNo": 서버(Java) 측에서 request.getParameter("empNo")로 
                        데이터를 받기 위한 식별자입니다. 
                    --%>
					<input type="text" name="empNo" placeholder="사원번호를 입력하세요." required
						autocomplete="off">
				</div>

				<div class="input-wrap">
					<%-- 
                        name="password": 서버 측에서 request.getParameter("password")로 
                        데이터를 받기 위한 식별자입니다. 
                    --%>
					<input type="password" name="password" placeholder="비밀번호를 입력하세요."
						required>
				</div>

				<%-- 
                    [동적 메시지 출력] 
                    로그인 실패 시 서블릿(Controller)에서 request.setAttribute("msg", "...")에 담아 보낸 
                    에러 메시지를 화면에 출력하는 로직입니다.
                --%>
				<% if(request.getAttribute("msg") != null) { %>
				<div class="error-msg"
					style="color: #ff6b6b; font-size: 13px; margin-bottom: 15px; text-align: center; font-weight: 600;">
					<%= request.getAttribute("msg") %>
				</div>
				<% } %>

				<button type="submit" class="login-submit">로그인</button>
			</form>

				<div class="link-footer">
					<%-- 계정 등록(join.jsp)과 비밀번호 찾기(findPw.jsp)로 이동하는 링크 --%>
					<a href="join.jsp">계정 등록</a> <span class="sep">|</span> <a
						href="findPw.jsp">비밀번호 찾기</a>
				</div>
			</div>
		</div>
	</body>
</html>