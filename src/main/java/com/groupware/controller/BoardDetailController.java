package com.groupware.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.groupware.dao.BoardDAO;
import com.groupware.dao.CommentDAO;
import com.groupware.dto.BoardDTO;
import com.groupware.dto.BoardFileDTO;
import com.groupware.dto.CommentDTO;

@WebServlet("/boardDetail.do")
public class BoardDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        
        BoardDAO dao = new BoardDAO();
        dao.increaseHit(boardNo);
        BoardDTO board = dao.getBoardDetail(boardNo);
        List<BoardFileDTO> files = dao.getFilesByBoardNo(boardNo);
        
        // 댓글 목록 조회 추가
        CommentDAO commentDao = new CommentDAO();
        List<CommentDTO> commentList = commentDao.getCommentsByBoardNo(boardNo);
        
        request.setAttribute("board", board);
        request.setAttribute("fileList", files);
        request.setAttribute("commentList", commentList);
        
        request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
    }
}
