package com.groupware.controller;

import com.groupware.dao.EmployeeDAO;
import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EquipmentDTO;
import com.groupware.dto.EmployeeDTO;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * 관리자 페이지에서 발생하는 액션(직급변경, 권한이양, 퇴사)을 처리하는 컨트롤러입니다.
 */
@WebServlet("/adminAction.do")
public class AdminActionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        // 관리자가 아니면 튕겨내는 방어 로직 20260513 LHS 수정
        if (loginEmp == null || !"Y".equals(loginEmp.getManager())) {
            out.println("<script>alert('관리자가 아닙니다.'); location.href='index.jsp';</script>");
            return;
        }
        
        //조회를 통해 관리자는 총 2명까지 가능하고 그 이하는 불가능합니다. 20260514 LHS
        //System.out.println(Integer.parseInt(request.getParameter("count")));
        
        String action = request.getParameter("action");
        int targetEmpNo = Integer.parseInt(request.getParameter("empNo"));
        EmployeeDAO dao = new EmployeeDAO();
        //값을 갖고 오기 위함 20260514 LHS
        List<EmployeeDTO> dto = dao.getAllEmployees();
        boolean isSuccess = false;

        
        out.println("<script>");

        try {
        	
            if ("transferManager".equals(action) || "updateLevel".equals(action)) { 
                
                
                int currentManagerCount = dto.get(0).getCount_manager();// DB에서 실시간 관리자 수 조회
                System.out.println(currentManagerCount);
                // 관리자가 2명 이하인 상태에서 권한을 넘기거나 바꾸려고 하면 차단
                if (currentManagerCount <= 2) {
                    out.println("alert('최소 2명의 관리자가 유지되어야 하므로 작업을 진행할 수 없습니다. 현재 관리자 수: " + currentManagerCount + "명');");
                    out.println("history.back();");
                    out.println("</script>");
                    out.flush();
                    out.close();
                    return; // 여기서 서블릿 실행을 종료시킵니다.
                }
            }

        	
        	
        	
        	
            if ("updateLevel".equals(action)) {
                // 직급 변경 로직
                int newLevel = Integer.parseInt(request.getParameter("newLevel"));
                isSuccess = dao.updateEmployeeLevel(targetEmpNo, newLevel);
                
                if (isSuccess) {
                    out.println("alert('직급이 성공적으로 변경되었습니다.');");
                    out.println("location.href='admin.do';");
                }
                
            } else if ("transferManager".equals(action)) {
                // 권한 이양 로직
                isSuccess = dao.transferManagerRole(loginEmp.getEmpNo(), targetEmpNo);
                
                if (isSuccess) {
                    // 권한을 넘겼으므로 세션을 초기화하고 강제 로그아웃 처리합니다.
                	// 20260514 2명까지 위임이 가능하므로 초기화 시킬 이유가 없음 20260514
                    //session.invalidate();
                    //20260514 관리자 권한 양도 한 후 admin 페이지로 넘기는 부분 수정
                    out.println("alert('관리자 권한을 성공적으로 넘겼습니다.');");
                    out.println("location.href='admin.do';");
                }
                
            } else if ("deleteEmp".equals(action)) {
                // 퇴사(삭제) 처리 로직
                isSuccess = dao.deleteEmployee(targetEmpNo);
                
                if (isSuccess) {
                    out.println("alert('해당 사원의 퇴사(삭제) 처리가 완료되었습니다.');");
                    out.println("location.href='admin.do';");
                }
            }

            if (!isSuccess) {
                out.println("alert('요청하신 작업 처리에 실패했습니다.');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("alert('서버 오류가 발생했습니다.');");
            out.println("history.back();");
        }

        out.println("</script>");
        out.flush();
        out.close();
    }
}