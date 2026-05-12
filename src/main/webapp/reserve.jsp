<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%@ page import="com.groupware.dto.RoomDTO"%>
<%
EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
if (loginEmp == null) {
	response.sendRedirect("index.jsp");
	return;
}

RoomDTO roomInfo = (RoomDTO) request.getAttribute("roomInfo");
if (roomInfo == null) {
	response.sendRedirect("main.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 예약 신청</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reserve.css">

<script>
    let timerInterval = null; 
    let selectedTimes = [];

    function isContinuous(times) {
        if (times.length <= 1) return true;
        for (let i = 0; i < times.length - 1; i++) {
            let t1 = parseInt(times[i].split(':')[0]);
            let t2 = parseInt(times[i+1].split(':')[0]);
            if (t2 - t1 !== 1) return false;
        }
        return true;
    }

    function startTimer(startStr, endStr) {
        let timeLeft = 300; // 5분 (테스트용 10초로 변경 가능)
        const timerBox = document.getElementById("timerBox");
        const timeDisplay = document.getElementById("timeDisplay");
        const submitBtn = document.getElementById("submitBtn");
        const holdBtn = document.getElementById("holdBtn");
        const holdTimeText = document.getElementById("holdTimeText");

        holdTimeText.innerText = "[" + startStr + " ~ " + endStr + "]";

        timerBox.style.display = "block"; 
        submitBtn.disabled = false; // 이때만 예약 확정 버튼이 활성화됨
        holdBtn.style.display = "none"; 

        document.querySelectorAll('.time-btn').forEach(btn => btn.disabled = true);

        if(timerInterval) clearInterval(timerInterval);
        timerInterval = setInterval(function() {
            timeLeft--;
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;
            timeDisplay.textContent = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                alert("예약 대기 시간이 만료되었습니다. 다시 진행해 주세요.");
                location.reload(); 
            }
        }, 1000);
    }

    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        const resDateInput = document.getElementById("resDate");
        const holdBtn = document.getElementById("holdBtn");
        
        // ★ 오늘 날짜 구해서 달력(min)에 세팅하기 (과거 날짜 원천 차단)
        const today = new Date();
        const todayStr = today.getFullYear() + '-' + String(today.getMonth() + 1).padStart(2, '0') + '-' + String(today.getDate()).padStart(2, '0');
        resDateInput.setAttribute("min", todayStr);
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                if(this.disabled) return;
                const timeStr = this.getAttribute('data-time');

                if (this.classList.contains('active')) {
                    this.classList.remove('active');
                    selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다.");
                        this.classList.add('active');
                        selectedTimes.push(timeStr);
                        selectedTimes.sort();
                    }
                } else {
                    selectedTimes.push(timeStr);
                    selectedTimes.sort();
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다.");
                        selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    } else {
                        this.classList.add('active');
                    }
                }
            });
        });

        holdBtn.addEventListener('click', function() {
            // ★ JS 이중 차단: HTML을 조작해서 누르더라도 여기서 튕겨냄
            <% boolean isRoomEnableJs = "Y".equals(roomInfo.getEnable()); %>
            <% if (!isRoomEnableJs) { %>
                alert("현재 점검 중으로 예약이 불가능한 회의실입니다.");
                return;
            <% } %>

            const selectedDate = resDateInput.value;
            if(!selectedDate) { alert("예약 일자를 먼저 선택해 주세요."); return; }
            if(selectedTimes.length === 0) { alert("예약할 시간을 선택해 주세요."); return; }

            const startTime = selectedTimes[0];
            let lastHour = parseInt(selectedTimes[selectedTimes.length - 1].split(':')[0]);
            let endTimeStr = (lastHour + 1 < 10 ? '0' + (lastHour + 1) : (lastHour + 1)) + ':00';
            
            const roomId = document.getElementById("roomId").value;
            const empNo = document.getElementById("empNo").value;

            const formData = new URLSearchParams();
            formData.append("roomId", roomId);
            formData.append("empNo", empNo);
            formData.append("resDate", selectedDate);
            formData.append("startTime", startTime);
            formData.append("endTime", endTimeStr); 

            fetch('holdReserve.do', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            })
            .then(response => response.json())
            .then(data => {
                if(data.result === 'success') {
                    document.getElementById('resNo').value = data.resNo;
                    startTimer(startTime, endTimeStr);
                } else {
                    alert("선택하신 시간에 이미 등록된 예약이 존재합니다. 새로고침 후 다시 시도해 주세요.");
                    location.reload();
                }
            })
            .catch(error => console.error('Error:', error));
        });

        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;
            
            // 날짜 수동 입력 변경 시 과거 날짜인지 한 번 더 검증
            if (selectedDate < todayStr) {
                alert("과거 날짜는 예약할 수 없습니다.");
                this.value = "";
                return;
            }
            
            const roomId = document.getElementById("roomId").value;

            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    selectedTimes = []; 
                    timeButtons.forEach(btn => { btn.disabled = false; btn.classList.remove('active'); });
                    
                    clearInterval(timerInterval);
                    document.getElementById("timerBox").style.display = "none";
                    document.getElementById("submitBtn").disabled = true;
                    document.getElementById("holdBtn").style.display = "block";
                    document.getElementById("resNo").value = "";

                    // 현재 시간 구하기 (오늘 날짜를 선택했을 때만 비교하기 위해)
                    const now = new Date();
                    const isToday = (selectedDate === todayStr); 
                    const currentHour = now.getHours();

                    timeButtons.forEach(btn => {
                        const btnTime = btn.getAttribute('data-time');
                        const btnHour = parseInt(btnTime.split(':')[0]);

                        // 1. 이미 다른 사람이 예약한 시간 비활성화
                        if (reservedTimes.includes(btnTime)) {
                            btn.disabled = true;
                        }
                        // 2. 오늘 날짜이면서, 현재 시간보다 과거인 버튼 비활성화
                        else if (isToday && btnHour <= currentHour) {
                            btn.disabled = true;
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        });
    });

    function validateForm() {
        if (!document.getElementById("resNo").value) { alert("예약 시간을 먼저 지정해 주세요."); return false; }
        if (!document.getElementById("purpose").value.trim()) { alert("사용 목적을 기재해 주세요."); return false; }
        return true;
    }
</script>
</head>
<body>

	<div class="reserve-container">
		<h2>
			회의실 예약 신청
			<%
			if ("Y".equals(loginEmp.getManager())) {
			%>
			<button type="button" class="btn-modify"
				onclick="location.href='roomUpdate.do?roomId=<%=roomInfo.getRoomId()%>'">
				정보 수정</button>
			<%
			}
			%>
		</h2>
		<%
		// 방 예약 가능 여부를 변수로 저장
		boolean isRoomEnable = "Y".equals(roomInfo.getEnable());
		%>

		<div class="info-box">
			<h3>
				<%=roomInfo.getRoomName()%>
				<%
				if (isRoomEnable) {
				%>
				<span class="status-badge bg-success"
					style="font-size: 12px; margin-left: 8px;">예약 가능</span>
				<%
				} else {
				%>
				<span class="status-badge bg-danger"
					style="font-size: 12px; margin-left: 8px;">점검 중 (예약 불가)</span>
				<%
				}
				%>
			</h3>
			<p>
				수용 인원: <%=roomInfo.getCapacity()%>명 | 빔프로젝터: <%=roomInfo.getHasBeam()%>
            </p>
			<p><%=roomInfo.getDescription()%></p>
		</div>

		<div class="timer-box" id="timerBox">
			<span id="holdTimeText"
				style="color: #495057; font-size: 14px; font-weight: bold;"></span><br>
			해당 시간대 확보 완료. 남은 시간 내에 확정해 주세요. <span class="timer-text"
				id="timeDisplay">05:00</span>
		</div>

		<form action="reserveProcess.do" method="post"
			onsubmit="return validateForm();">
			<input type="hidden" id="resNo" name="resNo" value=""> <input
				type="hidden" id="roomId" name="roomId"
				value="<%=roomInfo.getRoomId()%>"> <input type="hidden"
				id="empNo" name="empNo" value="<%=loginEmp.getEmpNo()%>">

			<div class="form-group">
				<label>예약 일자</label> <input type="date" id="resDate" name="resDate"
					required>
			</div>

			<div class="form-group">
				<label>예약 시간 선택</label>
				<div class="time-grid" id="timeGrid">
					<%
					// 방이 비활성화 상태면 버튼들에 들어갈 disabled 속성 텍스트 준비
					String disabledAttr = isRoomEnable ? "" : "disabled";
					%>
					<button type="button" class="time-btn" data-time="09:00"
						<%=disabledAttr%>>09:00</button>
					<button type="button" class="time-btn" data-time="10:00"
						<%=disabledAttr%>>10:00</button>
					<button type="button" class="time-btn" data-time="11:00"
						<%=disabledAttr%>>11:00</button>
					<button type="button" class="time-btn" data-time="12:00"
						<%=disabledAttr%>>12:00</button>
					<button type="button" class="time-btn" data-time="13:00"
						<%=disabledAttr%>>13:00</button>
					<button type="button" class="time-btn" data-time="14:00"
						<%=disabledAttr%>>14:00</button>
					<button type="button" class="time-btn" data-time="15:00"
						<%=disabledAttr%>>15:00</button>
					<button type="button" class="time-btn" data-time="16:00"
						<%=disabledAttr%>>16:00</button>
					<button type="button" class="time-btn" data-time="17:00"
						<%=disabledAttr%>>17:00</button>
					<button type="button" class="time-btn" data-time="18:00"
						<%=disabledAttr%>>18:00</button>
				</div>

				<button type="button" class="btn-action btn-hold" id="holdBtn"
					<%=disabledAttr%> <%if (!isRoomEnable) {%>
					style="background-color: #6c757d; cursor: not-allowed;" <%}%>>
					선택 시간 확보</button>
			</div>

			<div class="form-group">
				<label>사용 목적</label> <input type="text" id="purpose" name="purpose"
					placeholder="예: 주간 부서 회의" required>
			</div>

			<div class="btn-group">
				<a href="main.jsp" class="btn-action btn-cancel" style="text-align: center; text-decoration: none; line-height: normal; display: inline-flex; align-items: center; justify-content: center;">취소</a>
				<button type="submit" class="btn-action btn-submit" id="submitBtn"
					disabled>예약 확정</button>
			</div>
		</form>
	</div>

</body>
</html>