package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.groupware.dao.LeaveDAO;
import com.groupware.dto.LeaveHistoryDTO;

@WebServlet("/leaveDetail.do")
public class LeaveDetailController extends HttpServlet {
    private LeaveDAO dao = new LeaveDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. 로그인 체크
        if (session.getAttribute("loginEmp") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. 문서 번호 파라미터 확인
        String leaveNoParam = request.getParameter("leaveNo");
        if (leaveNoParam == null) {
            response.sendRedirect("documentList.do?tab=leave");
            return;
        }

        int leaveNo = Integer.parseInt(leaveNoParam);
        
        // 3. 데이터 조회
        LeaveHistoryDTO leave = dao.getLeaveDetail(leaveNo);
        
        // 4. 결과 전달
        if (leave != null) {
            request.setAttribute("leave", leave);
            // 로그인 유저의 레벨을 미리 세팅 (결재 권한 확인용)
            request.setAttribute("loginUserLevel", ((com.groupware.dto.EmployeeDTO)session.getAttribute("loginEmp")).getManagerLevel());
            request.getRequestDispatcher("/leaveDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("documentList.do?tab=leave");
        }
    }
}