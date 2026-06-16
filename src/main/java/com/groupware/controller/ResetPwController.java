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
import com.groupware.util.CryptoUtil;

@WebServlet("/resetPw.do")
public class ResetPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empNoParam = request.getParameter("empNo");
        if (empNoParam != null && !empNoParam.trim().isEmpty() && !empNoParam.equals("null")) {
            request.getSession().setAttribute("resetEmpNo", Integer.parseInt(empNoParam.trim()));
        }
        request.getRequestDispatcher("resetPw.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Object sessionEmpNo = session.getAttribute("resetEmpNo");
        String empNoParam = request.getParameter("empNo");
        
        String newPw = request.getParameter("newPw").trim();
        String newPwConfirm = request.getParameter("newPwConfirm").trim();

        if (!newPw.equals(newPwConfirm)) {
            out.println("<script>alert('パスワードが一致しません。'); history.back();</script>");
            return;
        }

        try {
            int empNo = 0;
            if (empNoParam != null && !empNoParam.trim().isEmpty() && !empNoParam.equals("null")) {
                empNo = Integer.parseInt(empNoParam.trim());
            } else if (sessionEmpNo != null) {
                empNo = (Integer) sessionEmpNo;
            }

            if (empNo == 0) {
                out.println("<script>alert('ユーザー情報が見つかりません。最初からやり直してください。'); location.href='index.jsp';</script>");
                return;
            }

            EmployeeDAO empDAO = new EmployeeDAO();
            String encryptedPassword = CryptoUtil.encrypt(newPw);
            
            // 기존 비밀번호 중복 검증
            if (empDAO.updatePasswordWithVerify(empNo, encryptedPassword, encryptedPassword)) {
                out.println("<script>alert('現在のパスワードと同じパスワードはご利用いただけません。'); location.href='resetPw.do?empNo=" + empNo + "';</script>");
                return; 
            }
            
            // 비밀번호 업데이트 실행
            if (empDAO.updatePassword(empNo, encryptedPassword)) {
                session.removeAttribute("resetEmpNo"); // 성공 시 세션 파기
                out.println("<script>alert('パスワードが変更されました。もう一度ログインしてください。'); location.href='index.jsp';</script>");
            } else {
                out.println("<script>alert('変更に失敗しました。'); history.back();</script>");
            }
            
        } catch (Exception e) {
            e.printStackTrace(); 
            out.println("<script>alert('サーバーエラーが発生しました。'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}