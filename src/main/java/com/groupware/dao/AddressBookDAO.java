package com.groupware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.groupware.dto.EmployeeDTO;
import com.groupware.util.DBConnection;

/**
 * 기존 EmployeeDAO를 수정하지 않기 위해 새로 생성한 주소록 전용 DAO입니다.
 * 부서(dept) 정보를 포함하여 전체 사원 목록을 가져옵니다.
 */
public class AddressBookDAO {

    public List<EmployeeDTO> getAllAddressList() {
        return getAddressListPaged(1, Integer.MAX_VALUE); // 기존 코드 호환성을 위해 유지할 수 있으나, 여기서는 오버로딩처럼 활용
    }

    public List<EmployeeDTO> getAddressListPaged(int page, int pageSize) {
        List<EmployeeDTO> list = new ArrayList<>();
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        // ROWNUM을 이용한 페이징 처리 (Oracle/H2 Oracle Mode 호환)
        String sql = "SELECT * FROM ( "
                   + "  SELECT ROWNUM AS RN, A.* FROM ( "
                   + "    SELECT EMP_NO, EMP_NAME, EMP_LEVEL, DEPT, MANAGER, RETIRED "
                   + "    FROM EMPLOYEE "
                   + "    WHERE RETIRED = 'N' OR RETIRED IS NULL "
                   + "    ORDER BY EMP_NO ASC "
                   + "  ) A "
                   + ") WHERE RN BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, start);
            pstmt.setInt(2, end);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setEmpNo(rs.getInt("EMP_NO"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
                    dto.setDept(rs.getString("DEPT") != null ? rs.getString("DEPT") : "미지정");
                    dto.setManager(rs.getString("MANAGER"));
                    dto.setRetired(rs.getString("RETIRED"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalAddressCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM EMPLOYEE WHERE RETIRED = 'N' OR RETIRED IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
