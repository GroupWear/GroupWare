<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Groupware - 비밀번호 찾기</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    
    <link rel="stylesheet" href="css/findPw.css">
</head>
<body class="auth-body">

    <div class="auth-container">
        <div class="auth-logo">
            <span class="bold">Group</span><span class="thin">ware</span>
        </div>

        <h2 class="auth-title">비밀번호 찾기</h2>
        <p class="auth-subtitle">
            계정 보안을 위해 본인 인증이 필요합니다.<br>
            가입 시 등록된 사원번호와 이름을 입력해 주세요.
        </p>

        <form action="findPw.do" method="post">
            
            <div class="form-group">
                <label for="empNo" class="form-label">사원번호</label>
                <input type="text" id="empNo" name="empNo" class="form-input" 
                       placeholder="사원번호를 입력하세요 (예: 2026001)" required>
            </div>

            <div class="form-group">
                <label for="empName" class="form-label">이름</label>
                <input type="text" id="empName" name="empName" class="form-input" 
                       placeholder="성명을 입력하세요" required>
            </div>

            <button type="submit" class="btn-submit">정보 확인</button>
            
            <a href="index.jsp" class="back-to-login">로그인 화면으로 돌아가기</a>
            
        </form>
    </div>

</body>
</html>