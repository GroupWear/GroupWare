package com.groupware.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.groupware.dao.RentalDAO;
import com.groupware.dao.ReservationDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.RentalHistoryDTO;
import com.groupware.dto.ReservationDTO;
import com.groupware.dao.LeaveDAO;
import com.groupware.dto.LeaveHistoryDTO;

@WebServlet("/myPage.do")
public class MyPageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 1. 내 비품 대여 내역
//        RentalDAO rentalDao = new RentalDAO();
//        List<RentalHistoryDTO> myList = rentalDao.getMyRentalList(loginEmp.getEmpNo()); 
//        request.setAttribute("myList", myList);

        // 2. 내 회의실 예약 내역
        ReservationDAO reserveDao = new ReservationDAO();
        List<ReservationDTO> reserveList = reserveDao.getMyReservations(loginEmp.getEmpNo());
        request.setAttribute("reserveList", reserveList);

        // ★ 3. 내 휴가 신청 내역 추가 
//        LeaveDAO leaveDao = new LeaveDAO();
//        List<LeaveHistoryDTO> leaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo());
//        request.setAttribute("leaveList", leaveList);

        request.getRequestDispatcher("myPage.jsp").forward(request, response);
    }
}