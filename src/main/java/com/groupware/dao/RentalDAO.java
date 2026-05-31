package com.groupware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.groupware.dto.RentalHistoryDTO;
import com.groupware.util.DBConnection;

public class RentalDAO {

	/* 📌 [오류 해결 완료] 물음표 개수 불일치 및 인덱스 누락 완벽 교정 */
    public boolean insertRental(RentalHistoryDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtEq = null;

        // 💡 [핵심 교정]: 컬럼명은 총 19개입니다. VALUES 뒤의 물음표(?) 개수를 정확히 19개로 복구했습니다.
        String sql = "INSERT INTO RENTAL_HISTORY (RENTAL_NO, TITLE, EMP_NO, EQ_NO, RENTAL_DATE, RETURN_DATE, STATUS, APPROVAL_STEP, "
                   + "SIGN1, SIGN1_DATE, SIGN2, SIGN2_DATE, SIGN3, SIGN3_DATE, SIGN4, SIGN4_DATE, SIGN5, SIGN5_DATE, REQ_COUNT, REASON) "
                   + "VALUES (SEQ_RENTAL.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            pstmt = conn.prepareStatement(sql);
            
            // 💡 1번부터 19번까지의 파라미터가 유실 없이 100% 매칭됩니다.
            pstmt.setString(1, dto.getTitle());
            pstmt.setInt(2, dto.getEmpNo());
            pstmt.setInt(3, dto.getEqNo());
            pstmt.setDate(4, dto.getRentalDate());
            pstmt.setDate(5, dto.getReturnDate());
            pstmt.setString(6, dto.getStatus());
            pstmt.setInt(7, dto.getApprovalStep());
            
            pstmt.setString(8, dto.getSign1());
            pstmt.setDate(9, dto.getSign1Date());
            pstmt.setString(10, dto.getSign2());
            pstmt.setDate(11, dto.getSign2Date());
            pstmt.setString(12, dto.getSign3());
            pstmt.setDate(13, dto.getSign3Date());
            pstmt.setString(14, dto.getSign4());
            pstmt.setDate(15, dto.getSign4Date());
            pstmt.setString(16, dto.getSign5());
            pstmt.setDate(17, dto.getSign5Date());
            
            pstmt.setInt(18, dto.getReqCount()); 
            // 📌 [교정 확인]: 19번째 파라미터인 대여 사유(REASON 컬럼)에 정상 바인딩됩니다.
            pstmt.setString(19, dto.getContent()); 

            int count = pstmt.executeUpdate();

            if (count > 0) {
                // 📌 [변경]: 상태와 관계없이 기안서 등록 성공 시 무조건 비품 수량을 즉시 차감합니다.
                String updateEqSql = "UPDATE EQUIPMENT SET REMAIN_COUNT = REMAIN_COUNT - ? WHERE EQ_NO = ? AND REMAIN_COUNT >= ?";
                pstmtEq = conn.prepareStatement(updateEqSql);
                pstmtEq.setInt(1, dto.getReqCount());
                pstmtEq.setInt(2, dto.getEqNo());
                pstmtEq.setInt(3, dto.getReqCount()); // 재고가 신청 수량보다 많을 때만 실행되도록 안전 가드
                
                int eqCount = pstmtEq.executeUpdate();
                if (eqCount > 0) {
                    conn.commit(); // 기안 등록 + 재고 차감 둘 다 성공 시 커밋
                    result = true;
                } else {
                    conn.rollback(); // 재고 부족 등의 이유로 차감 실패 시 전체 롤백
                }
            } else {
                conn.rollback(); // 기안 등록 자체 실패 시 롤백
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            if (pstmtEq != null) try { pstmtEq.close(); } catch(Exception e) {}
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    public List<RentalHistoryDTO> getAllDocumentList() {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT h.RENTAL_NO, h.TITLE, h.REASON, h.EMP_NO, h.EQ_NO, h.RENTAL_DATE, h.RETURN_DATE, "
                + "CASE WHEN h.STATUS = '대여중' AND h.RETURN_DATE < TRUNC(SYSDATE) THEN '미반납' ELSE h.STATUS END AS STATUS, "
                + "h.APPROVAL_STEP, h.SIGN1, h.SIGN1_DATE, h.SIGN2, h.SIGN2_DATE, h.SIGN3, h.SIGN3_DATE, "
                + "h.SIGN4, h.SIGN4_DATE, h.SIGN5, h.SIGN5_DATE, h.REQ_COUNT, "
                + "e.EMP_NAME, e.EMP_LEVEL, eq.EQ_NAME, eq.TOTAL_COUNT, eq.REMAIN_COUNT "
                + "FROM RENTAL_HISTORY h "
                + "LEFT JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
                + "LEFT JOIN EQUIPMENT eq ON h.EQ_NO = eq.EQ_NO "
                + "ORDER BY h.RENTAL_NO DESC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    RentalHistoryDTO dto = mapResultSetToDTO(rs, true);
                    list.add(dto);
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return list;
    }

    public RentalHistoryDTO getDocumentDetail(int rentalNo) {
        RentalHistoryDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

     // 💡 h.REASON 컬럼을 명시하여 데이터베이스에서 사유를 확실하게 받아옵니다.
        String sql = "SELECT h.RENTAL_NO, h.TITLE, h.REASON, h.EMP_NO, h.EQ_NO, h.RENTAL_DATE, h.RETURN_DATE, "
                + "CASE WHEN h.STATUS = '대여중' AND h.RETURN_DATE < TRUNC(SYSDATE) THEN '미반납' ELSE h.STATUS END AS STATUS, "
                + "h.APPROVAL_STEP, h.SIGN1, h.SIGN1_DATE, h.SIGN2, h.SIGN2_DATE, h.SIGN3, h.SIGN3_DATE, "
                + "h.SIGN4, h.SIGN4_DATE, h.SIGN5, h.SIGN5_DATE, h.REQ_COUNT, "
                + "e.EMP_NAME, e.EMP_LEVEL, eq.EQ_NAME, eq.TOTAL_COUNT, eq.REMAIN_COUNT "
                + "FROM RENTAL_HISTORY h "
                + "LEFT JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
                + "LEFT JOIN EQUIPMENT eq ON h.EQ_NO = eq.EQ_NO "
                + "WHERE h.RENTAL_NO = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, rentalNo);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    dto = mapResultSetToDTO(rs, true);
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return dto;
    }

    /* 📌 2. 결재 승인/반려 제어 로직 수정 (최종 승인 시에는 재고 변동 X, '반려' 시에만 재고 환원) */
    public boolean processApproval(int rentalNo, int eqNo, int step, String empName, boolean isApprove) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtEq = null;

        String signCol = "SIGN" + step;
        String dateCol = "SIGN" + step + "_DATE";
        
        // 승인 시 최종 5단계면 '대여중', 그 전 단계면 '승인대기'유지 / 반려 시 '반려됨'
        String status = isApprove ? (step == 5 ? "대여중" : "승인대기") : "반려됨";
        int nextStep = isApprove ? step + 1 : step;

        String sql = "UPDATE RENTAL_HISTORY SET " + signCol + " = ?, " + dateCol + " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
        
        // 📌 [변경]: 기안이 반려되었을 때(isApprove == false), 선점했던 수량(REQ_COUNT)만큼 재고를 다시 더해줍니다(+).
        String refundEqSql = "UPDATE EQUIPMENT SET REMAIN_COUNT = REMAIN_COUNT + (SELECT REQ_COUNT FROM RENTAL_HISTORY WHERE RENTAL_NO = ?) "
                           + "WHERE EQ_NO = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empName);
            pstmt.setString(2, status);
            pstmt.setInt(3, nextStep);
            pstmt.setInt(4, rentalNo);
            int count = pstmt.executeUpdate();

            if (count > 0) {
                if (!isApprove) {
                    // 📌 [반려 상황]: 결재 상태가 반려로 바뀌었다면 묶여있던 재고 복구 실행
                    pstmtEq = conn.prepareStatement(refundEqSql);
                    pstmtEq.setInt(1, rentalNo);
                    pstmtEq.setInt(2, eqNo);
                    
                    int eqCount = pstmtEq.executeUpdate();
                    if (eqCount > 0) {
                        conn.commit();
                        result = true;
                    } else {
                        conn.rollback();
                    }
                } else {
                    // [승인 상황]: 이미 신청 단계에서 수량을 깎았으므로, 결재 단계 승인 시에는 별도의 재고 수정 없이 상태만 업데이트하고 커밋합니다.
                    conn.commit();
                    result = true;
                }
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            if (pstmtEq != null) try { pstmtEq.close(); } catch(Exception e) {}
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    public List<RentalHistoryDTO> getPendingList(int managerLevel) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM RENTAL_HISTORY WHERE STATUS = '승인대기' AND APPROVAL_STEP = ? ORDER BY RENTAL_DATE ASC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, managerLevel);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs, false));
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return list;
    }

    public List<RentalHistoryDTO> getMyRentalList(int empNo) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT h.RENTAL_NO, h.TITLE, h.EMP_NO, h.EQ_NO, h.RENTAL_DATE, h.RETURN_DATE, "
                + "CASE WHEN h.STATUS = '대여중' AND h.RETURN_DATE < TRUNC(SYSDATE) THEN '미반납' ELSE h.STATUS END AS STATUS, "
                + "h.APPROVAL_STEP, h.SIGN1, h.SIGN1_DATE, h.SIGN2, h.SIGN2_DATE, h.SIGN3, h.SIGN3_DATE, "
                + "h.SIGN4, h.SIGN4_DATE, h.SIGN5, h.SIGN5_DATE, h.REQ_COUNT, e.EQ_NAME "
                + "FROM RENTAL_HISTORY h JOIN EQUIPMENT e ON h.EQ_NO = e.EQ_NO "
                + "WHERE h.EMP_NO = ? ORDER BY h.RENTAL_NO DESC";
        
        
        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, empNo);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs, false));
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return list;
    }

    public boolean updateStatus(int rentalNo, String status) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtRestore = null;

        String sql = "UPDATE RENTAL_HISTORY SET STATUS = ? WHERE RENTAL_NO = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, rentalNo);

            int count = pstmt.executeUpdate();
            
            // ★ 반납 완료 시 수량 복구 (재고 더하기)
            if (count > 0 && "반납완료".equals(status)) {
                String restoreSql = "UPDATE EQUIPMENT SET REMAIN_COUNT = REMAIN_COUNT + (SELECT REQ_COUNT FROM RENTAL_HISTORY WHERE RENTAL_NO = ?) "
                                  + "WHERE EQ_NO = (SELECT EQ_NO FROM RENTAL_HISTORY WHERE RENTAL_NO = ?)";
                pstmtRestore = conn.prepareStatement(restoreSql);
                pstmtRestore.setInt(1, rentalNo);
                pstmtRestore.setInt(2, rentalNo);
                pstmtRestore.executeUpdate();
            }

            if (count > 0) {
                conn.commit();
                result = true;
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            if (pstmtRestore != null) try { pstmtRestore.close(); } catch(Exception e) {}
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    public boolean processStepApproval(int rentalNo, int currentStep, String managerName, String action) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "";

            if ("approve".equals(action)) {
                if (currentStep < 5) {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN" + currentStep + " = ?, SIGN" + currentStep + "_DATE = SYSDATE, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, currentStep + 1);
                    pstmt.setInt(3, rentalNo);
                } else {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN5 = ?, SIGN5_DATE = SYSDATE, STATUS = '대여중' WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, rentalNo);
                }
            } else {
                sql = "UPDATE RENTAL_HISTORY SET SIGN1=NULL, SIGN1_DATE=NULL, SIGN2=NULL, SIGN2_DATE=NULL, SIGN3=NULL, SIGN3_DATE=NULL, SIGN4=NULL, SIGN4_DATE=NULL, SIGN5=NULL, SIGN5_DATE=NULL, "
                        + "APPROVAL_STEP = 1, STATUS = '반려됨' WHERE RENTAL_NO = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, rentalNo);
            }

            int count = pstmt.executeUpdate();
            if (count > 0) result = true;

        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, null); }
        return result;
    }

    // 공통 매핑 유틸리티 (중복 코드 제거)
    private RentalHistoryDTO mapResultSetToDTO(ResultSet rs, boolean includeJoinFields) throws Exception {
        RentalHistoryDTO dto = new RentalHistoryDTO();
        dto.setRentalNo(rs.getInt("RENTAL_NO"));
        dto.setEmpNo(rs.getInt("EMP_NO"));
        dto.setEqNo(rs.getInt("EQ_NO"));
        dto.setRentalDate(rs.getDate("RENTAL_DATE"));
        dto.setReturnDate(rs.getDate("RETURN_DATE"));
        dto.setStatus(rs.getString("STATUS"));
        dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
        dto.setSign1(rs.getString("SIGN1"));
        dto.setSign2(rs.getString("SIGN2"));
        dto.setSign3(rs.getString("SIGN3"));
        dto.setSign4(rs.getString("SIGN4"));
        dto.setSign5(rs.getString("SIGN5"));
        dto.setTitle(rs.getString("TITLE"));
     // 📌 [수정]: 이제 DB에 REASON 컬럼이 존재하므로 가드 없이 깨끗하게 데이터를 수령합니다.
        dto.setContent(rs.getString("REASON"));
        dto.setSign1Date(rs.getDate("SIGN1_DATE"));
        dto.setSign2Date(rs.getDate("SIGN2_DATE"));
        dto.setSign3Date(rs.getDate("SIGN3_DATE"));
        dto.setSign4Date(rs.getDate("SIGN4_DATE"));
        dto.setSign5Date(rs.getDate("SIGN5_DATE"));
        dto.setReqCount(rs.getInt("REQ_COUNT")); // ★ 수량 매핑

        // JOIN 필드가 있는 경우 처리
        if (includeJoinFields) {
            dto.setEmpName(rs.getString("EMP_NAME"));
            dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
            dto.setEqName(rs.getString("EQ_NAME"));
            dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
            dto.setRemainCount(rs.getInt("REMAIN_COUNT"));
        } else {
            // MyRentalList에서 조인으로 EQ_NAME만 가져올 경우 대비
            try { dto.setEqName(rs.getString("EQ_NAME")); } catch (Exception e) {}
        }
        return dto;
    }

    private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}