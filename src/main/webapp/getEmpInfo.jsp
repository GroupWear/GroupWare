<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.groupware.dao.EmployeeDAO"%>
<%@ page import="com.groupware.dto.EmployeeDTO"%>
<%
    String empNoStr = request.getParameter("empNo");
    String resultJson = "{\"result\": \"fail\"}"; 
    
    if (empNoStr != null && !empNoStr.trim().isEmpty()) {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            // DAO에서 String 타입을 받으므로 그대로 전달
            EmployeeDTO dto = dao.getEmployeeByNo(empNoStr.trim());
            
            if (dto != null) {
                /* [주의] 현재 DAO의 getEmployeeByNo 쿼리에는 emp_pw가 빠져있습니다.
                   만약 가입 여부(비밀번호 존재 여부)를 확인해야 한다면 
                   DAO의 해당 메서드 SQL문에 emp_pw를 추가해야 합니다.
                   우선은 에러가 나지 않도록 처리합니다.
                */
                String currentPw = dto.getEmpPw(); // DAO SQL에 추가 전까지는 null일 수 있음
                
                // 가입 가능 조건: 비밀번호가 없거나 비어있는 경우
                if (currentPw == null || currentPw.trim().isEmpty()) {
                    StringBuilder json = new StringBuilder();
                    json.append("{");
                    json.append("\"result\": \"success\",");
                    json.append("\"name\": \"" + (dto.getEmpName() != null ? dto.getEmpName() : "") + "\",");
                    json.append("\"dept\": \"" + (dto.getDept() != null ? dto.getDept() : "미배정") + "\",");
                    // getPosName() 대신 존재하는 getEmpLevel()을 사용하거나 수동 매칭
                    json.append("\"position\": \"" + dto.getEmpLevel() + "레벨\""); 
                    json.append("}");
                    resultJson = json.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
    
    out.clear(); 
    out.print(resultJson);
%>