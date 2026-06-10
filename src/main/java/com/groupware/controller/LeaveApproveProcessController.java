package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.groupware.dao.LeaveDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.LeaveHistoryDTO;

@WebServlet("/leaveApproveProcess.do")
public class LeaveApproveProcessController extends HttpServlet { // HttpServlet 상속 추가
    private LeaveDAO dao = new LeaveDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");

        // [기능 1] 신규 휴가 신청
        if ("insert".equals(action)) { // action으로 구분
            LeaveHistoryDTO leave = new LeaveHistoryDTO();
            leave.setEmpNo(loginEmp.getEmpNo());
            leave.setStartDate(java.sql.Date.valueOf(request.getParameter("startDate")));
            leave.setEndDate(java.sql.Date.valueOf(request.getParameter("endDate")));
            leave.setUseDays(Integer.parseInt(request.getParameter("useDays"))); // 계산된 일수 포함
            leave.setReason(request.getParameter("reason"));
            leave.setStatus("승인대기");
            leave.setApprovalStep(1); // 1단계부터 시작
            
            dao.insertLeave(leave);
            response.sendRedirect("documentList.do?tab=leave");
        } 
        // [기능 2] 결재 승인/반려 처리
        else {
            int leaveNo = Integer.parseInt(request.getParameter("leaveNo"));
            int step = Integer.parseInt(request.getParameter("step"));
            String managerName = loginEmp.getEmpName(); // 결재자 이름
            boolean isApprove = "approve".equals(action);
            
            // DAO의 processApproval 메서드 호출
            dao.processApproval(leaveNo, step, managerName, isApprove);
            
            response.sendRedirect("leaveDetail.do?leaveNo=" + leaveNo);
        }
    }
}