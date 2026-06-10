package com.groupware.controller;

import com.groupware.dao.EmployeeDAO;
import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EmployeeDTO;
import com.groupware.dto.EquipmentDTO;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/insertEmp.do")
public class AdminEmpInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 관리자가 폼에서 넘긴 파라미터 수집
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        String empName = request.getParameter("empName").trim();
        int empLevel = Integer.parseInt(request.getParameter("empLevel"));
        String manager = request.getParameter("manager").trim();
        String dept = request.getParameter("dept").trim();

        // 2. DTO에 담기 (비밀번호는 세팅하지 않음)
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpNo(empNo);
        dto.setEmpName(empName);
        dto.setEmpLevel(empLevel);
        dto.setManager(manager);
        dto.setDept(dept);

        // 3. DAO 호출하여 DB에 Insert
        EmployeeDAO dao = new EmployeeDAO();
        boolean isSuccess = dao.insertEmployee(dto);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('新規社員情報がシステムに事前登録されました。');");
            out.println("location.href='admin.do';"); // 사원 관리 목록 페이지로 리다이렉트
        } else {
            out.println("alert('登録に失敗しました。既に存在する社員番号か確認してください。');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}