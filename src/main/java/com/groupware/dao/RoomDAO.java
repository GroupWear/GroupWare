package com.groupware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.groupware.dto.RoomDTO;
import com.groupware.util.DBConnection;

/**
 * 회의실 마스터 정보를 조회하는 DAO입니다.
 */
public class RoomDAO {

    /**
     * 특정 회의실 상세 정보 조회: 방 번호(roomId)로 해당 실의 전체 정보를 가져옵니다.[cite: 39]
     */
    public RoomDTO getRoomDetail(String roomId) {
        RoomDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM ROOM WHERE ROOM_ID = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomId);
                rs = pstmt.executeQuery();

                // 데이터가 존재하면 DTO 객체에 각 컬럼 값을 매핑합니다.[cite: 39]
                if (rs.next()) {
                    dto = new RoomDTO();
                    dto.setRoomId(rs.getString("ROOM_ID"));
                    dto.setRoomName(rs.getString("ROOM_NAME"));
                    dto.setCapacity(rs.getInt("CAPACITY"));
                    dto.setHasBeam(rs.getString("HAS_BEAM"));
                    dto.setDescription(rs.getString("DESCRIPTION"));
                    dto.setEnable(rs.getString("ENABLE"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 자원 해제[cite: 39]
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dto;
    }
    public boolean updateRoom(RoomDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE ROOM SET ROOM_NAME=?, CAPACITY=?, HAS_BEAM=?, DESCRIPTION=?, ENABLE=? WHERE ROOM_ID=?";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getRoomName());
            pstmt.setInt(2, dto.getCapacity());
            pstmt.setString(3, dto.getHasBeam());
            pstmt.setString(4, dto.getDescription());
            pstmt.setString(5, dto.getEnable());
            pstmt.setString(6, dto.getRoomId());

            if (pstmt.executeUpdate() > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 자원 해제 로직 호출 (closeResource)
        }
        return result;
    }
}