package com.groupware.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.groupware.dao.RoomDAO;
import com.groupware.dto.RoomDTO;

@WebServlet("/roomUpdate.do")
public class RoomUpdateController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roomId = request.getParameter("roomId");
        
        RoomDAO dao = new RoomDAO();
        RoomDTO room = dao.getRoomDetail(roomId); // 기존 상세조회 메서드 활용
        
        request.setAttribute("room", room);
        request.getRequestDispatcher("roomUpdate.jsp").forward(request, response);
    }
}
