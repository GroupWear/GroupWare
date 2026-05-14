package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// ❗ 이 부분이 반드시 있어야 EmployeeDTO를 인식합니다.
import com.groupware.dto.EmployeeDTO; 

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // JSP의 name="empId", name="empPw" 값을 가져옴
        String id = request.getParameter("empId");
        String pw = request.getParameter("empPw");

        // 1. 임시로 DTO 객체 생성 (나중엔 DB에서 조회)
        EmployeeDTO user = new EmployeeDTO();
        user.setEmpNo(1001); 
        user.setEmpPw("1234");
        user.setEmpName("관리자");

        // 2. 로그인 판별 로직
        // String과 int를 비교하기 위해 String.valueOf() 사용
        if (id != null && id.equals(String.valueOf(user.getEmpNo())) && pw != null && pw.equals(user.getEmpPw())) {
            
            // 로그인 성공: 설계도(image_3a3678.png)대로 세션에 DTO 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", user); 
            
            // 메인 대시보드로 이동
            response.sendRedirect("main.jsp");
        } else {
            // 로그인 실패: 다시 로그인 페이지로 (에러 파라미터 포함)
            response.sendRedirect("index.jsp?error=1");
        }
    }
}