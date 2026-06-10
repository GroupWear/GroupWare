package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EquipmentDTO;

/**
 * equipmentList.jsp에서 '대여 신청' 버튼을 클릭했을 때 요청을 처리하는 서블릿입니다.
 * URL 패턴은 버튼 링크에 맞추어 "/rentForm.do"로 설정했습니다.
 */
@WebServlet("/rentForm.do")
public class RentFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 인코딩 설정 (한글 깨짐 방지)
        request.setCharacterEncoding("UTF-8");
        
        // 2. equipmentList.jsp에서 쿼리스트링으로 보낸 비품 번호(eqNo) 파라미터 가져오기
        String eqNoStr = request.getParameter("eqNo");
        
        if (eqNoStr != null && !eqNoStr.isEmpty()) {
            try {
                int eqNo = Integer.parseInt(eqNoStr);
                
                // 3. 비품 재고 데이터를 관리하는 DAO 인스턴스 생성
                EquipmentDAO dao = new EquipmentDAO();
                
                // 4. DB에서 선택한 비품의 상세 내용 및 대여 가능 수량(REMAIN_COUNT) 가져오기
                // 롬복(@Data) 기반의 EquipmentDTO 객체에 값이 자동으로 매핑되어 반환됩니다.
                EquipmentDTO equipment = dao.getEquipmentDetail(eqNo);
                
                if (equipment != null) {
                    // 5. rentForm.jsp 화면으로 넘겨주기 위해 request 영역에 바인딩
                    // jsp에서는 ${equipment.eqName}, ${equipment.remainCount} 형태로 바로 꺼낼 수 있습니다.
                    request.setAttribute("equipment", equipment);
                } else {
                    request.setAttribute("errorMsg", "該当する備品の情報が見つかりません。");
                }
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "不正な備品番号形式です。");
            }
        } else {
            request.setAttribute("errorMsg", "備品番号パラメータが欠落しています。");
        }

        // 6. 조회한 데이터를 request 객체에 실어 대여 신청 폼 페이지(rentForm.jsp)로 이동 (Forward)
        request.getRequestDispatcher("/rentForm.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
