package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.RentalDAO;
import com.groupware.dao.LeaveDAO; 
import com.groupware.dto.RentalHistoryDTO;
import com.groupware.dto.LeaveHistoryDTO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/documentList.do")
public class DocumentListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 필요한 서비스입니다.');location.href='index.jsp';</script>");
            out.close();
            return;
        }
        
        try {
            int empNo = loginEmp.getEmpNo();
            RentalDAO rentalDao = new RentalDAO();
            LeaveDAO leaveDao = new LeaveDAO();
            
            // --- 페이징 공통 설정 ---
            int pageSize = 10;
            int pageBlock = 5;
            
            // 1. 비품 목록 페이징 처리
            int eqCurrentPage = 1;
            String eqPageParam = request.getParameter("eqPage");
            if (eqPageParam != null && !eqPageParam.isEmpty()) {
                try { eqCurrentPage = Integer.parseInt(eqPageParam); } catch (NumberFormatException e) { eqCurrentPage = 1; }
            }
            int eqStartRow = (eqCurrentPage - 1) * pageSize + 1;
            int eqEndRow = eqCurrentPage * pageSize;
            
            List<RentalHistoryDTO> docList = rentalDao.getAllDocumentListPaging(eqStartRow, eqEndRow);
            int eqTotalCount = rentalDao.getTotalRentalCount();
            int eqTotalPages = (int) Math.ceil((double) eqTotalCount / pageSize);
            int eqStartPage = ((eqCurrentPage - 1) / pageBlock) * pageBlock + 1;
            int eqEndPage = Math.min(eqStartPage + pageBlock - 1, eqTotalPages);
            
            request.setAttribute("docList", docList);
            request.setAttribute("eqCurrentPage", eqCurrentPage);
            request.setAttribute("eqTotalPages", eqTotalPages);
            request.setAttribute("eqStartPage", eqStartPage);
            request.setAttribute("eqEndPage", eqEndPage);
            
            // 2. 휴가 목록 페이징 처리
            int leaveCurrentPage = 1;
            String leavePageParam = request.getParameter("leavePage");
            if (leavePageParam != null && !leavePageParam.isEmpty()) {
                try { leaveCurrentPage = Integer.parseInt(leavePageParam); } catch (NumberFormatException e) { leaveCurrentPage = 1; }
            }
            int leaveStartRow = (leaveCurrentPage - 1) * pageSize + 1;
            int leaveEndRow = leaveCurrentPage * pageSize;
            
            List<LeaveHistoryDTO> leaveList = leaveDao.getAllLeaveDocumentsPaging(leaveStartRow, leaveEndRow);
            int leaveTotalCount = leaveDao.getTotalLeaveCount();
            int leaveTotalPages = (int) Math.ceil((double) leaveTotalCount / pageSize);
            int leaveStartPage = ((leaveCurrentPage - 1) / pageBlock) * pageBlock + 1;
            int leaveEndPage = Math.min(leaveStartPage + pageBlock - 1, leaveTotalPages);
            
            request.setAttribute("leaveList", leaveList);
            request.setAttribute("leaveCurrentPage", leaveCurrentPage);
            request.setAttribute("leaveTotalPages", leaveTotalPages);
            request.setAttribute("leaveStartPage", leaveStartPage);
            request.setAttribute("leaveEndPage", leaveEndPage);
            
            String activeTab = request.getParameter("tab");
            request.setAttribute("activeTab", activeTab); 
            
            request.getRequestDispatcher("documentList.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<script>alert('문서함을 로드하는 중 오류가 발생했습니다.');location.href='main.jsp';</script>");
            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}