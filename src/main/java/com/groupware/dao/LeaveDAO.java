package com.groupware.dao;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.time.*;
import com.groupware.dto.LeaveHistoryDTO; // 별도 생성 필요
import com.groupware.util.DBConnection;

public class LeaveDAO {

	// 내 휴가 신청 내역만 조회하는 메서드 추가
	public List<com.groupware.dto.LeaveHistoryDTO> getMyLeaveList(int empNo) {
		List<com.groupware.dto.LeaveHistoryDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM LEAVE_HISTORY WHERE EMP_NO = ? ORDER BY LEAVE_NO DESC";

		try (Connection conn = com.groupware.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, empNo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				com.groupware.dto.LeaveHistoryDTO dto = new com.groupware.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 평일(휴가 사용일) 계산 로직
	public int calculateWorkingDays(LocalDate start, LocalDate end) {
		int workingDays = 0;
		LocalDate date = start;
		while (!date.isAfter(end)) {
			DayOfWeek day = date.getDayOfWeek();
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				workingDays++;
			}
			date = date.plusDays(1);
		}
		return workingDays;
	}
	// 06 10 수정: 신청 시 서명 정보가 포함되도록 수정된 insertLeave
	public boolean insertLeave(LeaveHistoryDTO dto) {
	    boolean result = false;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    PreparedStatement pstmtUpdate = null; // 연차 차감용 추가

	    String sql = "INSERT INTO LEAVE_HISTORY (LEAVE_NO, EMP_NO, START_DATE, END_DATE, USE_DAYS, REASON, STATUS, APPROVAL_STEP, "
	               + "SIGN1, SIGN1_DATE, SIGN2, SIGN2_DATE, SIGN3, SIGN3_DATE, SIGN4, SIGN4_DATE, SIGN5, SIGN5_DATE) "
	               + "VALUES (SEQ_LEAVE.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false);
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setInt(1, dto.getEmpNo());
	        pstmt.setDate(2, dto.getStartDate());
	        pstmt.setDate(3, dto.getEndDate());
	        pstmt.setInt(4, dto.getUseDays());
	        pstmt.setString(5, dto.getReason());
	        pstmt.setString(6, dto.getStatus());
	        pstmt.setInt(7, dto.getApprovalStep());

	        String[] signs = {dto.getSign1(), dto.getSign2(), dto.getSign3(), dto.getSign4(), dto.getSign5()};
	        Date[] dates = {dto.getSign1Date(), dto.getSign2Date(), dto.getSign3Date(), dto.getSign4Date(), dto.getSign5Date()};

	        for (int i = 0; i < 5; i++) {
	            if (signs[i] != null) pstmt.setString(8 + (i * 2), signs[i]);
	            else pstmt.setNull(8 + (i * 2), java.sql.Types.VARCHAR);

	            if (dates[i] != null) pstmt.setDate(9 + (i * 2), dates[i]);
	            else pstmt.setNull(9 + (i * 2), java.sql.Types.DATE);
	        }

	        if (pstmt.executeUpdate() > 0) {
	            // [추가] 대표(레벨 5)인 경우 신청과 동시에 즉시 연차 차감
	            if (dto.getEmpLevel() == 5) {
	                String updateSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - ? WHERE EMP_NO = ?";
	                pstmtUpdate = conn.prepareStatement(updateSql);
	                pstmtUpdate.setInt(1, dto.getUseDays());
	                pstmtUpdate.setInt(2, dto.getEmpNo());
	                pstmtUpdate.executeUpdate();
	            }
	            
	            conn.commit();
	            result = true;
	        }
	    } catch (Exception e) {
	        try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
	        e.printStackTrace();
	    } finally {
	        // 추가된 자원 해제
	        if (pstmtUpdate != null) try { pstmtUpdate.close(); } catch (Exception e) {}
	        closeResource(conn, pstmt, null);
	    }
	    return result;
	}

	public boolean processLeaveApproval(int leaveNo, int step, String managerName, boolean isApprove) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUpdateLeave = null;

		// 결재 단계에 따른 컬럼명 동적 생성
		String signCol = "SIGN" + step;
		String dateCol = "SIGN" + step + "_DATE";

		// 승인 시: 마지막 5단계면 '승인완료', 아니면 '승인대기' 유지
		String status = isApprove ? (step == 5 ? "승인완료" : "승인대기") : "반려됨";
		int nextStep = isApprove ? (step < 5 ? step + 1 : 5) : step;

		String sql = "UPDATE LEAVE_HISTORY SET " + signCol + " = ?, " + dateCol
				+ " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE LEAVE_NO = ?";

		try {
			conn = com.groupware.util.DBConnection.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, managerName);
			pstmt.setString(2, status);
			pstmt.setInt(3, nextStep);
			pstmt.setInt(4, leaveNo);

			int count = pstmt.executeUpdate();

			// ★ 최종 5단계 승인 시에만 실제 연차 차감 로직 실행
			if (count > 0 && isApprove && step == 5) {
				String updateLeaveSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - "
						+ "(SELECT USE_DAYS FROM LEAVE_HISTORY WHERE LEAVE_NO = ?) "
						+ "WHERE EMP_NO = (SELECT EMP_NO FROM LEAVE_HISTORY WHERE LEAVE_NO = ?)";

				pstmtUpdateLeave = conn.prepareStatement(updateLeaveSql);
				pstmtUpdateLeave.setInt(1, leaveNo);
				pstmtUpdateLeave.setInt(2, leaveNo);

				if (pstmtUpdateLeave.executeUpdate() > 0) {
					conn.commit();
					result = true;
				} else {
					conn.rollback();
				}
			} else if (count > 0) {
				conn.commit();
				result = true;
			}
		} catch (Exception e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (Exception ex) {
			}
			e.printStackTrace();
		} finally {
			// 자원 해제 로직 (pstmt, conn 등 close)
		}
		return result;
	}

	// 결재 대기 목록 조회 (관리자용)
	public List<com.groupware.dto.LeaveHistoryDTO> getPendingLeaveList(int managerLevel) {
		List<com.groupware.dto.LeaveHistoryDTO> list = new ArrayList<>();
		String sql = "SELECT h.*, e.EMP_NAME, e.DEPT FROM LEAVE_HISTORY h " + "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
				+ "WHERE h.STATUS = '승인대기' AND h.APPROVAL_STEP = ? ORDER BY h.START_DATE ASC";
		try (Connection conn = com.groupware.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, managerLevel);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				com.groupware.dto.LeaveHistoryDTO dto = new com.groupware.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 특정 휴가 신청 상세 조회 (기존 코드 아래에 추가) 06 10 수정 
	public com.groupware.dto.LeaveHistoryDTO getLeaveDetail(int leaveNo) {
		com.groupware.dto.LeaveHistoryDTO dto = null;
		String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, e.DEPT " + "FROM LEAVE_HISTORY h "
				+ "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO " + "WHERE h.LEAVE_NO = ?";

		try (Connection conn = com.groupware.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, leaveNo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new com.groupware.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setEmpLevel(rs.getInt("EMP_LEVEL"));//06 10 추가 
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				// 서명 정보 세팅
				dto.setSign1(rs.getString("SIGN1"));
				dto.setSign1Date(rs.getDate("SIGN1_DATE"));
				dto.setSign2(rs.getString("SIGN2"));
				dto.setSign2Date(rs.getDate("SIGN2_DATE"));
				dto.setSign3(rs.getString("SIGN3"));
				dto.setSign3Date(rs.getDate("SIGN3_DATE"));
				dto.setSign4(rs.getString("SIGN4"));
				dto.setSign4Date(rs.getDate("SIGN4_DATE"));
				dto.setSign5(rs.getString("SIGN5"));
				dto.setSign5Date(rs.getDate("SIGN5_DATE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	// 기존 코드 하단에 추가
	public List<com.groupware.dto.LeaveHistoryDTO> getAllLeaveDocuments() {
		List<com.groupware.dto.LeaveHistoryDTO> list = new java.util.ArrayList<>();
		// SQL JOIN을 통해 e.EMP_LEVEL을 가져옵니다.
		String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, e.DEPT FROM LEAVE_HISTORY h "
				+ "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO ORDER BY h.LEAVE_NO DESC";
		try (java.sql.Connection conn = com.groupware.util.DBConnection.getConnection();
				java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
				java.sql.ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				com.groupware.dto.LeaveHistoryDTO dto = new com.groupware.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				dto.setReason(rs.getString("REASON"));
				dto.setEmpLevel(rs.getInt("EMP_LEVEL")); // ★ 추가: 직급 매핑
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 전체 휴가 기안 개수 조회
	 */
	public int getTotalLeaveCount() {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM LEAVE_HISTORY";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 페이징 처리가 된 전체 휴가 기안 목록 조회
	 */
	public List<com.groupware.dto.LeaveHistoryDTO> getAllLeaveDocumentsPaging(int startRow, int endRow) {
		List<com.groupware.dto.LeaveHistoryDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM ("
				   + "  SELECT ROWNUM AS RN, A.* FROM ("
				   + "    SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, e.DEPT FROM LEAVE_HISTORY h "
				   + "    JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO ORDER BY h.LEAVE_NO DESC"
				   + "  ) A"
				   + ") WHERE RN BETWEEN ? AND ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					com.groupware.dto.LeaveHistoryDTO dto = new com.groupware.dto.LeaveHistoryDTO();
					dto.setLeaveNo(rs.getInt("LEAVE_NO"));
					dto.setEmpNo(rs.getInt("EMP_NO"));
					dto.setEmpName(rs.getString("EMP_NAME"));
					dto.setDept(rs.getString("DEPT"));
					dto.setStartDate(rs.getDate("START_DATE"));
					dto.setEndDate(rs.getDate("END_DATE"));
					dto.setUseDays(rs.getInt("USE_DAYS"));
					dto.setStatus(rs.getString("STATUS"));
					dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
					dto.setReason(rs.getString("REASON"));
					dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
					list.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean processApproval(int leaveNo, int step, String managerName, boolean isApprove) {
	    boolean result = false;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    PreparedStatement pstmtUpdateEmp = null;

	    String signCol = "SIGN" + step;
	    String dateCol = "SIGN" + step + "_DATE";
	    
	    // 승인 여부에 따른 상태값 결정
	    String status = isApprove ? "승인대기" : "반려됨";
	    int nextStep = isApprove ? step + 1 : step;

	    // 마지막 5단계 승인 시 '승인완료'로 상태 변경
	    if (isApprove && step == 5) {
	        status = "승인완료";
	    }

	    String sql = "UPDATE LEAVE_HISTORY SET " + signCol + " = ?, " + dateCol
	               + " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE LEAVE_NO = ?";

	    try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false); // 트랜잭션 시작

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, managerName);
	        pstmt.setString(2, status);
	        pstmt.setInt(3, nextStep);
	        pstmt.setInt(4, leaveNo);
	        
	        int count = pstmt.executeUpdate();
	        
	        // [수정된 로직] 상태가 '승인완료'가 되는 시점에 연차 차감 수행
	        if (count > 0 && isApprove && "승인완료".equals(status)) {
	            String updateSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - "
	                             + "(SELECT USE_DAYS FROM LEAVE_HISTORY WHERE LEAVE_NO = ?) "
	                             + "WHERE EMP_NO = (SELECT EMP_NO FROM LEAVE_HISTORY WHERE LEAVE_NO = ?)";
	            
	            pstmtUpdateEmp = conn.prepareStatement(updateSql);
	            pstmtUpdateEmp.setInt(1, leaveNo);
	            pstmtUpdateEmp.setInt(2, leaveNo);
	            
	            int updateCount = pstmtUpdateEmp.executeUpdate();
	            
	            System.out.println("연차 차감 결과(업데이트된 행 수): " + updateCount);
	            
	            if (updateCount > 0) {
	                conn.commit();
	                result = true;
	            } else {
	                conn.rollback(); // 연차 차감 대상이 없으면 롤백
	                System.out.println("연차 차감 실패: 업데이트 대상 없음");
	            }
	        } else if (count > 0) {
	            conn.commit(); // 일반 결재 단계 승인/반려 시 커밋
	            result = true;
	        }
	    } catch (Exception e) {
	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        // 자원 해제
	        if (pstmtUpdateEmp != null) try { pstmtUpdateEmp.close(); } catch (Exception e) {}
	        closeResource(conn, pstmt, null);
	    }
	    return result;
	}
	
	
	// 퀵 승인용 (GET 방식 대응)
	public boolean processStepApproval(int leaveNo, int currentStep, String managerName, String action) {
		boolean isApprove = "approve".equals(action);
		return processApproval(leaveNo, currentStep, managerName, isApprove);
	}

	private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}
	//06 10 추가 
	// 특정 사원의 직급(EMP_LEVEL)을 조회하는 메서드
	public int getEmpLevelByNo(int empNo) {
	    int level = 0;
	    String sql = "SELECT EMP_LEVEL FROM EMPLOYEE WHERE EMP_NO = ?";

	    try (Connection conn = com.groupware.util.DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, empNo);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                level = rs.getInt("EMP_LEVEL");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return level;
	}
	
	
	
	
}