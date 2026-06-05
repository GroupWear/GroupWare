<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>

<%
    // 세션에서 로그인한 사원 정보를 가져옴
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    // 로그인 정보가 없으면 로그인 페이지로 리다이렉트
    if (loginEmp == null) { response.sendRedirect("index.jsp"); return; }
    
    // 잔여 연차를 계산하여 색상 클래스 지정 (0일 이하면 빨간색, 아니면 파란색)
    int curLeave = loginEmp.getCurLeave();
    String leaveColor = (curLeave <= 0) ? "red-text" : "blue-text";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>휴가 신청</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/leaveForm.css">
</head>
<body>
    <div class="container">
        <div class="title-area">
            <h3>🏖️ 휴가 신청서</h3>
        </div>

        <div class="info-card">
            <p>신청인:<b> <%=loginEmp.getEmpName()%></b> (<%=loginEmp.getDept()%> / <%=loginEmp.getEmpLevel()%>단계)</p>
            <p>잔여 연차: <span class="<%=leaveColor%>"><b><%=curLeave%></b></span> / <%=loginEmp.getMaxLeave()%>일</p>
        </div>

       <form action="leaveForm.do" method="post" onsubmit="return validate();">
            <div class="form-group">
                <label>시작 기간</label>
                <input type="date" id="startDate" name="startDate" required onchange="updateEndDateMin()">
            </div>
            
            <div class="form-group spacing">
                <label>종료 기간</label>
                <input type="date" id="endDate" name="endDate" required>
            </div>
            
            <div class="form-group">
                <label>사유</label>
                <select id="reasonCategory" name="reasonCategory" onchange="toggleReason(this.value)">
                    <option value="연차">연차</option>
                    <option value="병가">병가</option>
                    <option value="경조사">경조사</option>
                    <option value="직접입력">직접 입력</option> 
                </select>
                <input type="text" id="customReason" name="reason" style="display:none; margin-top:10px;" placeholder="사유 입력">
            </div>

            <div class="btn-group">
                <button type="submit" class="btn-submit">신청서 제출</button>
                <button type="button" class="btn-cancel" onclick="location.href='main.do'">취소</button>
            </div>
        </form>
    </div>

    <script>
        // 페이지 로드 시 시작일의 최소 선택 가능 날짜를 오늘로 설정
        document.getElementById('startDate').setAttribute('min', new Date().toISOString().split('T')[0]);

        // 시작일이 변경될 때마다 종료일의 최소 날짜를 시작일로 업데이트
        function updateEndDateMin() {
            const startDate = document.getElementById('startDate').value;
            document.getElementById('endDate').setAttribute('min', startDate);
        }

        // 사유 선택에 따른 입력창 표시 여부 제어
        function toggleReason(val) {
            const input = document.getElementById('customReason');
            input.style.display = (val === '직접입력') ? 'block' : 'none';
            input.required = (val === '직접입력');
        }

        // 최종 유효성 검사 함수
        function validate() {
            const start = new Date(document.getElementById('startDate').value);
            const end = new Date(document.getElementById('endDate').value);
            
            let weekdayCount = 0;
            let currentDate = new Date(start);

            // 시작일부터 종료일까지 순회하며 평일(월~금) 개수 카운트
            while (currentDate <= end) {
                const dayOfWeek = currentDate.getDay(); // 0:일요일 ~ 6:토요일
                if (dayOfWeek !== 0 && dayOfWeek !== 6) {
                    weekdayCount++;
                }
                currentDate.setDate(currentDate.getDate() + 1);
            }

            // 주말만 선택하여 평일이 0일인 경우 전송 차단
            if (weekdayCount === 0) {
                alert('휴가는 주말을 제외한 평일 기준으로 선택해야 합니다.');
                return false;
            }

            return confirm('휴가를 신청하시겠습니까? (주말 제외 평일 ' + weekdayCount + '일 차감)');
        }
    </script>
</body>
</html>