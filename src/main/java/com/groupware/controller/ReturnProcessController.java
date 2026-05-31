package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.groupware.dao.RentalDAO;

@WebServlet("/returnProcess.do")
public class ReturnProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 파라미터 받기 (JSP에서 넘겨준 rentalNo)
        String rentalNoStr = request.getParameter("rentalNo");
        
        if (rentalNoStr != null && !rentalNoStr.isEmpty()) {
        	try {
        	    RentalDAO rentalDao = new RentalDAO();
        	    // rentalDao.returnRental(rentalNoStr);  <-- 이 부분을 아래와 같이 변경
        	    
        	    int rentalNo = Integer.parseInt(rentalNoStr);
        	    rentalDao.updateStatus(rentalNo, "반납완료");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 3. 처리 후 메인 페이지(main.jsp)로 리다이렉트
        response.sendRedirect("main.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}