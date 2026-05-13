package com.groupware.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dto.EmployeeDTO;

@WebServlet("/main.do")
public class MainController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	// --- [로그인 더미 데이터 세팅 시작] ---
        HttpSession session = request.getSession();
        if (session.getAttribute("loginEmp") == null) {
            EmployeeDTO dummyEmp = new EmployeeDTO();
            //1번 5등급 김대표
            dummyEmp.setEmpNo(1); 
            dummyEmp.setEmpName("김대표");
            dummyEmp.setEmpLevel(5);
            dummyEmp.setDept("경영지원팀");
            dummyEmp.setManager("Y");
            //2번 4등급 이인사
//            dummyEmp.setEmpNo(2); 
//            dummyEmp.setEmpName("이인사");
//            dummyEmp.setEmpLevel(4);
//            dummyEmp.setDept("경영지원팀");
//            dummyEmp.setManager("N");
            
            //3번 3등급 김대표
//            dummyEmp.setEmpNo(3); 
//            dummyEmp.setEmpName("박개발");
//            dummyEmp.setEmpLevel(5);
//            dummyEmp.setDept("개발팀");
//            dummyEmp.setManager("N");
            
            //4번 2등급 김대표
//            dummyEmp.setEmpNo(4); 
//            dummyEmp.setEmpName("최대리");
//            dummyEmp.setEmpLevel(2);
//            dummyEmp.setDept("개발팀");
//            dummyEmp.setManager("N");
            
            //20번 1등급 서정산
//            dummyEmp.setEmpNo(20); 
//            dummyEmp.setEmpName("서정산");
//            dummyEmp.setEmpLevel(1);
//            dummyEmp.setDept("재무팀");
//            dummyEmp.setManager("N");
       
             
            // 세션에 강제로 로그인 정보 굽기
            session.setAttribute("loginEmp", dummyEmp);
            System.out.println("더미 로그인 세션 생성 완료");
        }
        // --- [로그인 더미 데이터 세팅 끝] ---
      response.sendRedirect("main.jsp");
    }
}