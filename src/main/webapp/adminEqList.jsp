<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.groupware.dto.EmployeeDTO"%>

<%
	EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
	if (loginEmp == null) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 재고 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/adminEqList.css">
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>공용 비품 마스터 데이터 관리</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>
    
    <div class="insert-box">
        <h3>신규 비품 등록</h3>
        <form action="insertEq.do" method="post" style="display: flex; gap: 10px; flex: 1;">
            <input type="text" name="eqName" placeholder="비품 명칭 입력" required style="flex: 1;">
            <input type="number" name="totalCount" placeholder="초기 총 수량" required style="width: 120px;">
            <button type="submit" class="btn-submit">DB 등록</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>관리 번호</th>
                    <th>비품 명칭</th>
                    <th>보유 총 수량</th>
                    <th>대여 가능 수량</th>
                    <th>데이터 관리</th>
                </tr>
            </thead>
            <tbody>
                <%-- 
                1. 재고 수량 확인 ( SELECT )
                2. 정보 수정 통해 ( UPDATE )
                3. 영구 폐기를 통해 ( DELETE )
                
                
                
                <% if (eqList != null && !eqList.isEmpty()) {
                    for (EquipmentDTO eq : eqList) { 
                %>
                <tr>
                    <td style="color: #6c757d;"><%= eq.getEqNo() %></td>
                    <td style="font-weight: 600; color: #343a40;"><%= eq.getEqName() %></td>
                    <td><%= eq.getTotalCount() %> EA</td>
                    <td><strong style="color: <%= eq.getRemainCount() > 0 ? "#212529" : "#dc3545" %>;"><%= eq.getRemainCount() %> EA</strong></td>
                    <td>
                        <button class="btn-edit" onclick="openUpdateModal('<%= eq.getEqNo() %>', '<%= eq.getEqName() %>', '<%= eq.getTotalCount() %>', '<%= eq.getRemainCount() %>')">정보 수정</button>
                        <button class="btn-del" onclick="deleteEquipment('<%= eq.getEqNo() %>')">영구 폐기</button>
                    </td>
                </tr>
                <%  } } else { %>
                <tr><td colspan="5" style="padding: 40px; color: #6c757d;">시스템에 등록된 비품 마스터 데이터가 없습니다.</td></tr>
                <% } %> --%>
            </tbody>
        </table>
    </div>
</div>

<div class="modal-overlay" id="modalOverlay" onclick="closeUpdateModal()"></div>
<div id="updateModal">
    <h3>비품 정보 수정</h3>
    <form action="updateEq.do" method="post">
        <input type="hidden" name="eqNo" id="upEqNo">
        <div class="update-group">
            <label>비품 명칭</label>
            <input type="text" name="eqName" id="upEqName" required>
        </div>
        <div class="update-group">
            <label>보유 총 수량</label>
            <input type="number" name="totalCount" id="upTotalCount" required>
        </div>
        <div class="update-group">
            <label>대여 가능 잔여 수량</label>
            <input type="number" name="remainCount" id="upRemainCount" required>
        </div>
        <div class="modal-btn-group">
            <button type="button" class="btn-modal-cancel" onclick="closeUpdateModal()">취소</button>
            <button type="submit" class="btn-modal-submit">수정 반영</button>
        </div>
    </form>
</div>

<form id="deleteForm" action="deleteEq.do" method="post">
    <input type="hidden" name="eqNo" id="delEqNo">
</form>

<script>
    function deleteEquipment(eqNo) {
        if (confirm("해당 비품 데이터를 시스템에서 영구 삭제하시겠습니까?\n(경고: 현재 사원이 대여 중인 비품은 삭제할 수 없습니다)")) {
            document.getElementById("delEqNo").value = eqNo;
            document.getElementById("deleteForm").submit();
        }
    }

    function openUpdateModal(no, name, total, remain) {
        document.getElementById("upEqNo").value = no;
        document.getElementById("upEqName").value = name;
        document.getElementById("upTotalCount").value = total;
        document.getElementById("upRemainCount").value = remain;
        
        document.getElementById("updateModal").style.display = "block";
        document.getElementById("modalOverlay").style.display = "block";
    }

    function closeUpdateModal() {
        document.getElementById("updateModal").style.display = "none";
        document.getElementById("modalOverlay").style.display = "none";
    }
</script>

</body>
</html>