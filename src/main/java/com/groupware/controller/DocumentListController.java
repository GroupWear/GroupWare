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
            
            // 📌 [변경 포인트] 기존 getMyRentalList(empNo) 대신 전체 목록을 뽑아오는 메서드로 교체합니다.
            // 이 메서드는 RentalDAO 내부에서 3개 테이블(RENTAL_HISTORY, EMPLOYEE, EQUIPMENT)을 
            // LEFT JOIN 하여 기안자 이름(EMP_NAME)까지 한 번에 수집해 옵니다.
            List<RentalHistoryDTO> docList = rentalDao.getAllDocumentList(); 
            request.setAttribute("docList", docList); 
            
            // 휴가 신청 리스트는 기존 본인 조회 규칙 유지 (터짐 방지용 가드 포함)
            List<LeaveHistoryDTO> leaveList = new ArrayList<>();
            try {
                LeaveDAO leaveDao = new LeaveDAO();
                leaveList = leaveDao.getMyLeaveList(empNo);
            } catch (Exception e) {
                System.out.println("LeaveDAO 메서드가 없는 상태이므로 빈 리스트 대체");
            }
            request.setAttribute("leaveList", leaveList);
            
            // 📌 [신규 추가]: 돌아갈 탭 파라미터를 받아서 존재하면 request에 심어 JSP로 보냅니다.
            String activeTab = request.getParameter("tab");
            request.setAttribute("activeTab", activeTab); 
            
            // 데이터 탑재 후 기안 문서함 화면(JSP)으로 포워딩
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