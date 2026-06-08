package com.groupware.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.AddressBookDAO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/addressBook.do")
public class AddressBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 로그인 체크
        HttpSession session = request.getSession();
        if (session.getAttribute("loginEmp") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 페이징 파라미터 처리
        String pageStr = request.getParameter("page");
        int page = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);
        int pageSize = 10;

        AddressBookDAO dao = new AddressBookDAO();
        List<EmployeeDTO> empList = dao.getAddressListPaged(page, pageSize);
        int totalCount = dao.getTotalAddressCount();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        request.setAttribute("empList", empList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
        request.getRequestDispatcher("addressBook.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
