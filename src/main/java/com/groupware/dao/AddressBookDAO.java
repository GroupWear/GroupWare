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
        List<EmployeeDTO> list = new ArrayList<>();
        // 부서(DEPT) 정보를 포함하고, 퇴사자(RETIRED='Y')는 제외한 목록을 가져옵니다.
        String sql = "SELECT EMP_NO, EMP_NAME, EMP_LEVEL, DEPT, MANAGER, RETIRED "
                   + "FROM EMPLOYEE "
                   + "WHERE RETIRED = 'N' OR RETIRED IS NULL "
                   + "ORDER BY EMP_NO ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
