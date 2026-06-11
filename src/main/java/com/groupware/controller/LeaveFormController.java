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
            LeaveDAO dao = new LeaveDAO();
            LeaveHistoryDTO dto = new LeaveHistoryDTO();
            
            dto.setEmpNo(loginEmp.getEmpNo());
            dto.setStartDate(java.sql.Date.valueOf(start));
            dto.setEndDate(java.sql.Date.valueOf(end));
            dto.setUseDays(days);
            dto.setReason(reason);
            dto.setEmpLevel(loginEmp.getEmpLevel());

            // [통합 로직] 신청자 레벨에 따른 자동 승인 및 본인 도장 날인
            int userLevel = loginEmp.getEmpLevel();
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            if (userLevel == 5) {
                // 5단계 신청 시 즉시 승인 완료
                dto.setStatus("승인완료");
                dto.setApprovalStep(6);
                dto.setSign5(loginEmp.getEmpName());
                dto.setSign5Date(today);
            } else {
                // 1~4단계 신청 시 승인 대기 및 본인 도장 자동 날인
                dto.setStatus("승인대기");
                dto.setApprovalStep(userLevel + 1);
                
                if (userLevel == 1) { dto.setSign1(loginEmp.getEmpName()); dto.setSign1Date(today); }
                else if (userLevel == 2) { dto.setSign2(loginEmp.getEmpName()); dto.setSign2Date(today); }
                else if (userLevel == 3) { dto.setSign3(loginEmp.getEmpName()); dto.setSign3Date(today); }
                else if (userLevel == 4) { dto.setSign4(loginEmp.getEmpName()); dto.setSign4Date(today); }
            }

            // DB 저장 (DAO에서 insertLeave 메서드가 위 필드들을 다 받도록 설정되어 있어야 함)
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