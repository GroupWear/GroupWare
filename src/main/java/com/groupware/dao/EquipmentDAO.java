package com.groupware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.groupware.dto.EquipmentDTO;
import com.groupware.util.DBConnection;

/**
 * 비품 재고 관리 및 조회를 담당하는 데이터 접근 객체입니다.
 */
public class EquipmentDAO {

	/**
	 * 전체 비품 목록 조회: 모든 비품을 번호 순서대로 가져와 리스트로 반환합니다.
	 */
	public List<EquipmentDTO> getAllEquipments() {
		List<EquipmentDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//ischecked 추가 폐기 때문에
		String sql = "SELECT * FROM EQUIPMENT WHERE 1=1 AND ISCHECKED = 'Y' ORDER BY EQ_NO ASC";
		System.out.println("전체 : 장비 list 출력 : getAllEquipments"+sql);
		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					EquipmentDTO dto = new EquipmentDTO();
					String ischecked_ch = rs.getString("ischecked");
					dto.setEqNo(rs.getInt("EQ_NO"));
					dto.setEqName(rs.getString("EQ_NAME"));
					dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
					dto.setRemainCount(rs.getInt("REMAIN_COUNT"));
					dto.setIschecked(ischecked_ch.charAt(0));
					list.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 신규 비품 등록: 현재 비품 번호 중 최대값에 +1을 하여 자동으로 새 번호를 부여하고 등록합니다.
	 */
//	public boolean insertEquipment(EquipmentDTO dto) {
//		boolean result = false;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//
//		// NVL과 MAX를 활용하여 수동으로 시퀀스 효과를 낸 쿼리
//		String sql = "INSERT INTO EQUIPMENT (EQ_NO, EQ_NAME, TOTAL_COUNT, REMAIN_COUNT) "
//				+ "VALUES ((SELECT NVL(MAX(EQ_NO), 0) + 1 FROM EQUIPMENT), ?, ?, ?)";
//
//		try {
//			conn = DBConnection.getConnection();
//			if (conn != null) {
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, dto.getEqName());
//				pstmt.setInt(2, dto.getTotalCount());
//				// 신규 등록 시 총 수량과 잔여 수량은 동일하게 세팅합니다.
//				pstmt.setInt(3, dto.getTotalCount());
//
//				int count = pstmt.executeUpdate();
//				if (count > 0) result = true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeResource(conn, pstmt, null);
//		}
//		return result;
//	}
	// SQL 쿼리에서 번호를 계산하던 서브쿼리를 시퀀스로 교체
	public boolean insertEquipment(EquipmentDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// ★ 수정됨: NVL(MAX...) 대신 시퀀스(SEQ_EQUIPMENT.NEXTVAL) 사용
		String sql = "INSERT INTO EQUIPMENT (EQ_NO, EQ_NAME, TOTAL_COUNT, REMAIN_COUNT) "
				+ "VALUES (SEQ_EQUIPMENT.NEXTVAL, ?, ?, ?)";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEqName());
				pstmt.setInt(2, dto.getTotalCount());
				pstmt.setInt(3, dto.getTotalCount()); // 초기 등록 시 잔여량 = 총수량

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	/**
	 * 비품 정보 수정: 관리자가 수정한 비품명, 총 수량, 잔여 수량을 업데이트합니다.
	 */
	public boolean updateEquipment(EquipmentDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE EQUIPMENT SET EQ_NAME = ?, TOTAL_COUNT = ?, REMAIN_COUNT = ? WHERE EQ_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEqName());
				pstmt.setInt(2, dto.getTotalCount());
				pstmt.setInt(3, dto.getRemainCount());
				pstmt.setInt(4, dto.getEqNo());

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
			}
			System.out.println("비품 정보 수정 : "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	/**
	 * 비품 폐기: 더 이상 사용하지 않는 비품 정보를 DB에서 삭제합니다.
	 */
	public boolean deleteEquipment(int eqNo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		//전) history 이력에 있는 테이블로 인해 폐기 오류가 나옴
		//20260608 LHS
		//String sql = "DELETE FROM EQUIPMENT WHERE EQ_NO = ?";
		String sql = "UPDATE EQUIPMENT SET ISCHECKED = 'N' WHERE EQ_NO = ?";
		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, eqNo);

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	/**
	 * 특정 비품 상세 조회: 대여 폼 등에서 선택한 비품 1개의 상세 정보를 가져올 때 사용합니다.
	 */
	public EquipmentDTO getEquipmentDetail(int eqNo) {
		EquipmentDTO dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM EQUIPMENT WHERE EQ_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, eqNo);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new EquipmentDTO();
					dto.setEqNo(rs.getInt("EQ_NO"));
					dto.setEqName(rs.getString("EQ_NAME"));
					dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
					dto.setRemainCount(rs.getInt("REMAIN_COUNT"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, rs);
		}
		return dto;
	}
	/*
	 * Ischecked 컬럼 추가로 인해 select 를 통해 반납 완료한 데이터 삭제 가능한 쿼리
	 * 
	 * */
	public int getCheckEquipment(int eqNo) {
        int count = 0;
        String sql = "SELECT COUNT(*)  FROM RENTAL_HISTORY A WHERE 1=1 AND A.EQ_NO = ? AND A.STATUS = '반납완료' AND NOT EXISTS ( SELECT 1  FROM RENTAL_HISTORY B WHERE B.EQ_NO = A.EQ_NO AND B.STATUS IN ('승인대기', '대여중', '반려됨'))";
        try {
        	Connection conn = DBConnection.getConnection();
        	PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eqNo);
        		
            ResultSet rs = pstmt.executeQuery(); 
            if (rs.next()) count = rs.getInt(1);
            //test
            //System.out.println("여부 파악"+sql);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
	
	
	/**
	 * 전체 비품 개수 조회 (페이징 처리를 위해 필요)
	 */
	public int getTotalCount() {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM EQUIPMENT";
		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, rs);
		}
		return count;
	}

	/**
	 * 페이징 처리가 된 비품 목록 조회
	 */
	public List<EquipmentDTO> getEquipmentsPaging(int startRow, int endRow) {
		List<EquipmentDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// H2 (Oracle 모드) 또는 Oracle에서 사용 가능한 ROWNUM 쿼리
		String sql = "SELECT * FROM ("
				   + "  SELECT ROWNUM AS RN, A.* FROM ("
				   + "    SELECT * FROM EQUIPMENT ORDER BY EQ_NO ASC"
				   + "  ) A"
				   + ") WHERE RN BETWEEN ? AND ?";

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				EquipmentDTO dto = new EquipmentDTO();
				dto.setEqNo(rs.getInt("EQ_NO"));
				dto.setEqName(rs.getString("EQ_NAME"));
				dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
				dto.setRemainCount(rs.getInt("REMAIN_COUNT"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 자원 해제 공통 메서드입니다.
	 */
	private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}