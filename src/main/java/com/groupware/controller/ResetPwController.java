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

        // 1. 입력 폼 검증 (비밀번호 불일치 방지)
        if (empNoParam == null || !newPw.equals(newPwConfirm)) {
            out.println("<script>alert('パスワードが一致しません。'); history.back();</script>");
            return;
        }

        try {
            int empNo = Integer.parseInt(empNoParam);
            EmployeeDAO empDAO = new EmployeeDAO();
            
            // 사용자가 새로 입력한 비밀번호 암호화 가공
            String encryptedNewPassword = CryptoUtil.encrypt(newPw);

            // ★ 2. 기존 비밀번호 중복 검증 알고리즘 실행 ★
            if (empDAO.updatePasswordWithVerify(empNo, encryptedNewPassword, encryptedNewPassword)) {
                // [수정] "현재 비밀번호와 동일한 비밀번호는 사용하실 수 없습니다."
                out.println("<script>alert('現在のパスワードと同じパスワードはご利用いただけません。'); location.href='resetPw.do';</script>");
                return; 
            }

            // 3. 중복이 아니라 진짜 새로운 비밀번호라면 정석대로 업데이트 수행
            if (empDAO.updatePassword(empNo, encryptedNewPassword)) {
                out.println("<script>alert('パスワードが変更されました。もう一度ログインしてください。'); location.href='index.jsp';</script>");
            } else {
                // [수정] "변경에 실패했습니다."
                out.println("<script>alert('変更に失敗しました。'); history.back();</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // [수정] "サーバーエラーが発生しました。"
            out.println("<script>alert('サーバーエラーが発生しました。'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}