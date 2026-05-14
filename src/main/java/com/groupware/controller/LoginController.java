package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.EmployeeDAO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String empNo = request.getParameter("empNo");
        String password = request.getParameter("password");

        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDTO loginEmp = dao.loginCheck(empNo, password);

        if (loginEmp != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginEmp", loginEmp); 
            response.sendRedirect("main.jsp");         
        } else {
            request.setAttribute("msg", "사원번호 또는 비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
