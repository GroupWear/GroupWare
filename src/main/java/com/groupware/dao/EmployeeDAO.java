package com.groupware.dao;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.groupware.dto.EmployeeDTO;
import com.groupware.util.DBConnection;



/**
 * 사원 관련 데이터베이스 연동을 담당하는 클래스입니다.
 */
public class EmployeeDAO {

	/**
	 * 로그인 체크: 사번(empNo)과 비밀번호(empPw)가 일치하면 사원 정보를 반환합니다. 
	 * (아이디 대신 사번으로 로그인하도록 수정됨)
	 */

	public EmployeeDTO loginCheck(String loginNo, String loginPw) {
	    EmployeeDTO dto = null;
	 //  SQL문에 dept, max_leave, cur_leave 추가
	    String sql = "SELECT EMP_NO, EMP_PW, EMP_NAME, EMP_LEVEL, MANAGER, DEPT, MAX_LEAVE, CUR_LEAVE, RETIRED "
	               + "FROM EMPLOYEE WHERE EMP_NO = ? AND EMP_PW = ?";

	    try (Connection conn = DBConnection.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 // trim() 사용자 오타방지 스페이스바 무시.	    	
	        pstmt.setString(1, loginNo.trim());
	        pstmt.setString(2, loginPw.trim());

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // 퇴사자 여부 확인
	                if ("Y".equals(rs.getString("RETIRED"))) {
	                    System.out.println("로그인 실패: 퇴사 처리된 계정입니다. (사번: " + loginNo + ")");
	                    return null;
	                }

	                dto = new EmployeeDTO();

	                dto.setEmpNo(rs.getInt("EMP_NO"));
	                dto.setEmpPw(rs.getString("EMP_PW"));
	                dto.setEmpName(rs.getString("EMP_NAME"));
	                dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
	                dto.setManager(rs.getString("MANAGER"));
	                dto.setDept(rs.getString("DEPT"));
	                dto.setMaxLeave(rs.getInt("MAX_LEAVE"));
	                dto.setCurLeave(rs.getInt("CUR_LEAVE"));
	                dto.setRetired(rs.getString("RETIRED"));
	                
	                System.out.println("로그인 성공: " + dto.getEmpName() + "님 환영합니다.");
	            } else {
	                System.out.println("로그인 실패: 일치하는 데이터가 없습니다. (입력사번: " + loginNo + ")");
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("EmployeeDAO.loginCheck 오류 발생");
	        e.printStackTrace();
	    }
	    return dto;
	}
	/**
	 * 회원가입(정보 업데이트): 초기 사원 데이터에 사용자가 입력한 비밀번호를 업데이트합니다. (이제 INSERT가 아니라, 이미 존재하는 사원
	 * 정보에 PW만 UPDATE 하는 방식으로 변경됨)
	 */
	public boolean updateEmployeePassword(String empNo, String empPw) {
		boolean result = false;
		// 사번을 조건으로 비밀번호를 업데이트합니다.
		String sql = "UPDATE EMPLOYEE SET emp_pw = ? WHERE emp_no = ?";
		System.out.println("암호화 : "+empPw);
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, empPw);
			pstmt.setString(2, empNo);
			
			int count = pstmt.executeUpdate();
			if (count > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 사번으로 사원 정보(이름, 직급, 매니저 여부)를 조회하는 기능 (회원가입 전 정보 확인용)
	 */
//	public EmployeeDTO getEmployeeByNo(String empNo) {
//		EmployeeDTO dto = null;
//		String sql = "SELECT emp_no, emp_name, emp_level, manager FROM EMPLOYEE WHERE emp_no = ?";
//
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//			pstmt.setString(1, empNo);
//
//			try (ResultSet rs = pstmt.executeQuery()) {
//				if (rs.next()) {
//					dto = new EmployeeDTO();
//					dto.setEmpNo(rs.getInt("emp_no"));
//					dto.setEmpName(rs.getString("emp_name"));
//					dto.setEmpLevel(rs.getInt("emp_level"));
//					dto.setManager(rs.getString("manager"));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return dto;
//	}
	// 2. getEmployeeByNo 메서드 수정 (회원가입 시 정보 확인용)
	//20260526 LHS retired 추가
	public EmployeeDTO getEmployeeByNo(String empNo) {
	    EmployeeDTO dto = null;
	    // SQL문에 dept, max_leave, cur_leave 추가
	    String sql = "SELECT emp_no, emp_name, emp_level, manager, dept, max_leave, cur_leave , retired "
	               + "FROM EMPLOYEE WHERE emp_no = ?";

	    try (Connection conn = DBConnection.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, empNo);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                dto = new EmployeeDTO();
	                dto.setEmpNo(rs.getInt("emp_no"));
	                dto.setEmpName(rs.getString("emp_name"));
	                dto.setEmpLevel(rs.getInt("emp_level"));
	                dto.setManager(rs.getString("manager"));
	                // ★ 추가된 필드 세팅
	                dto.setDept(rs.getString("dept"));
	                dto.setMaxLeave(rs.getInt("max_leave"));
	                dto.setCurLeave(rs.getInt("cur_leave"));
	                // ★ 추가된 필드 세팅
	                dto.setRetired(rs.getString("retired"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return dto;
	}

	// 관리자 페이지용: 전체 사원 목록 조회
	public List<EmployeeDTO> getAllEmployees() {


		List<EmployeeDTO> list = new ArrayList<>();
		// 관리자(Y)가 맨 위에, 그다음 직급 높은 순, 마지막으로 사번 순으로 정렬합니다.
		String sql = "SELECT emp_no, emp_name, emp_level, manager, retired, "
		           + "(SELECT COUNT(*) FROM EMPLOYEE WHERE 1=1 AND MANAGER = 'Y') AS count_manager " // count 뒤에 공백 추가
		           + "FROM EMPLOYEE ORDER BY manager DESC, emp_level DESC, emp_no ASC";

		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setEmpNo(rs.getInt("emp_no")); // int 타입 유지
				dto.setEmpName(rs.getString("emp_name"));
				dto.setEmpLevel(rs.getInt("emp_level"));
				dto.setManager(rs.getString("manager"));
				dto.setRetired(rs.getString("retired"));//20260513 퇴사자 데이터 확인
				dto.setCount_manager(rs.getInt("count_manager"));//20260514 위임 카운트 데이터
				list.add(dto);
			}
			/* 20260513 L.H.S 실행 쿼리문 */
			System.out.println("EmployeeDAO.getAllEmployees 전체 사원 목록 조회"+sql);
		} catch (Exception e) {
			e.printStackTrace();
			/* 20260513 L.H.S 오류 쿼리문 */
			System.out.println("Employee.getAllEmployees 전체 사원 목록 조회 오류"+e);
			
		}
		return list;
	}

	// 1. 사원 직급 변경
	public boolean updateEmployeeLevel(int empNo, int newLevel) {
		boolean result = false;
		String sql = "UPDATE EMPLOYEE SET emp_level = ? WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, newLevel);
			pstmt.setInt(2, empNo);

			if (pstmt.executeUpdate() > 0)
				result = true;
			
			System.out.println("updateEmployeeLevel사원 직급 변경"+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 2. 관리자 권한 이양 (기존 관리자는 N, 새 관리자는 Y로 변경)
//	public boolean transferManagerRole(int oldManagerNo, int newManagerNo) {
		public boolean transferManagerRole(int newManagerNo) {
		boolean result = false;
		// 두 개의 쿼리를 실행해야 하므로 수동 커밋 모드를 사용할 수도 있지만,
		// 간단하게 두 번의 UPDATE 문을 순차적으로 실행합니다.
		String sql1 = "UPDATE EMPLOYEE SET manager = 'N' WHERE emp_no = (SELECT EMP_NO FROM EMPLOYEE WHERE 1=1 AND MANAGER = 'Y' AND EMP_LEVEL <> 5)";
		String sql2 = "UPDATE EMPLOYEE SET manager = 'Y' WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement(sql1);
				PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

			// 기존 관리자 권한 박탈
//			pstmt1.setInt(1, oldManagerNo);
			pstmt1.executeUpdate();

			// 새 관리자 권한 부여
			pstmt2.setInt(1, newManagerNo);
			if (pstmt2.executeUpdate() > 0)
				result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 사원 비밀번호 변경 메서드
	public boolean updatePassword(int empNo, String newPw) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// 해당 사번(EMP_NO)의 비밀번호를 새 비밀번호로 업데이트합니다.
		String sql = "UPDATE EMPLOYEE SET EMP_PW = ? WHERE EMP_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newPw);
				pstmt.setInt(2, empNo);

				int count = pstmt.executeUpdate();
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 3. 퇴사 처리 (데이터 영구 삭제)
	// 주의: 실제 실무에서는 DELETE 대신 status='퇴사' 로 UPDATE 하지만, 요청하신 대로 삭제 처리합니다.
//	public boolean deleteEmployee(int empNo) {
//		boolean result = false;
//		String sql = "DELETE FROM EMPLOYEE WHERE emp_no = ?";
//
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//			pstmt.setInt(1, empNo);
//
//			if (pstmt.executeUpdate() > 0)
//				result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	/**
	 * [관리자용] 사원 퇴사 처리 (Soft Delete 방식) 실제 데이터를 삭제하지 않고, 비밀번호를 변경하여 로그인을 차단하고 모든 권한을
	 * 회수합니다.
	 * 20260513 LHS 퇴사자 처리 컬럼 추가
	 * 
	 */
	public boolean deleteEmployee(int empNo) {
		boolean result = false;

		// DELETE 쿼리 대신 UPDATE 쿼리를 사용하여 계정을 비활성화(잠금) 처리합니다.
		// EMP_PW를 'RETIRED'로 바꾸어 기존 비밀번호로 로그인할 수 없게 만듭니다.
		String sql = "UPDATE EMPLOYEE SET EMP_PW = 'RETIRED', EMP_LEVEL = 0, MANAGER = 'N', RETIRED = 'Y' WHERE EMP_NO = ?";

		try (java.sql.Connection conn = com.groupware.util.DBConnection.getConnection();
				java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, empNo);

			int count = pstmt.executeUpdate();
			if (count > 0) {
				result = true;
			}
			
			/* 20260513 L.H.S 실행 쿼리문 */
			System.out.println("EmployeeDAO.deleteEmployee 사원 퇴사 처리"+sql);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 사번과 이름으로 비밀번호 찾기 메서드
	public String findPassword(int empNo, String empName) {
		String pw = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 사번과 이름이 모두 일치하는 데이터의 비밀번호를 가져옵니다.
		String sql = "SELECT EMP_PW FROM EMPLOYEE WHERE EMP_NO = ? AND EMP_NAME = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, empNo);
				pstmt.setString(2, empName);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					pw = rs.getString("EMP_PW");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		return pw;
	}

	/**
	 * [관리자용] 신규 사원 사전 등록 비밀번호(EMP_PW)는 비워두고 사번, 이름, 직급, 관리자 여부만 초기 세팅합니다.
	 * 20260514 LHS : RETIRED 컬럼 추가 N
	 */
	public boolean insertEmployee(EmployeeDTO dto) {
		boolean result = false;
		// 비밀번호는 제외하고 INSERT 진행
		String sql = "INSERT INTO EMPLOYEE (EMP_NO, EMP_NAME, EMP_LEVEL, MANAGER,RETIRED) VALUES (?, ?, ?, ?,'N')";

		try (java.sql.Connection conn = com.groupware.util.DBConnection.getConnection();
				java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, dto.getEmpNo());
			pstmt.setString(2, dto.getEmpName());
			pstmt.setInt(3, dto.getEmpLevel());
			pstmt.setString(4, dto.getManager());

			int count = pstmt.executeUpdate();
			if (count > 0) {
				result = true;
			}
			System.out.println("(insertEmployee)신규 사원 사전 등록 비밀번호(EMP_PW)는 비워두고 사번, 이름, 직급, 관리자 여부 세팅 : "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/*
	 * 20260515 LHS 위임 취소
	 * */
	public boolean updateManagerStatus(int targetEmpNo, String status) {
	    String sql = "UPDATE EMPLOYEE SET MANAGER = ? WHERE EMP_NO = ?";
	
	    boolean result = false;

	  
	    try (java.sql.Connection conn = com.groupware.util.DBConnection.getConnection();
	         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, status);
	        pstmt.setInt(2, targetEmpNo);

	        int count = pstmt.executeUpdate();
	        if (count > 0) {
	            result = true;
	        }
	        
	        System.out.println("(updateManagerStatus) 관리자 상태 변경 완료 [대상:" + targetEmpNo + ", 상태:" + status + "]");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return result;
	}
	
	/*
	 * 0526 [비밀번호 변경 기능 추가]
	 * 1. 보안을 위해 현재 비밀번호(currentPw)가 DB값과 일치하는지 WHERE절에서 함께 검증합니다.
	 * 2. 일치하는 행이 있을 경우에만 UPDATE가 수행되며, 업데이트된 행의 개수를 통해 성공 여부를 반환합니다.
	 */
	public boolean updatePasswordWithVerify(int empNo, String currentPw, String newPw) {
		boolean result = false;
		// 사번과 현재 비밀번호가 모두 일치하는 경우에만 비밀번호를 새 값으로 갱신합니다.
		String sql = "UPDATE EMPLOYEE SET EMP_PW = ? WHERE EMP_NO = ? AND EMP_PW = ?";

		try (java.sql.Connection conn = com.groupware.util.DBConnection.getConnection(); 
			 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, newPw); // 새 비밀번호
			pstmt.setInt(2, empNo);    // 사번
			pstmt.setString(3, currentPw); // 현재 비밀번호 확인

			// executeUpdate()는 쿼리 수행 후 영향을 받은 행의 개수를 반환합니다.
			// 1이면 비밀번호 변경 성공, 0이면 현재 비밀번호가 틀려 변경 실패를 의미합니다.
			if (pstmt.executeUpdate() > 0) {
				result = true;
			}
			
			System.out.println("(updatePasswordWithVerify) 비밀번호 변경 시도 - 사번: " + empNo + ", 결과: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
