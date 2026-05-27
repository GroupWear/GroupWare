package com.groupware.controller; // 본인 프로젝트의 패키지 구조에 맞게 수정하세요

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EquipmentDTO;

@WebServlet("/rentInsert.do")
public class RentInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 인코딩 및 응답 타입 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // 2. 폼에서 넘어온 파라미터 수령
        int eqNo = Integer.parseInt(request.getParameter("eqNo"));
        int rentCount = Integer.parseInt(request.getParameter("rentCount"));
        String rentPurpose = request.getParameter("rentPurpose");
        
        try {
            EquipmentDAO dao = new EquipmentDAO();
            
            // 3. 트랜잭션 전 재고 검증 (선택한 비품 상세 조회)
            EquipmentDTO equipment = dao.getEquipmentDetail(eqNo);
            
            if (equipment != null && equipment.getRemainCount() >= rentCount) {
                
                // [비즈니스 로직 영역]
                // 4-1. TODO: RentDAO 등을 생성하여 RENT 테이블에 대여 이력 INSERT 수행 필요
                // boolean isRentSuccess = rentDao.insertRentRecord(eqNo, rentCount, rentPurpose);
                
                // 4-2. 수량 차감 업데이트 수행
                // 현재 EquipmentDAO에 구현된 updateEquipment 메서드를 활용하여 잔여 수량을 깎아줍니다.
                equipment.setRemainCount(equipment.getRemainCount() - rentCount);
                boolean isUpdateSuccess = dao.updateEquipment(equipment);
                
                if (isUpdateSuccess) {
                    // 5. 성공 시 메시지 출력 후 비품 리스트 서블릿(.do)으로 이동
                    out.println("<script>");
                    out.println("alert('신청이 완료되었습니다.');");
                    out.println("location.href='equipmentList.do';"); // 본인의 목록 서블릿 주소로 작성
                    out.println("</script>");
                } else {
                    throw new Exception("재고 수량 수정 실패");
                }
            } else {
                // 재고가 부족한 경우 예외 처리
                out.println("<script>");
                out.println("alert('대여 가능한 수량이 부족합니다.');");
                out.println("history.back();");
                out.println("</script>");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>");
            out.println("alert('대여 신청 중 오류가 발생했습니다.');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}