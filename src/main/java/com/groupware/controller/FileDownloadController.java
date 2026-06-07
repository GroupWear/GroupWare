package com.groupware.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.groupware.dao.BoardDAO;
import com.groupware.dto.BoardFileDTO;

@WebServlet("/fileDownload.do")
public class FileDownloadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileNo = Integer.parseInt(request.getParameter("fileNo"));
        
        BoardDAO dao = new BoardDAO();
        BoardFileDTO fileDTO = dao.getFileDetail(fileNo);
        
        if (fileDTO != null) {
            String uploadPath = getServletContext().getRealPath("/upload/board");
            File file = new File(uploadPath + File.separator + fileDTO.getSavedName());
            
            if (file.exists()) {
                String mimeType = getServletContext().getMimeType(file.toString());
                if (mimeType == null) mimeType = "application/octet-stream";
                
                response.setContentType(mimeType);
                String fileName = URLEncoder.encode(fileDTO.getOrgName(), "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }
}
