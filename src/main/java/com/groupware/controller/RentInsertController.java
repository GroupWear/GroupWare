package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.EquipmentDAO;
import com.groupware.dao.RentalDAO;
import com.groupware.dto.EquipmentDTO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.RentalHistoryDTO;

@WebServlet("/rentInsert.do")
public class RentInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 인코딩 및 응답 헤더 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // 2. 세션 로그인 체크 (누가 빌렸는지 사번 추출을 위해 필수)
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            out.println("<script>");
            out.println("alert('로그인이 필요한 서비스입니다.');");
            out.println("location.href='index.jsp';");
            out.println("</script>");
            out.close();
            return;
        }
        
        try {
            // 3. rentForm.jsp에서 전달한 파라미터 수령
            int eqNo = Integer.parseInt(request.getParameter("eqNo"));
            int rentCount = Integer.parseInt(request.getParameter("rentCount"));
            
            // 대여 시작일과 반납 예정일 파라미터 (HTML의 <input type="date"> 규격 문자열 수령)
            String startDateStr = request.getParameter("rentalDate");
            String endDateStr = request.getParameter("returnDate");
            
            // 4. 데이터베이스 검증: 대여 신청 수량이 현재 실제 재고보다 많은지 선행 체크
            EquipmentDAO eqDao = new EquipmentDAO();
            EquipmentDTO equipment = eqDao.getEquipmentDetail(eqNo);
            
            if (equipment == null) {
                throw new Exception("존재하지 않는 비품 번호입니다.");
            }
            
            if (equipment.getRemainCount() < rentCount) {
                out.println("<script>");
                out.println("alert('대여 가능한 잔여 재고가 부족합니다. (현재 잔여: " + equipment.getRemainCount() + "개)');");
                out.println("history.back();");
                out.println("</script>");
                return;
            }
            
            // 5. RentalHistoryDTO 객체 생성 및 데이터베이스 저장 데이터 매핑
            RentalHistoryDTO rentalDto = new RentalHistoryDTO();
            rentalDto.setEmpNo(loginEmp.getEmpNo());                  // 기안자 사번
            rentalDto.setEqNo(eqNo);                                 // 비품 번호
            rentalDto.setReqCount(rentCount);                        // 📌 수정된 규격 필드명(reqCount)에 매핑
            rentalDto.setTitle(equipment.getEqName() + " 대여 신청"); // 기안서 제목 자동 구성
            rentalDto.setStatus("승인대기");                          // 최초 문서 상태 설정
            rentalDto.setApprovalStep(1);                             // 결재 최초 1단계 세팅
            
            // 날짜 예외 방어 가드 처리 (날짜 입력칸이 비어있다면 자동 기본값 주입)
            if (startDateStr != null && !startDateStr.isEmpty()) {
                rentalDto.setRentalDate(Date.valueOf(startDateStr));
            } else {
                rentalDto.setRentalDate(Date.valueOf(LocalDate.now())); // 오늘 날짜
            }
            
            if (endDateStr != null && !endDateStr.isEmpty()) {
                rentalDto.setReturnDate(Date.valueOf(endDateStr));
            } else {
                rentalDto.setReturnDate(Date.valueOf(LocalDate.now().plusDays(7))); // 기본 7일 뒤 반납
            }
            
            // 6. DB 반영을 위해 RentalDAO 호출 
            // (insertRental 메서드 실행 시 RENTAL_HISTORY에 INSERT가 일어납니다)
            RentalDAO rentalDao = new RentalDAO();
            boolean isSuccess = rentalDao.insertRental(rentalDto); 
            
            if (isSuccess) {
                // 7. 성공 시 메시지 출력 후 통합 기안 문서함 목록조회 서블릿으로 이동시킵니다.
                out.println("<script>");
                out.println("alert('비품 대여 신청이 완료되었습니다.');");
                out.println("location.href='documentList.do';"); // 전사 목록을 가져오는 리스트 서블릿 주소
                out.println("</script>");
            } else {
                throw new Exception("RENTAL_HISTORY 테이블 데이터 삽입 트랜잭션 오류");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>");
            out.println("alert('대여 신청 처리 중 시스템 오류가 발생했습니다.');");
            out.println("history.back();");
            out.println("</script>");
        } finally {
            out.close();
        }
    }
}