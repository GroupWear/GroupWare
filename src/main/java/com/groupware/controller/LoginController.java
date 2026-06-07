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
import com.groupware.util.CryptoUtil;

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
        
        // 혹시 모를 공백 방지를 위해 trim() 처리 (선택 사항)
        if(empNo != null) empNo = empNo.trim();
        if(password != null) password = password.trim();
        
        // ★ 2. 사용자가 입력한 평문 비밀번호를 가입할 때와 '동일한 키'로 암호화합니다.
        // 다른 곳에서도 이와 같이 값 하나만 넣어주면 즉시 호출하여 동일한 암호문을 얻을 수 있습니다.
        String encryptedPassword = CryptoUtil.encrypt(password);
        
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeDTO loginEmp = dao.loginCheck(empNo, encryptedPassword);

        boolean isLegacyUser = false; // "이 사람이 옛날 평문 유저인가?"를 체크할 센서
        
        // 2. 만약 신규 암호문으로 로그인이 실패했다면, 옛날 평문 유저인지 한 번 더 확인합니다.
        if (loginEmp == null) {
            // 입력받은 날것 그대로의 평문 비밀번호로 조회를 시도합니다 (과거 가입자용)
            loginEmp = dao.loginCheck(empNo, password);
            
            if (loginEmp != null) {
                // 날것의 비밀번호로 로그인이 성공했다면? -> "아! 이 사람은 옛날 평문 유저구나!" 라고 판단합니다.
                isLegacyUser = true; 
            }
        }
        
        
//        if (loginEmp != null) {//로그인성공하면 main.jsp로 이동
//            HttpSession session = request.getSession();
//            session.setAttribute("loginEmp", loginEmp); 
//            response.sendRedirect("main.jsp");         
//        } else {//로그인 실패 index.jsp 그대로 유지 
//            request.setAttribute("msg", "사원번호 또는 비밀번호가 일치하지 않습니다.");
//            request.getRequestDispatcher("index.jsp").forward(request, response);
//        }
        
        // 3. 최종적으로 로그인이 성공했는지 확인합니다. (신규 유저든, 옛날 유저든 둘 중 하나라도 성공했다면 들어옴)
        if (loginEmp != null) {
            
            // ★★★ [이 부분이 핵심 질문 답변: 자동 암호화 업그레이드] ★★★
            if (isLegacyUser) {
                // 옛날 평문 유저임이 확인되었으니, 방금 입력한 따끈따끈한 비밀번호를 암호문으로 변환합니다.
                String newEncryptedPassword = CryptoUtil.encrypt(password);
                
                // DB에 접근하여 이 사원의 옛날 비밀번호를 방금 만든 강력한 암호문으로 교체(업데이트)합니다.
                boolean isUpdateSuccess = dao.updateEmployeePassword(empNo, newEncryptedPassword);
                
                if (isUpdateSuccess) {
                    System.out.println("[보안 알림] 사번 " + empNo + " 사원의 구형 평문 비밀번호가 신형 암호문으로 자동 업그레이드 되었습니다!");
                }
            }
            
            // 4. 원래 하던 대로 세션에 로그인 정보를 굽고 메인 페이지로 이동시킵니다.
            HttpSession session = request.getSession();
            session.setAttribute("loginEmp", loginEmp); 
            response.sendRedirect("main.jsp");         
            
        } else { 
            // 로그인 실패 (사번이 없거나 비밀번호가 진짜로 틀린 경우)
            request.setAttribute("msg", "사원번호 또는 비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
