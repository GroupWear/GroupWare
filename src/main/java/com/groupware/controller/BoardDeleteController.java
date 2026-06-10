package com.groupware.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.groupware.dao.BoardDAO;
import com.groupware.dto.BoardDTO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/boardDelete.do")
public class BoardDeleteController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        BoardDAO dao = new BoardDAO();
        BoardDTO board = dao.getBoardDetail(boardNo);
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (board.getEmpNo() != loginEmp.getEmpNo() && !"Y".equals(loginEmp.getManager())) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('削除権限がありません。'); history.back();</script>");
            return;
        }
        
        int type = board.getBoardType();
        if (dao.deleteBoard(boardNo)) {
            response.sendRedirect("boardList.do?type=" + type);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('投稿の削除に失敗しました。'); history.back();</script>");
        }
    }
}
