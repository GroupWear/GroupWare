<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
    <div class="login-container">
        <h1>계정 등록</h1>
        <form action="JoinController" method="post">
            <div class="input-group">
                <input type="text" name="empId" placeholder="사원번호를 입력하세요" required>
            </div>
            <div class="input-group">
                <input type="password" name="empPw" placeholder="비밀번호를 설정하세요" required>
            </div>
            <div class="input-group">
                <input type="text" name="empName" placeholder="성함을 입력하세요" required>
            </div>
            <button type="submit" class="btn-login">가입하기</button>
        </form>
    </div>
</body>
</html>