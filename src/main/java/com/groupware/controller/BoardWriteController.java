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

@WebServlet("/boardWrite.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15   // 15MB
)
public class BoardWriteController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        String typeStr = request.getParameter("type");
        int type = (typeStr == null) ? 1 : Integer.parseInt(typeStr);
        
        // 사내 소식(1)은 관리자만 작성 가능
        if (type == 1 && !"Y".equals(loginEmp.getManager())) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('사내 소식 게시판은 관리자만 작성할 수 있습니다.'); history.back();</script>");
            return;
        }
        
        request.setAttribute("type", type);
        request.getRequestDispatcher("boardWrite.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        int type = Integer.parseInt(request.getParameter("type"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        BoardDTO board = new BoardDTO();
        board.setBoardType(type);
        board.setTitle(title);
        board.setContent(content);
        board.setEmpNo(loginEmp.getEmpNo());
        
        // 파일 업로드 처리
        String uploadPath = getServletContext().getRealPath("/upload/board");
        File uploadDir = new File(uploadPath);
        System.out.println("내 파일이 저장되는 실제 위치: " + uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        List<BoardFileDTO> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("files") && part.getSize() > 0) {
                String orgName = getFileName(part);
                String savedName = UUID.randomUUID().toString() + "_" + orgName;
                part.write(uploadPath + File.separator + savedName);
                
                BoardFileDTO fileDTO = new BoardFileDTO();
                fileDTO.setOrgName(orgName);
                fileDTO.setSavedName(savedName);
                fileDTO.setFileSize(part.getSize());
                files.add(fileDTO);
            }
        }
        
        BoardDAO dao = new BoardDAO();
        if (dao.insertBoard(board, files)) {
            response.sendRedirect("boardList.do?type=" + type);
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('게시글 등록에 실패했습니다.'); history.back();</script>");
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
