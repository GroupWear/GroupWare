package com.groupware.controller;

import com.groupware.dao.EquipmentDAO;
import com.groupware.dto.EquipmentDTO;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 비품 삭제(폐기) 요청을 처리하는 컨트롤러입니다.
 */
@WebServlet("/deleteEq.do")
public class EqDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 삭제할 비품 번호를 파라미터에서 가져옵니다.
        int eqNo = Integer.parseInt(request.getParameter("eqNo"));
        EquipmentDAO dao = new EquipmentDAO();
        
        //※ ischecked 를 통해 값이 1인 상태만 무조건
        int checkEquipment = dao.getCheckEquipment(eqNo);
        System.out.println(checkEquipment);
        
        
        PrintWriter out = response.getWriter();
        out.println("<script>");
        try {
            // 2. DAO를 호출하여 비품 삭제를 시도합니다.
			if(checkEquipment > 0)
			{
				  boolean isSuccess = dao.deleteEquipment(eqNo); 
				  
				  if (isSuccess) {
					  	out.println("alert('備品が正常に廃棄されました。');");
				  		out.println("location.href='adminEqList.do';"); 
				  } 
				  else {
					  out.println("alert('廃棄処理に失敗しました。');"); 
					  out.println("history.back();"); 
				  }
			}else {
				out.println("alert('返却を確認してください。廃棄処理に失敗しました。');"); 
				out.println("history.back();");
			}
				
        } catch (Exception e) {
            // 3. 외래 키 제약 조건 등으로 인해 삭제가 불가능한 경우 예외 처리를 수행합니다.
            // 해당 비품이 대여 내역 테이블에 묶여있어 외래 키 오류가 발생하는 경우
            out.println("alert('現在貸出記録が残っている備品は廃棄できません。');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}