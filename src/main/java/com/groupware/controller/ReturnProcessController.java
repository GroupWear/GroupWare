package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.RentalDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.RentalHistoryDTO;

@WebServlet("/returnProcess.do")
public class ReturnProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 파라미터 받기
        String rentalNoStr = request.getParameter("rentalNo");
        
        // 2. 세션에서 로그인한 유저 정보 및 최고 관리자(ADMIN) 여부 획득
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        boolean isAdmin = (loginEmp != null && "Y".equals(loginEmp.getManager()));
        
        // 💡 [수정] 반납 완료 알림창을 제어하기 위한 통합 성공 플래그 변수
        boolean isReturnSuccess = false;
        
        if (rentalNoStr != null && !rentalNoStr.isEmpty()) {
            try {
                RentalDAO rentalDao = new RentalDAO();
                int rentalNo = Integer.parseInt(rentalNoStr);
                
                // DTO에 선언된 empLevel을 확인하기 위해 DB 상세 정보를 먼저 가져옵니다.
                RentalHistoryDTO detail = rentalDao.getRentalDetail(rentalNo);
                
                if (detail != null) {
                    int creatorLevel = detail.getEmpLevel(); // 기안자의 레벨 (퇴사자는 0)
                    String currentStatus = detail.getStatus(); // 현재 문서의 결재/대여 상태
                    
                    // 🛡️ 백엔드 보안 검증
                    if ("승인대기".equals(currentStatus) || "반려됨".equals(currentStatus)) {
                        System.out.println("[차단] 미승인 또는 이미 반려된 문서에 대한 반납 시도 차단 - 문서번호: " + rentalNo);
                    
                    } else if (creatorLevel == 0 && !isAdmin) {
                        System.out.println("[경고] 일반 사원이 퇴사자 기안을 대리 반납하려고 시도함 - 문서번호: " + rentalNo);
                    
                    } else {
                        // ✅ 검증 통과 상황 (본인 반납 또는 관리자의 퇴사자 대리 반납)
                        rentalDao.updateStatus(rentalNo, "반납완료");
                        System.out.println("[성공] 반납 완료 처리 완료 - 문서번호: " + rentalNo + " (관리자대리여부: " + isAdmin + ")");
                        
                        // 💡 [수정] 모든 정상 반납 완료 상황에서 플래그를 true로 변경
                        isReturnSuccess = true;
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 3. 💡 [수정] 알림창 출력 및 리다이렉트 분기 처리
        if (isReturnSuccess) {
            // 본인 반납 또는 퇴사자 대리 반납이 성공하면 알림창을 띄우고 메인으로 이동합니다.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('반납을 완료했습니다.');");
            out.println("location.href='main.jsp';");
            out.println("</script>");
            out.flush();
        } else {
            // 보안 검증에 실패했거나 예외가 발생한 경우에는 알림창 없이 바로 안전하게 리다이렉트합니다.
            response.sendRedirect("main.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}