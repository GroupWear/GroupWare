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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("changePw.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        // LoginController에서 저장한 "loginEmp" 객체로 세션 검증
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            out.println("<script>alert('로그인이 필요합니다.'); location.href='index.jsp';</script>");
            return;
        }
        
        int empNo = loginEmp.getEmpNo();
        String currentPw = request.getParameter("currentPw").trim();
        String newPw = request.getParameter("newPw").trim();
        String newPwConfirm = request.getParameter("newPwConfirm").trim();
        

       
        //Int -> String 사번 Int형에서 String으로 변환 LHS
        String empNO_String = String.valueOf(empNo);
        //DB에 있는 PW 복호화 작업을 통해 사용자가 입력한 pw 일치 여부 확인
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDTO dto = dao.getEmployeeByNo(empNO_String);
//      System.out.println("데이터 TEST : "+dto.getEmpPw());
        
        //현재 데이터 상에 있는 PW 복호화 작업 및 사용자가 입력한 pw 일치하는지 확인
        String decryptedPassword = CryptoUtil.decrypt(dto.getEmpPw().trim()).trim();
        String encrypted_present_Password = CryptoUtil.encrypt(currentPw).trim();
//        System.out.println("복호화 비번 : "+decryptedPassword);
//        System.out.println("현재  비번 : "+encrypted_present_Password);
//        System.out.println("현재  비번 : "+dto.getEmpPw().trim());
//      System.out.println("비번 일치여부 : "+currentPw.equals(decryptedPassword));


         
        // 새 비밀번호 일치 확인
        if (!currentPw.equals(decryptedPassword)) {
        	out.println("<script>alert('현재 비밀번호가 일치하지 않습니다.'); history.back();</script>");
        	return;
        }
        
        // 새 비밀번호 일치 확인
        if (!newPw.equals(newPwConfirm)) {
        	out.println("<script>alert('새 비밀번호가 일치하지 않습니다.'); history.back();</script>");
            return;
        }


        try {
            EmployeeDAO empDAO = new EmployeeDAO();
            
          //현재 데이터 상에 있는 PW 복호화 작업 및 사용자가 입력한 pw 일치하는지 확인
            String encryptedPassword = CryptoUtil.encrypt(newPw);
            
            // [수정 및 검증] 기존 원본 구조를 유지하며 자바 로직과 스크립트 분리
            if (empDAO.updatePasswordWithVerify(empNo, encrypted_present_Password, encryptedPassword)) {
                
                /* * ★ 핵심 수정 구역 ★
                 * 자바에서 무효화 처리를 먼저 명확히 실행한 후, 
                 * out.println 스크립트를 통해 index.jsp로 완전히 리다이렉트 시킵니다.
                 */
                session.invalidate(); // 1. 서버 세션 완전 삭제 (자바 코드 실행)
                
                out.println("<script>");
                out.println("alert('비밀번호 변경 완료. 다시 로그인해주세요.');");
                out.println("location.href='index.jsp';"); // 2. 로그인 페이지로 강제 이동
                out.println("</script>");
                
            } else {
                out.println("<script>alert('현재 비밀번호가 틀렸습니다.'); history.back();</script>");
            }
        } catch (Exception e) {
            out.println("<script>alert('서버 오류가 발생했습니다.'); history.back();</script>");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}