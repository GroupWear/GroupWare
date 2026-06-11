package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.groupware.dao.LeaveDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.LeaveHistoryDTO;

@WebServlet("/leaveApproveProcess.do")
public class LeaveApproveProcessController extends HttpServlet {
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

        // [기능 1] 휴가 신청 (insert)
        if ("insert".equals(action)) {
            LeaveHistoryDTO leave = new LeaveHistoryDTO();
            leave.setEmpNo(loginEmp.getEmpNo());
            
            int userLevel = loginEmp.getEmpLevel(); // 기안자 레벨
            leave.setEmpLevel(userLevel);
            
            // 공통 데이터 바인딩
            leave.setStartDate(java.sql.Date.valueOf(request.getParameter("startDate")));
            leave.setEndDate(java.sql.Date.valueOf(request.getParameter("endDate")));
            leave.setUseDays(Integer.parseInt(request.getParameter("useDays")));
            leave.setReason(request.getParameter("reason"));
            
            // ---------------------------------------------------------------
            // ⭐️ [신청자 자동 사인 & 결재 단계 설정]
            // ---------------------------------------------------------------
            String managerName = loginEmp.getEmpName();
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            // 1. 기안자 본인 직급에 해당하는 결재란에 즉시 자동 서명
            if (userLevel == 1) { leave.setSign1(managerName); leave.setSign1Date(today); }
            else if (userLevel == 2) { leave.setSign2(managerName); leave.setSign2Date(today); }
            else if (userLevel == 3) { leave.setSign3(managerName); leave.setSign3Date(today); }
            else if (userLevel == 4) { leave.setSign4(managerName); leave.setSign4Date(today); }
            else if (userLevel == 5) { leave.setSign5(managerName); leave.setSign5Date(today); }

            // 2. 다음 결재 단계 지정 (기존 설정값 반영: 기안자 레벨 + 1)
            int nextStep = userLevel + 1;
            leave.setApprovalStep(nextStep);

            // 3. ⭐ 5레벨이 신청하여 다음 단계가 6이 된 경우 최종 승인 처리
            if (nextStep >= 6) {
                leave.setStatus("승인완료"); // 5레벨 신청시 바로 완료!
            } else {
                leave.setStatus("승인대기"); // 그 외엔 다음 레벨 결재자 대기
            }
            // ---------------------------------------------------------------
            
            // DB 저장 진행
            dao.insertLeave(leave);
            response.sendRedirect("documentList.do?tab=leave");
        }
        // [기능 2] 결재 승인/반려 처리
        else {
            int leaveNo = Integer.parseInt(request.getParameter("leaveNo"));
            int step = Integer.parseInt(request.getParameter("step"));
            String managerName = loginEmp.getEmpName(); 
            boolean isApprove = "approve".equals(action);
            
            dao.processApproval(leaveNo, step, managerName, isApprove);
            
            response.sendRedirect("leaveDetail.do?leaveNo=" + leaveNo);
        }
    }
}