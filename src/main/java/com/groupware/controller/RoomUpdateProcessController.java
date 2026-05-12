package com.groupware.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.groupware.dao.RoomDAO;
import com.groupware.dto.RoomDTO;

@WebServlet("/roomUpdateProcess.do")
public class RoomUpdateProcessController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(request.getParameter("roomId"));
        dto.setRoomName(request.getParameter("roomName"));
        dto.setCapacity(Integer.parseInt(request.getParameter("capacity")));
        dto.setHasBeam(request.getParameter("hasBeam"));
        dto.setEnable(request.getParameter("enable"));
        dto.setDescription(request.getParameter("description"));

        RoomDAO dao = new RoomDAO();
        boolean isSuccess = dao.updateRoom(dto);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        if(isSuccess) {
            out.println("alert('회의실 정보가 수정되었습니다.');");
            out.println("location.href='officeMap.jsp';"); // 요청하신 대로 officeMap으로 이동
        } else {
            out.println("alert('수정에 실패했습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
    }
}