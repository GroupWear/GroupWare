package com.groupware.controller;

import java.io.IOException;
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
        
        // 1. 파라미터 받기 (JSP의 자바스크립트나 location.href에서 넘어온 PK)
        String rentalNoStr = request.getParameter("rentalNo");
        
        // 2. 세션에서 로그인한 유저 정보 및 최고 관리자(ADMIN) 여부 획득
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        boolean isAdmin = (loginEmp != null && "Y".equals(loginEmp.getManager()));
        
        if (rentalNoStr != null && !rentalNoStr.isEmpty()) {
            try {
                RentalDAO rentalDao = new RentalDAO();
                int rentalNo = Integer.parseInt(rentalNoStr);
                
                // 📌 DTO에 선언된 empLevel을 확인하기 위해 DB 상세 정보를 먼저 가져옵니다.
                RentalHistoryDTO detail = rentalDao.getRentalDetail(rentalNo);
                
                if (detail != null) {
                    int creatorLevel = detail.getEmpLevel(); // 기안자의 레벨 (퇴사자는 0)
                    String currentStatus = detail.getStatus(); // 현재 문서의 결재/대여 상태
                    
                    // 🛡️ [백엔드 보안 검증 가드라인]
                    if ("승인대기".equals(currentStatus) || "반려됨".equals(currentStatus)) {
                        // 애초에 결재 승인도 안 났거나 반려된 문서는 반납 처리 불가능
                        System.out.println("[차단] 미승인 또는 이미 반려된 문서에 대한 반납 시도 차단 - 문서번호: " + rentalNo);
                    
                    } else if (creatorLevel == 0 && !isAdmin) {
                        // 💡 신청자가 퇴사자(Lv.0)인데, 로그인한 세션이 최고 관리자(ADMIN)가 아니라면 차단!
                        System.out.println("[경고] 일반 사원이 퇴사자 기안을 대리 반납하려고 시도함 - 문서번호: " + rentalNo);
                    
                    } else {
                        // ✅ 검증 통과 상황:
                        // 1. 일반 사원이 본인의 '대여중 / 미반납' 비품을 직접 반납하는 경우
                        // 2. 최고 관리자(isAdmin = true)가 퇴사자의 '대여중 / 미반납' 비품을 대신 반납하는 경우
                        rentalDao.updateStatus(rentalNo, "반납완료");
                        System.out.println("[성공] 반납 완료 처리 완료 - 문서번호: " + rentalNo + " (관리자대리여부: " + isAdmin + ")");
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 3. 처리가 끝나면 메인 대시보드(main.jsp)로 안전하게 리다이렉트
        response.sendRedirect("main.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}