package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.groupware.dao.EmployeeDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.util.CryptoUtil;

@WebServlet("/changePw.do")
public class ChangePwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET 요청 시 비밀번호 변경 페이지(JSP)를 보여줍니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("changePw.jsp").forward(request, response);
    }
    // POST 요청 시(폼 제출 시) 실제 비밀번호 변경 로직을 처리합니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	// 한글 깨짐 방지를 위해 인코딩을 설정합니다.
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        // 현재 로그인한 사용자의 정보를 세션에서 가져옵니다.
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        // 비밀번호 찾기 후 넘어오는 경우(비로그인 상태)를 위해 파라미터도 확인합니다.
        String empNoParam = request.getParameter("empNo"); 
        
        int empNo = 0;
        boolean isLoginMode = false; // 로그인 상태인지 구분하는 플래그
        
        if (loginEmp != null) {
            empNo = loginEmp.getEmpNo();
            isLoginMode = true; // 로그인 상태
        } else if (empNoParam != null) {
            empNo = Integer.parseInt(empNoParam);
        } else {
            out.println("<script>alert('접근 권한이 없습니다.'); location.href='index.jsp';</script>");
            return;
        }

        String newPw = request.getParameter("newPw").trim();
        String newPwConfirm = request.getParameter("newPwConfirm").trim();

        if (!newPw.equals(newPwConfirm)) {
            out.println("<script>alert('새 비밀번호가 일치하지 않습니다.'); history.back();</script>");
            return;
        }

        try {
            EmployeeDAO empDAO = new EmployeeDAO();
            
            // [마이페이지 모드일 때만 현재 비밀번호 검증]
            if (isLoginMode) {
                String currentPw = request.getParameter("currentPw").trim();
                EmployeeDTO dto = empDAO.getEmployeeByNo(String.valueOf(empNo));
                String decryptedPassword = CryptoUtil.decrypt(dto.getEmpPw().trim()).trim();
                
                if (!currentPw.equals(decryptedPassword)) {
                    out.println("<script>alert('현재 비밀번호가 일치하지 않습니다.'); history.back();</script>");
                    return;
                }
            }

            // DB 업데이트
            String encryptedPassword = CryptoUtil.encrypt(newPw);
            if (empDAO.updatePassword(empNo, encryptedPassword)) {
                session.invalidate(); 
                out.println("<script>alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.'); location.href='index.jsp';</script>");
            } else {
                out.println("<script>alert('비밀번호 변경에 실패했습니다.'); history.back();</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('서버 오류가 발생했습니다.'); history.back();</script>");
        } finally {
            out.close();
        }
    }
}