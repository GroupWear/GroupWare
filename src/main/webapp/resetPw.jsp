<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Groupware - 비밀번호 재설정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/findPw.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <h2 class="auth-title">새 비밀번호 설정</h2>
        <p style="text-align: center; color: #666; margin-bottom: 20px;">
            새로운 비밀번호를 입력해주세요.
        </p>
        <form action="resetPw.do" method="post"> 
            <input type="hidden" name="empNo" value="<%= request.getParameter("empNo") %>">
            <div class="form-group">
                <label>새 비밀번호</label>
                <input type="password" name="newPw" class="form-input" placeholder="새 비밀번호를 입력하세요" required>
            </div>
            <div class="form-group">
                <label>새 비밀번호 확인</label>
                <input type="password" name="newPwConfirm" class="form-input" placeholder="비밀번호를 다시 한번 입력하세요" required>
            </div>
            <button type="submit" class="btn-confirm" style="width: 100%; margin-top: 10px;">비밀번호 변경</button>
            <a href="index.jsp" class="btn-cancel" style="width: 100%; margin-top: 10px;">취소하고 로그인으로</a>
        </form>
    </div>
</body>
</html>