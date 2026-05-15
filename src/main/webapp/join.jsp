<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계정 등록 - GroupWare</title>
<link rel="stylesheet" href="css/join.css">
<link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@300;600;800&display=swap" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

    <div class="join-card">
        <header class="brand-header">
            <div class="brand-logo">
                <span class="bold">Group</span><span class="thin">Ware</span>
            </div>
            <div class="subtitle">사내 시스템 계정 등록</div>
        </header>

        <form action="join.do" method="post" id="joinForm">
            
            <label class="field-label">사원번호 확인</label>
            <div class="input-group">
                <input type="text" name="empNo" id="empNo" placeholder="사번 입력 (숫자)" required autocomplete="off">
                <button type="button" class="btn-check" onclick="checkEmp()">정보 확인</button>
            </div>

            <label class="field-label">비밀번호 설정</label>
            <div style="margin-bottom: 18px;">
                <input type="password" name="password" id="password" placeholder="사용할 비밀번호 입력" required>
            </div>

            <label class="field-label">성명</label>
            <input type="text" id="empName" class="readonly-field" placeholder="사번 확인 시 자동 입력" readonly>

            <label class="field-label">부여된 권한 레벨</label>
            <input type="text" id="deptInfo" class="readonly-field" placeholder="사번 확인 시 자동 입력" readonly>

            <div class="button-area">
                <button type="button" class="btn-main btn-cancel" onclick="location.href='index.jsp'">취소</button>
                <button type="submit" class="btn-main btn-register">등록 완료</button>
            </div>
        </form>
    </div>

    <script>
    function checkEmp() {
        const empNo = $('#empNo').val().trim();
        if(!empNo) {
            alert("사번을 입력해주세요.");
            return;
        }
        $.ajax({
            url: 'getEmpInfo.jsp',
            type: 'get',
            data: { empNo: empNo },
            dataType: 'json',
            success: function(data) {
                if(data.result === "success") {
                    $('#empName').val(data.name);
                    $('#deptInfo').val(data.dept + " / " + data.position);
                    alert("사원 정보가 확인되었습니다.");
                    $('#password').focus();
                } else {
                    alert("등록되지 않은 사번이거나 이미 계정이 등록된 사원입니다.");
                    $('#empNo').val('').focus();
                }
            },
            error: function() { alert("통신 오류가 발생했습니다."); }
        });
    }

    $('#joinForm').submit(function() {
        if($('#empName').val() === "") {
            alert("'정보 확인' 버튼을 먼저 눌러주세요.");
            return false;
        }
        return true;
    });
    </script>
</body>
</html>