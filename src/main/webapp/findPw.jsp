<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/findPw.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-logo">
            <span class="bold">Group</span><span class="thin">ware</span>
        </div>
        <h2 class="auth-title">비밀번호 찾기</h2>
        <p class="auth-subtitle">계정 보안을 위해 본인 인증이 필요합니다.<br>가입 시 등록된 사원번호와 이름을 입력해 주세요.</p>

        <form action="findPw.do" method="post">
            <div class="form-group">
                <label>사원번호</label>
                <input type="text" name="empNo" class="form-input" placeholder="사원번호를 입력하세요" required>
            </div>
            <div class="form-group">
                <label>이름</label>
                <input type="text" name="empName" class="form-input" placeholder="성명을 입력하세요" required>
            </div>
            <div class="btn-group">
                <a href="index.jsp" class="btn-cancel">취소</a>
                <button type="submit" class="btn-confirm">정보 확인</button>
            </div>
        </form>
    </div>
</body>
</html>