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

@WebServlet("/commentDelete.do")
public class CommentDeleteController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int commentNo = Integer.parseInt(request.getParameter("commentNo"));
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));

        CommentDAO dao = new CommentDAO();
        CommentDTO comment = dao.getComment(commentNo);

        if (comment != null) {
            // 본인 혹은 관리자만 삭제 가능
            if (comment.getEmpNo() == loginEmp.getEmpNo() || "Y".equals(loginEmp.getManager())) {
                dao.deleteComment(commentNo);
            }
        }
        
        response.sendRedirect("boardDetail.do?boardNo=" + boardNo);
    }
}
