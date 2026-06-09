package com.groupware.controller;

import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EquipmentDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 전체 비품 목록을 조회하여 화면에 전달하는 컨트롤러입니다.
 */
@WebServlet("/equipmentList.do")
public class EquipmentListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// 1. 페이징 관련 파라미터 처리
    	int currentPage = 1;
    	String pageParam = request.getParameter("page");
    	if (pageParam != null && !pageParam.isEmpty()) {
    		try {
    			currentPage = Integer.parseInt(pageParam);
    		} catch (NumberFormatException e) {
    			currentPage = 1;
    		}
    	}

    	int pageSize = 10; // 한 페이지당 출력할 항목 수
    	int startRow = (currentPage - 1) * pageSize + 1;
    	int endRow = currentPage * pageSize;

    	// 2. DAO를 통해 데이터베이스로부터 페이징 처리된 비품 목록을 가져옵니다.
    	EquipmentDAO dao = new EquipmentDAO();
    	List<EquipmentDTO> eqList = dao.getEquipmentsPaging(startRow, endRow);
    	int totalCount = dao.getTotalCount();
    	
    	// 3. 페이징 네비게이션을 위한 계산
    	int totalPages = (int) Math.ceil((double) totalCount / pageSize);
    	int pageBlock = 5; // 하단에 표시할 페이지 번호 개수
    	int startPage = ((currentPage - 1) / pageBlock) * pageBlock + 1;
    	int endPage = startPage + pageBlock - 1;
    	if (endPage > totalPages) {
    		endPage = totalPages;
    	}

    	// 4. JSP 화면에서 리스트와 페이징 정보를 출력할 수 있도록 request 영역에 데이터를 담습니다.
    	request.setAttribute("eqList", eqList);
    	request.setAttribute("currentPage", currentPage);
    	request.setAttribute("totalPages", totalPages);
    	request.setAttribute("startPage", startPage);
    	request.setAttribute("endPage", endPage);

    	// 5. 비품 목록 화면(equipmentList.jsp)으로 포워딩하여 이동합니다.
    	request.getRequestDispatcher("equipmentList.jsp").forward(request, response);
    }
}