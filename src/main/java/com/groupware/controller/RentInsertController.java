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
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.EquipmentDTO;
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
        
        // 2. 세션 로그인 체크
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            out.println("<script>alert('로그인이 필요한 서비스입니다.');location.href='index.jsp';</script>");
            out.close();
            return;
        }
        
        try {
            // 3. rentForm.jsp에서 전달한 파라미터 수령
            int eqNo = Integer.parseInt(request.getParameter("eqNo"));
            int rentCount = Integer.parseInt(request.getParameter("rentCount"));
            
            String startDateStr = request.getParameter("rentalDate");
            String endDateStr = request.getParameter("returnDate");
            String content = request.getParameter("content"); 
            
            // 4. 데이터베이스 검증: 실제 재고 체크 선행
            EquipmentDAO eqDao = new EquipmentDAO();
            EquipmentDTO equipment = eqDao.getEquipmentDetail(eqNo);
            
            if (equipment == null) {
                throw new Exception("존재하지 않는 비품 번호입니다.");
            }
            
            if (equipment.getRemainCount() < rentCount) {
                out.println("<script>alert('대여 가능한 잔여 재고가 부족합니다. (현재 잔여: " + equipment.getRemainCount() + "개)');history.back();</script>");
                return;
            }
            
            // 5. RentalHistoryDTO 객체 생성 및 데이터베이스 저장 데이터 매핑
            RentalHistoryDTO rentalDto = new RentalHistoryDTO();
            rentalDto.setEmpNo(loginEmp.getEmpNo());                  // 기안자 사번
            rentalDto.setEqNo(eqNo);                                 // 비품 번호
            rentalDto.setReqCount(rentCount);                        // 신청 수량
            rentalDto.setTitle(equipment.getEqName() + " 대여 신청"); // 기안서 제목 자동 구성
            rentalDto.setContent(content);                           // 대여 사유
            
            // 💡 [초강력 안전장치 - 누락된 날짜 처리 완벽 복구]
            // 기본값으로 오늘과 7일 뒤를 먼저 꽂아두어 데이터가 비어 가드가 깨지는 문제를 방지합니다.
            rentalDto.setRentalDate(Date.valueOf(LocalDate.now()));
            rentalDto.setReturnDate(Date.valueOf(LocalDate.now().plusDays(7)));
            
            // 사용자가 화면에서 날짜를 선택한 경우 안전하게 오버라이딩 처리
            if (startDateStr != null && !startDateStr.trim().isEmpty() && !"null".equalsIgnoreCase(startDateStr)) {
                rentalDto.setRentalDate(Date.valueOf(startDateStr.trim()));
            }
            if (endDateStr != null && !endDateStr.trim().isEmpty() && !"null".equalsIgnoreCase(endDateStr)) {
                rentalDto.setReturnDate(Date.valueOf(endDateStr.trim()));
            }
            
         // 💡 [등급별 동적 결재 프로세스 - 기안자 자동 승인 및 넥스트 단계 점프 설계]
            int empLevel = loginEmp.getEmpLevel(); 
            
            if (empLevel == 5) {
                // 5단계(최고 등급 김대표) 임직원 기안 시 즉시 최종 승인 및 대여중 종결
                rentalDto.setStatus("대여중");          
                rentalDto.setApprovalStep(6);         // 최종 마감 단계(6) 세팅
                rentalDto.setSign5(loginEmp.getEmpName()); 
                rentalDto.setSign5Date(Date.valueOf(LocalDate.now())); 
            } else {
                // 1~4단계 일반 임직원 기안 시
                rentalDto.setStatus("승인대기");
                
                // 💡 [핵심 교정 1]: 본인 단계에서 승인 버튼을 또 누르지 않도록, 결재 단계를 다음 상급자 레벨(+1)로 자동 점프시킵니다!
                rentalDto.setApprovalStep(empLevel + 1); 
                
                // 💡 [핵심 교정 2]: 기안자 본인의 결재 칸(SIGN)에는 신청과 동시에 '담당 서명 및 결재 완료 처리'를 미리 대입합니다.
                if (empLevel == 1) {
                    rentalDto.setSign1(loginEmp.getEmpName());
                    rentalDto.setSign1Date(Date.valueOf(LocalDate.now()));
                } else if (empLevel == 2) {
                    rentalDto.setSign2(loginEmp.getEmpName());
                    rentalDto.setSign2Date(Date.valueOf(LocalDate.now()));
                } else if (empLevel == 3) {
                    rentalDto.setSign3(loginEmp.getEmpName());
                    rentalDto.setSign3Date(Date.valueOf(LocalDate.now()));
                } else if (empLevel == 4) {
                    rentalDto.setSign4(loginEmp.getEmpName());
                    rentalDto.setSign4Date(Date.valueOf(LocalDate.now()));
                }
            }
            
            // 6. DB 반영을 위해 RentalDAO 호출 
            RentalDAO rentalDao = new RentalDAO();
            boolean isSuccess = rentalDao.insertRental(rentalDto); 
            
            if (isSuccess) {
                // 7. 성공 시 리스트로 복귀 (?tab=equipment 로 프론트 탭 제어 연동)
                out.println("<script>alert('비품 대여 신청이 완료되었습니다.');location.href='documentList.do?tab=equipment';</script>");
            } else {
                throw new Exception("RENTAL_HISTORY 테이블 데이터 삽입 트랜잭션 오류");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('대여 신청 처리 중 시스템 오류가 발생했습니다.');history.back();</script>");
        } finally {
            out.close();
        }
    }
}