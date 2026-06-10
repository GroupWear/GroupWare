package com.groupware.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.groupware.dao.BoardDAO;
import com.groupware.dto.BoardDTO;
import com.groupware.dto.BoardFileDTO;
import com.groupware.dto.EmployeeDTO;

@WebServlet("/boardUpdate.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 15
)
public class BoardUpdateController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        BoardDAO dao = new BoardDAO();
        BoardDTO board = dao.getBoardDetail(boardNo);
        List<BoardFileDTO> fileList = dao.getFilesByBoardNo(boardNo);
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (board.getEmpNo() != loginEmp.getEmpNo() && !"Y".equals(loginEmp.getManager())) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('編集権限がありません。'); history.back();</script>");
            return;
        }
        
        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.getRequestDispatcher("boardWrite.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int boardNo = Integer.parseInt(request.getParameter("boardNo"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        BoardDTO board = new BoardDTO();
        board.setBoardNo(boardNo);
        board.setTitle(title);
        board.setContent(content);
        
        // 삭제할 파일 목록
        String[] delFilesStr = request.getParameterValues("delFiles");
        List<Integer> delFiles = new ArrayList<>();
        if (delFilesStr != null) {
            for (String fno : delFilesStr) delFiles.add(Integer.parseInt(fno));
        }
        
        // 추가할 파일 목록
        String uploadPath = getServletContext().getRealPath("/upload/board");
        List<BoardFileDTO> newFiles = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("files") && part.getSize() > 0) {
                String orgName = getFileName(part);
                String savedName = UUID.randomUUID().toString() + "_" + orgName;
                part.write(uploadPath + File.separator + savedName);
                
                BoardFileDTO fileDTO = new BoardFileDTO();
                fileDTO.setOrgName(orgName);
                fileDTO.setSavedName(savedName);
                fileDTO.setFileSize(part.getSize());
                newFiles.add(fileDTO);
            }
        }
        
        BoardDAO dao = new BoardDAO();
        if (dao.updateBoard(board, newFiles, delFiles)) {
            response.sendRedirect("boardDetail.do?boardNo=" + boardNo);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('投稿の修正に失敗しました。'); history.back();</script>");
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
