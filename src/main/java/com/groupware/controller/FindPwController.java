package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.groupware.dao.EmployeeDAO;

@WebServlet("/findPw.do")
public class FindPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 비밀번호 찾기 페이지 폼 열기 (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("findPw.jsp").forward(request, response);
    }

    // 폼 데이터 전송 받아 처리하기 (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String empNoParam = request.getParameter("empNo");
        String empName = request.getParameter("empName");
        
        PrintWriter out = response.getWriter();
        
        // 1차 공백 및 누락 검증
        if (empNoParam == null || empNoParam.trim().isEmpty() || empName == null || empName.trim().isEmpty()) {
            out.println("<script>");
            out.println("alert('사원번호와 이름을 모두 입력해주세요.');");
            out.println("history.back();");
            out.println("</script>");
            return;
        }

        try {
            // 1. 기존 DAO 메서드 스펙(int)에 맞춰 타입 변환
            int empNo = Integer.parseInt(empNoParam.trim());
            String empNameTrimmed = empName.trim();

            EmployeeDAO empDAO = new EmployeeDAO();
            
            // 2. 건드리지 않은 순수 원본 findPassword(int, String) 메서드 호출
            String foundPassword = empDAO.findPassword(empNo, empNameTrimmed);
            
            if (foundPassword != null) {
                // 퇴사자 계정 잠금 처리 정책 연동 ('RETIRED' 문자열 검증)
                if ("RETIRED".equals(foundPassword)) {
                    out.println("<script>");
                    out.println("alert('퇴사 처리된 사원 계정입니다. 관리자에게 문의하세요.');");
                    out.println("history.back();");
                    out.println("</script>");
                } else {
                    // [성공] 비밀번호 팝업 알림
                    out.println("<script>");
                    out.println("alert('" + empNameTrimmed + "님의 비밀번호는 [" + foundPassword + "] 입니다.');");
                    out.println("location.href='index.jsp';"); // 확인 후 로그인 메인 페이지로 이동
                    out.println("</script>");
                }
            } else {
                // [실패] 일치하는 사원 없음
                out.println("<script>");
                out.println("alert('일치하는 사원 정보가 존재하지 않습니다. 다시 확인해주세요.');");
                out.println("history.back();");
                out.println("</script>");
            }

        } catch (NumberFormatException e) {
            // 사번 입력칸에 문자가 들어와 형변환 에러가 날 경우 방어 코드
            out.println("<script>");
            out.println("alert('사원번호는 숫자만 입력 가능합니다.');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}