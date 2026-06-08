package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.groupware.dao.CommentDAO;
import com.groupware.dto.CommentDTO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/commentUpdate.do")
public class CommentUpdateController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int commentNo = Integer.parseInt(request.getParameter("commentNo"));
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        String content = request.getParameter("content");

        CommentDAO dao = new CommentDAO();
        CommentDTO comment = dao.getComment(commentNo);

        if (comment != null) {
            // 본인 혹은 관리자만 수정 가능
            if (comment.getEmpNo() == loginEmp.getEmpNo() || "Y".equals(loginEmp.getManager())) {
                dao.updateComment(commentNo, content);
            }
        }
        
        response.sendRedirect("boardDetail.do?boardNo=" + boardNo);
    }
}
