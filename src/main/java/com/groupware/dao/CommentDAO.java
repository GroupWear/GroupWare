package com.groupware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.groupware.dto.CommentDTO;
import com.groupware.util.DBConnection;

public class CommentDAO {

    // 1. 특정 게시글의 댓글 목록 조회
    public List<CommentDTO> getCommentsByBoardNo(int boardNo) {
        List<CommentDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 그룹 번호 내림차순(최신글 상단), 그룹 내 순서 오름차순
        String sql = "SELECT C.*, E.EMP_NAME FROM BOARD_COMMENT C " +
                     "JOIN EMPLOYEE E ON C.EMP_NO = E.EMP_NO " +
                     "WHERE C.BOARD_NO = ? " +
                     "ORDER BY C.GROUP_NO DESC, C.ORDER_NO ASC";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardNo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CommentDTO dto = new CommentDTO();
                dto.setCommentNo(rs.getInt("COMMENT_NO"));
                dto.setBoardNo(rs.getInt("BOARD_NO"));
                dto.setEmpNo(rs.getInt("EMP_NO"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setRegDate(rs.getTimestamp("REG_DATE"));
                dto.setParentNo(rs.getInt("PARENT_NO"));
                if (rs.wasNull()) dto.setParentNo(null);
                dto.setGroupNo(rs.getInt("GROUP_NO"));
                dto.setOrderNo(rs.getInt("ORDER_NO"));
                dto.setDepth(rs.getInt("DEPTH"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return list;
    }

    // 2. 댓글 등록
    public boolean insertComment(CommentDTO comment) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (comment.getParentNo() == null || comment.getParentNo() == 0) {
                // 일반 댓글 (최상위)
                String sql = "INSERT INTO BOARD_COMMENT (COMMENT_NO, BOARD_NO, EMP_NO, CONTENT, GROUP_NO, ORDER_NO, DEPTH) " +
                             "VALUES (SEQ_COMMENT.NEXTVAL, ?, ?, ?, SEQ_COMMENT.CURRVAL, 0, 0)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, comment.getBoardNo());
                pstmt.setInt(2, comment.getEmpNo());
                pstmt.setString(3, comment.getContent());
            } else {
                // 대댓글
                // 1. 부모 댓글 정보 가져오기
                CommentDTO parent = getCommentDetail(comment.getParentNo(), conn);
                
                // 2. 신규 orderNo 결정 및 기존 댓글 순서 밀기
                // 부모의 자식들 중 가장 큰 orderNo를 찾거나, 부모의 orderNo 다음 자리에 삽입
                int newOrderNo = findNewOrderNo(parent, conn);
                
                String sqlUpdate = "UPDATE BOARD_COMMENT SET ORDER_NO = ORDER_NO + 1 WHERE GROUP_NO = ? AND ORDER_NO >= ?";
                PreparedStatement pstmtUpd = conn.prepareStatement(sqlUpdate);
                pstmtUpd.setInt(1, parent.getGroupNo());
                pstmtUpd.setInt(2, newOrderNo);
                pstmtUpd.executeUpdate();
                pstmtUpd.close();

                // 3. 삽입
                String sql = "INSERT INTO BOARD_COMMENT (COMMENT_NO, BOARD_NO, EMP_NO, CONTENT, PARENT_NO, GROUP_NO, ORDER_NO, DEPTH) " +
                             "VALUES (SEQ_COMMENT.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, comment.getBoardNo());
                pstmt.setInt(2, comment.getEmpNo());
                pstmt.setString(3, comment.getContent());
                pstmt.setInt(4, comment.getParentNo());
                pstmt.setInt(5, parent.getGroupNo());
                pstmt.setInt(6, newOrderNo);
                pstmt.setInt(7, parent.getDepth() + 1);
            }

            if (pstmt.executeUpdate() > 0) {
                conn.commit();
                result = true;
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return result;
    }

    // 3. 댓글 수정
    public boolean updateComment(int commentNo, String content) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE BOARD_COMMENT SET CONTENT = ? WHERE COMMENT_NO = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, content);
            pstmt.setInt(2, commentNo);
            if (pstmt.executeUpdate() > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    // 4. 댓글 삭제
    public boolean deleteComment(int commentNo) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM BOARD_COMMENT WHERE COMMENT_NO = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, commentNo);
            if (pstmt.executeUpdate() > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    // 내부 메서드: 댓글 하나 정보 가져오기 (트랜잭션 내 사용)
    private CommentDTO getCommentDetail(int commentNo, Connection conn) throws SQLException {
        CommentDTO dto = null;
        String sql = "SELECT * FROM BOARD_COMMENT WHERE COMMENT_NO = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new CommentDTO();
                    dto.setCommentNo(rs.getInt("COMMENT_NO"));
                    dto.setGroupNo(rs.getInt("GROUP_NO"));
                    dto.setOrderNo(rs.getInt("ORDER_NO"));
                    dto.setDepth(rs.getInt("DEPTH"));
                }
            }
        }
        return dto;
    }

    // 내부 메서드: 대댓글 삽입 위치 찾기
    private int findNewOrderNo(CommentDTO parent, Connection conn) throws SQLException {
        // 부모와 같은 그룹이면서, 부모의 depth보다 큰 (즉 자식/후손) 들 중 부모보다 큰 orderNo를 가진 애들 중 최소값? 
        // 아님 부모 그룹 내에서 부모보다 큰 orderNo를 가진 애들 중 depth가 부모보다 작거나 같은 첫번째 애의 orderNo
        String sql = "SELECT MIN(ORDER_NO) FROM BOARD_COMMENT WHERE GROUP_NO = ? AND ORDER_NO > ? AND DEPTH <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, parent.getGroupNo());
            pstmt.setInt(2, parent.getOrderNo());
            pstmt.setInt(3, parent.getDepth());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int minOrderNo = rs.getInt(1);
                    if (minOrderNo > 0) return minOrderNo;
                }
            }
        }
        // 뒤에 더이상 없으면 그룹 내 최대 orderNo + 1
        String sqlMax = "SELECT MAX(ORDER_NO) FROM BOARD_COMMENT WHERE GROUP_NO = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlMax)) {
            pstmt.setInt(1, parent.getGroupNo());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) + 1;
            }
        }
        return 1;
    }

    private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {}
    }

    // 컨트롤러에서 권한 체크용으로 사용될 상세 조회
    public CommentDTO getComment(int commentNo) {
        CommentDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM BOARD_COMMENT WHERE COMMENT_NO = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, commentNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dto = new CommentDTO();
                dto.setCommentNo(rs.getInt("COMMENT_NO"));
                dto.setBoardNo(rs.getInt("BOARD_NO"));
                dto.setEmpNo(rs.getInt("EMP_NO"));
                dto.setContent(rs.getString("CONTENT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return dto;
    }
}
