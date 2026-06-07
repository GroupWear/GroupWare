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
            
            // 1. 비품 목록 로드
            List<RentalHistoryDTO> docList = rentalDao.getAllDocumentList(); 
            request.setAttribute("docList", docList); 
            
            // 2. [수정 포인트] 휴가 목록 로드 (디버깅 추가)
            List<LeaveHistoryDTO> leaveList = new ArrayList<>();
            try {
                LeaveDAO leaveDao = new LeaveDAO();
                // getMyLeaveList(empNo) 대신 getAllLeaveDocuments() 사용
                leaveList = leaveDao.getAllLeaveDocuments(); 
                
                System.out.println("디버그: 시스템 내 전체 휴가 신청 건수 -> " + (leaveList != null ? leaveList.size() : "null"));
                
            } catch (Exception e) {
                e.printStackTrace(); 
            }
            request.setAttribute("leaveList", leaveList);
            
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