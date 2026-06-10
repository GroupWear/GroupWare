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

@WebServlet("/processApproval.do")
public class ProcessApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 인코딩 및 응답 헤더 바인딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // 2. 세션 로그인 상태 검증
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            out.println("<script>");
            out.println("alert('ログインが必要なサービスです。');");
            out.println("location.href='index.jsp';");
            out.println("</script>");
            out.close();
            return;
        }
        
        // 3. 상세 화면(Form)에서 전송한 결재 파라미터 수령
        String rentalNoStr = request.getParameter("rentalNo");
        String eqNoStr = request.getParameter("eqNo");
        String stepStr = request.getParameter("step");
        String isApproveStr = request.getParameter("isApprove");
        
        // 데이터 누락 시 안전 백 가드 장착
        if (rentalNoStr == null || eqNoStr == null || stepStr == null || isApproveStr == null) {
            out.println("<script>");
            out.println("alert('不正な承認依頼情報です。');");
            out.println("location.href='documentList.do?tab=equipment';");
            out.println("</script>");
            out.close();
            return;
        }
        
        try {
            int rentalNo = Integer.parseInt(rentalNoStr);
            int eqNo = Integer.parseInt(eqNoStr);
            int step = Integer.parseInt(stepStr);
            boolean isApprove = Boolean.parseBoolean(isApproveStr);
            
            // 📌 [사원 가계도 연동]: 결재란(SIGNx)에 기입할 현재 로그인한 결재권자의 실명 수령
            String empName = loginEmp.getEmpName();
            
            // 반려 처리가 진행될 경우, 이름 뒤에 공백 가공 후 (반려)가 박히도록 포맷팅 규칙을 적용합니다.
            if (!isApprove) {
                empName = empName + " (差し戻し)";
            }
            
            // 4. 백엔드 비즈니스 트랜잭션 레이어 호출
            RentalDAO rentalDao = new RentalDAO();
            boolean isSuccess = rentalDao.processApproval(rentalNo, eqNo, step, empName, isApprove);
            
            // 5. 트랜잭션 결과에 따른 목록 리다이렉트 피드백 처리
            if (isSuccess) {
                String resultMsg = isApprove ? "起案書の承認が完了しました。" : "起案書が最終的に却下され、先に確保されていた備品在庫が安全に復旧しました。";
                
                out.println("<script>");
                out.println("alert('" + resultMsg + "');");
                // 📌 [UI 연동 포인트]: 복귀했을 때 바로 비품 탭이 활성화되어 있도록 파라미터를 명시합니다.
                out.println("location.href='documentList.do?tab=equipment';"); 
                out.println("</script>");
            } else {
                throw new Exception("결재 처리 트랜잭션 반영 실패");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            out.println("<script>alert('パラメータデータ形式が失われました。');history.back();</script>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('決済処理中に予期しないシステムエラーが発生しました。');history.back();</script>");
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 보안 및 데이터 위변조 방지를 위해 결재 승인/반려 처리는 오직 POST 방식만 허용합니다.
        response.sendRedirect("documentList.do?tab=equipment");
    }
}