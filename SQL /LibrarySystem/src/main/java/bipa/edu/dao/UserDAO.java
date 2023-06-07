package bipa.edu.dao;

import bipa.edu.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection conn;

    /**
     * 로그인 메소드
     * @param id
     * @param password
     * @return 로그인 성공 시 true, 실패 시 false return.
     */
    public boolean login(String id, String password) throws SQLException {
        boolean result  = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select *\n" +
                "from Member\n" +
                "where member_id = ? and password = ? ";

             pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, password);

             rs = pstmt.executeQuery();
            if(rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }
}
