package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.groupware.dao.EmployeeDAO;

@WebServlet("/checkUser.do")
public class CheckUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET 요청 시 사원 확인 페이지(아이디/비밀번호 찾기 페이지 등)로 이동
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("findUser.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String empNoParam = request.getParameter("empNo");
        String empName = request.getParameter("empName");

        if (empNoParam == null || empName == null || empName.trim().isEmpty()) {
            out.println("<script>alert('사원번호와 이름을 모두 입력해주세요.'); history.back();</script>");
            return;
        }

        try {
            int empNo = Integer.parseInt(empNoParam.trim());
            EmployeeDAO empDAO = new EmployeeDAO();
            
            // ★ DAO의 findPassword를 사용해 사번과 이름이 일치하는지 확인 ★
            // 일치하면 DB에 저장된 비밀번호(암호문)가 반환되고, 일치하지 않으면 null이 반환됩니다.
            String dbPw = empDAO.findPassword(empNo, empName.trim());

            if (dbPw != null) {
                // [인증 성공] 사원번호와 이름이 일치함!
                // 다음 페이지(resetPw.jsp)에서 이 사원의 비밀번호를 바꿀 수 있도록 세션이나 request에 사번을 저장합니다.
                HttpSession session = request.getSession();
                session.setAttribute("resetEmpNo", empNo); // 비밀번호 변경 대상자의 사번을 세션에 킵!
                
                // 성공 알림창을 띄우고 비밀번호 재설정 페이지로 이동시킵니다.
                out.println("<script>alert('사원 인증에 성공했습니다. 비밀번호 재설정 페이지로 이동합니다.'); location.href='resetPw.do';</script>");
            } else {
                // [인증 실패] 사번이나 이름이 틀림
                out.println("<script>alert('일치하는 사원 정보가 없습니다. 다시 확인해주세요.'); history.back();</script>");
            }
        } catch (NumberFormatException e) {
            out.println("<script>alert('사원번호는 숫자만 입력 가능합니다.'); history.back();</script>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('서버 에러가 발생했습니다.'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}