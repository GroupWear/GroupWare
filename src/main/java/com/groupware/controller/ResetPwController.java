package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.groupware.dao.EmployeeDAO;
import com.groupware.util.CryptoUtil;

@WebServlet("/resetPw.do")
public class ResetPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("resetPw.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String empNoParam = request.getParameter("empNo");
        String newPw = request.getParameter("newPw").trim();
        String newPwConfirm = request.getParameter("newPwConfirm").trim();

        if (empNoParam == null || !newPw.equals(newPwConfirm)) {
            out.println("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
            return;
        }

        try {
            int empNo = Integer.parseInt(empNoParam);
            EmployeeDAO empDAO = new EmployeeDAO();
            //암호화
            String encryptedPassword = CryptoUtil.encrypt(newPw);

            if (empDAO.updatePassword(empNo, encryptedPassword)) {
                out.println("<script>alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.'); location.href='index.jsp';</script>");
            } else {
                out.println("<script>alert('변경 실패.'); history.back();</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('서버 오류.'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}