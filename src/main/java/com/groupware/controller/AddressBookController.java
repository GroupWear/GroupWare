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

        AddressBookDAO dao = new AddressBookDAO();
        List<EmployeeDTO> empList = dao.getAllAddressList();

        request.setAttribute("empList", empList);
        request.getRequestDispatcher("addressBook.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
