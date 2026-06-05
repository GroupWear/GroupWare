package com.groupware.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.LeaveHistoryDTO;
import com.groupware.dao.LeaveDAO;

@WebServlet("/leaveForm.do")
public class LeaveFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8"); 
        response.setContentType("text/html; charset=UTF-8");
        request.getRequestDispatcher("leaveForm.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) { response.sendRedirect("index.jsp"); return; }
        
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");
        String reason = "직접입력".equals(request.getParameter("reasonCategory")) ? 
                        request.getParameter("reason") : request.getParameter("reasonCategory");

        int days = calculateBusinessDays(start, end);
        
        if (days <= 0) {
            sendAlert(response, "신청하신 기간 내에 평일이 없습니다.", "leaveForm.do");
        } else if (days > loginEmp.getCurLeave()) {
            sendAlert(response, "연차가 부족합니다.", "leaveForm.do");
        } else {
            //서비스 객체 없이 DAO를 바로 호출
            LeaveDAO dao = new LeaveDAO();
            LeaveHistoryDTO dto = new LeaveHistoryDTO();
            
            dto.setEmpNo(loginEmp.getEmpNo());
            dto.setStartDate(java.sql.Date.valueOf(start));
            dto.setEndDate(java.sql.Date.valueOf(end));
            dto.setUseDays(days);
            dto.setReason(reason);
            dto.setStatus("승인대기"); // 초기 상태 설정
            dto.setApprovalStep(1);   // 첫 번째 단계
            
            boolean result = dao.insertLeave(dto);
            
            if (result) {
                response.sendRedirect("documentList.do?tab=leave");
            } else {
                sendAlert(response, "휴가 신청 중 오류가 발생했습니다.", "leaveForm.do");
            }
        }
    }
    private int calculateBusinessDays(String start, String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        return (int) Stream.iterate(s, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(s, e) + 1)
                .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }

    private void sendAlert(HttpServletResponse response, String msg, String url) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<script>alert('" + msg + "'); location.href='" + url + "';</script>");
    }
}