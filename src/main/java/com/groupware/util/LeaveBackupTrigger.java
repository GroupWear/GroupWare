package com.groupware.util;

import org.h2.api.Trigger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeaveBackupTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        String action = (oldRow == null) ? "INSERT" : "UPDATE";
        Object[] targetRow = (newRow != null) ? newRow : oldRow;

        String sql = "INSERT INTO LEAVE_HISTORY_BACKUP (ACTION_TYPE, LEAVE_NO, EMP_NO, START_DATE, END_DATE, USE_DAYS, REASON, STATUS, APPROVAL_STEP, SIGN1, SIGN1_DATE, SIGN2, SIGN2_DATE, SIGN3, SIGN3_DATE, SIGN4, SIGN4_DATE, SIGN5, SIGN5_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            
            // LEAVE_HISTORY의 모든 컬럼을 동적으로 매핑
            for (int i = 0; i < targetRow.length; i++) {
                pstmt.setObject(i + 2, targetRow[i]);
            }
            pstmt.executeUpdate();
        }
    }

    @Override public void close() {}
    @Override public void remove() {}
}