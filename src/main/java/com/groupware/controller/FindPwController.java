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

@WebServlet("/findPw.do")
public class FindPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("findPw.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String empNoParam = request.getParameter("empNo");
        String empName = request.getParameter("empName");
        
        PrintWriter out = response.getWriter();
        
        if (empNoParam == null || empNoParam.trim().isEmpty() || empName == null || empName.trim().isEmpty()) {
            out.println("<script>alert('社員番号と名前をすべて入力してください。'); history.back();</script>");
            return;
        }

        try {
            int empNo = Integer.parseInt(empNoParam.trim());
            String empNameTrimmed = empName.trim();

            EmployeeDAO empDAO = new EmployeeDAO();
            String foundPassword = empDAO.findPassword(empNo, empNameTrimmed);
            
            if (foundPassword != null) {
                if ("RETIRED".equals(foundPassword)) {
                    out.println("<script>");
                    out.println("alert('退職処理された社員アカウントです。 管理者にお問い合わせください。');");
                    out.println("history.back();");
                    out.println("</script>");
                } else {
                    // [핵심] 세션에 사번 저장하여 다음 단계로 토스
                    HttpSession session = request.getSession();
                    session.setAttribute("resetEmpNo", empNo);

                    out.println("<script>");
                    out.println("alert('本人確認が完了いたしました。 パスワードを新たに設定してください。');");
                    // .jsp가 아니라 .do 서블릿으로 안전하게 이동
                    out.println("location.href='resetPw.do?empNo=" + empNo + "';"); 
                    out.println("</script>");
                }
            } else {
                out.println("<script>");
                out.println("alert('一致する 社員情報は存在しません。 もう一度確認をお願いします。');");
                out.println("history.back();");
                out.println("</script>");
            }

        } catch (NumberFormatException e) {
            out.println("<script>alert('社員番号は数字のみ入力可能です。'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}