package com.smart.controller; // 1. 패키지 경로 확인

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smart.model.EmployeeDTO; // 2. EmployeeDTO 경로에 맞춰 수정 필수 [cite: 21, 22]

@WebServlet("/JoinController")
public class JoinController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글 깨짐 방지 설정
        request.setCharacterEncoding("UTF-8");

        // 1. DTO 객체 생성 및 값 채우기 [cite: 3, 4]
        EmployeeDTO newEmp = new EmployeeDTO();
        
        try {
            // JSP의 input name값들을 가져와 DTO에 저장
            // 사번(empId)이 숫자 타입(int)인 경우를 가정하여 처리 [cite: 25, 26]
            String empIdStr = request.getParameter("empId");
            if (empIdStr != null && !empIdStr.isEmpty()) {
                newEmp.setEmpNo(Integer.parseInt(empIdStr));
            }

            newEmp.setEmpPw(request.getParameter("empPw"));
            newEmp.setEmpName(request.getParameter("empName"));

            // 설계도에 따른 기본값 설정 [cite: 13, 75]
            newEmp.setManager("N"); // 일반 사용자 기본값
            newEmp.setRetired("N"); // 재직 상태 기본값

            // 2. 가입 처리 로직 (이후 DAO와 연동하여 DB에 저장) [cite: 52, 62]
            // 예시: int result = employeeDAO.insertEmployee(newEmp);
            
            // 콘솔 출력으로 데이터 확인
            System.out.println("가입 시도 - 사번: " + newEmp.getEmpNo() + ", 이름: " + newEmp.getEmpName());

            // 3. 완료 알림 후 이동
            // 성공 시 로그인 페이지나 메인으로 이동 처리
            response.sendRedirect("login.jsp");

        } catch (NumberFormatException e) {
            // 사번 입력 오류 시 예외 처리 [cite: 14, 30]
            System.out.println("오류: 사번은 숫자만 입력 가능합니다.");
            response.sendRedirect("join.jsp?error=invalidId");
        }
    }
}