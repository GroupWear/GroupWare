package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.groupware.dao.EmployeeDAO;

/**
 * [JoinController]
 * 역할: 가입 폼에서 입력받은 비밀번호를 실제 DB에 업데이트하여 계정 등록을 완료함
 */
@WebServlet("/join.do")
public class JoinController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 데이터 인코딩 설정 (한글 깨짐 방지)
        request.setCharacterEncoding("UTF-8");

        // join.jsp에서 전달된 파라미터 수집 (trim으로 공백 제거)
        String empNo = request.getParameter("empNo").trim();
        String password = request.getParameter("password").trim();

        EmployeeDAO dao = new EmployeeDAO();
        
        // ★ 중요: 보내주신 DAO 코드에 정의된 메서드명 'updateEmployeePassword'를 사용해야 함
        // 인자 타입은 둘 다 String이므로 그대로 전달
        boolean isSuccess = dao.updateEmployeePassword(empNo, password);

        response.setContentType("text/html; charset=UTF-8");
        if (isSuccess) {
            // 가입 성공 시 로그인 페이지로 안내
            response.getWriter().println("<script>");
            response.getWriter().println("alert('반갑습니다! 계정 등록이 완료되었습니다. 로그인 해주세요.');");
            response.getWriter().println("location.href='index.jsp';");
            response.getWriter().println("</script>");
        } else {
            // 가입 실패 시 이전 화면으로 이동
            response.getWriter().println("<script>");
            response.getWriter().println("alert('등록 실패: 관리자에게 문의하거나 사번을 다시 확인하세요.');");
            response.getWriter().println("history.back();");
            response.getWriter().println("</script>");
        }
    }
}