package com.groupware.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.groupware.dao.BoardDAO;
import com.groupware.dto.BoardDTO;

@WebServlet("/boardList.do")
public class BoardListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String typeStr = request.getParameter("type");
        int type = (typeStr == null) ? 1 : Integer.parseInt(typeStr);
        
        String pageStr = request.getParameter("page");
        int page = (pageStr == null) ? 1 : Integer.parseInt(pageStr);
        int pageSize = 10;
        
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");
        
        BoardDAO dao = new BoardDAO();
        List<BoardDTO> list = dao.getBoardList(type, page, pageSize, searchType, keyword);
        int totalCount = dao.getTotalCount(type, searchType, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        request.setAttribute("boardList", list);
        request.setAttribute("type", type);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);
        
        request.getRequestDispatcher("boardList.jsp").forward(request, response);
    }
}
