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

@WebServlet("/commentInsert.do")
public class CommentInsertController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        String content = request.getParameter("content");
        String parentNoStr = request.getParameter("parentNo");
        
        CommentDTO comment = new CommentDTO();
        comment.setBoardNo(boardNo);
        comment.setEmpNo(loginEmp.getEmpNo());
        comment.setContent(content);
        
        if (parentNoStr != null && !parentNoStr.isEmpty()) {
            comment.setParentNo(Integer.parseInt(parentNoStr));
        }

        CommentDAO dao = new CommentDAO();
        dao.insertComment(comment);
        
        response.sendRedirect("boardDetail.do?boardNo=" + boardNo);
    }
}
