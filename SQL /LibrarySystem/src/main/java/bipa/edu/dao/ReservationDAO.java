package bipa.edu.dao;

import bipa.edu.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 양승아
 */
public class ReservationDAO {
    private Connection conn;

    /**
     * 책이 대출된 상태인지 검사하는 메소드
     *
     * @param bookNo 검사할 책 번호
     * @return 대출된 상태면 true, 아니면 false return.
     */
    public boolean isRental(int bookNo) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from Rental where book_no = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookNo);

            rs = pstmt.executeQuery();
            if (rs.next()) {
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

    /**
     * 한 책에 2명이 이미 예약되어 있는지 검사하는 메소드
     *
     * @param bookNo 책 번호
     * @return 예약되어 있으면 true, 아니면 false return.
     */
    public boolean isTwoMemberReservationWithBook(int bookNo) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select count(reservation_no)\n" +
                "from Reservation\n" +
                "where book_no = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookNo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 2) {
                    result = true;
                }
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

    /**
     * 한 사람이 이미 책을 3권 예약했는지 검사하는 메소드
     *
     * @param currentId 로그인 된 member Id.
     * @return 3권 예약되어 있으면 true, 아니면 false return.
     */
    public boolean isThreeBookReservation(String currentId) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select count(reservation_no)\n" +
                "from Reservation\n" +
                "where member_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 3) {
                    result = true;
                }
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

    /**
     * 예약 정보 저장하는 메소드
     *
     * @param bookNo   예약할 책 번호
     * @param memberId 현재 로그인 된 member 의 Id
     */
    public int insert(int bookNo, String memberId) throws SQLException {
        int result = 0;
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into Reservation (reservated_at, member_id, book_no)\n" +
                "values (now(), ?, ?);";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.setInt(2, bookNo);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    public boolean exitReservation(int bookNo, String memberId) throws SQLException {
        boolean result = false;
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select *\n" +
                "from Reservation\n" +
                "where member_id = ? and book_no = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.setInt(2, bookNo);

            rs = pstmt.executeQuery();
            if (rs.next()) {
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
