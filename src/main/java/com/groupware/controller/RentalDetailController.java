package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.RentalDAO;
import com.groupware.dto.RentalHistoryDTO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/rentalDetail.do")
public class RentalDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // 로그인 체크
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        if (loginEmp == null) {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('ログインが必要なサービスです。');location.href='index.jsp';</script>");
            out.close();
            return;
        }
        
        // 파라미터 수령
        String rentalNoStr = request.getParameter("rentalNo");
        if (rentalNoStr == null || rentalNoStr.trim().isEmpty()) {
            response.sendRedirect("documentList.do");
            return;
        }
        
        try {
            int rentalNo = Integer.parseInt(rentalNoStr);
            RentalDAO rentalDao = new RentalDAO();
            
            // DAO에서 단건 세부 내역 조회
            RentalHistoryDTO rentalDetail = rentalDao.getDocumentDetail(rentalNo);
            
            if (rentalDetail == null) {
                PrintWriter out = response.getWriter();
                out.println("<script>alert('存在しないか削除された起案文書です。');location.href='documentList.do';</script>");
                out.close();
                return;
            }
            
            // 데이터를 request에 바인딩 후 세부 화면 JSP로 포워딩
            request.setAttribute("detail", rentalDetail);
            request.getRequestDispatcher("rentalDetail.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<script>alert('詳細表示を取得中にエラーが発生しました。');location.href='documentList.do';</script>");
            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}