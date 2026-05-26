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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
    /* doGet 막은이유 
     * 1. 잘못된 접근 방식 차단 (직접적인 URL 호출 방지) 
     * 	일반적으로 로그인 처리는 doPost를 통해 폼(Form) 데이터를 안전하게 전달받아 수행해야 합니다. 
     * 사용자가 브라우저 주소창에 직접 /login.do를 입력하여 접속하는 경우(GET 방식)는 로그인 정보가 하나도 없는 상태입니다. 
     * 이 상태에서 로그인 로직을 그대로 실행하면 null 값으로 DB 조회를 시도하거나 예외가 발생할 수 있습니다. 
     * 따라서 "로그인 페이지(index.jsp)로 다시 보내서 제대로 로그인하게 만드는 것"이 안전합니다.
     */
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestProcess(request, response);
	}

	protected void requestProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String empNo = request.getParameter("empNo");
        String password = request.getParameter("password");

        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDTO loginEmp = dao.loginCheck(empNo, password);

        if (loginEmp != null) {//로그인성공하면 main.jsp로 이동
            HttpSession session = request.getSession();
            session.setAttribute("loginEmp", loginEmp); 
            response.sendRedirect("main.jsp");         
        } else {//로그인 실패 index.jsp 그대로 유지 
            request.setAttribute("msg", "사원번호 또는 비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }


}
