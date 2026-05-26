<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Groupware - 비밀번호 변경</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/findPw.css">
</head>
<body class="auth-body">

    <div class="auth-container">
        <h2 class="auth-title">보안 인증 (비밀번호 변경)</h2>
        
        <form action="changePw.do" method="post">
            <div class="form-group">
                <label>현재 비밀번호</label>
                <input type="password" name="currentPw" class="form-input" required>
            </div>
            <div class="form-group">
                <label>새 비밀번호</label>
                <input type="password" name="newPw" class="form-input" required>
            </div>
            <div class="form-group">
                <label>새 비밀번호 확인</label>
                <input type="password" name="newPwConfirm" class="form-input" required>
            </div>

            <button type="submit" class="btn-confirm" style="width: 100%; margin-top: 10px;">변경 사항 저장</button>
            <a href="myPage.jsp" class="btn-cancel" style="width: 100%; margin-top: 10px;">취소하고 마이페이지로 돌아가기</a>
        </form>
    </div>
</body>
</html>