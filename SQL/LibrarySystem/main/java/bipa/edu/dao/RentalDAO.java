package bipa.edu.dao;


import bipa.edu.dto.BookDTO;
import bipa.edu.util.ConnectionManager;
import bipa.edu.util.GetUser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class RentalDAO {
    String sql = "";
    String sql2 = "";
    String answer = "";
    int intAnswer;


    Connection conn = ConnectionManager.getConnection();

    public int isRental(int bookID) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = String.format("SELECT COUNT(*) AS count FROM rental\n" +
                "WHERE book_no = '%d';", bookID);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //Read
            while (rs.next()) {
                intAnswer = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return intAnswer;
    }

    public String getPenaltyDate() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = String.format("SELECT penalty_time FROM member WHERE member_id = '%s';",
                GetUser.getId());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //Read
            while (rs.next()) {
                answer = rs.getString("penalty_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return answer;
    }

    public int maxRental() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = String.format("SELECT\n" +
                "    CASE\n" +
                "        WHEN grade = 1 THEN 3\n" +
                "        WHEN grade = 2 THEN 10\n" +
                "        WHEN grade = 3 THEN 15\n" +
                "    END AS max_book\n" +
                "FROM member\n" +
                "WHERE penalty_time = 0\n" +
                "AND member_id = '%s';", GetUser.getId());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //Read
            while (rs.next()) {
                intAnswer = rs.getInt("max_book");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return intAnswer;
    }

    public int countRental() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = String.format("SELECT COUNT(*) AS rental_count FROM rental " +
                "WHERE member_id = '%s';", GetUser.getId());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //Read
            while (rs.next()) {
                intAnswer = rs.getInt("rental_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return intAnswer;
    }

    //3. 대출이 가능하다면, book과 rental에 저장. 단, 저장날짜는 +7로 한다. 또한 책에 있는 rental_count++할 것.
    public int rentalBook(int bookID) throws SQLException {
        int result = 0;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = String.format(
                "INSERT INTO rental (return_date, member_id, book_no) VALUES (DATE_ADD(NOW(), INTERVAL 7 DAY), '%s', %d);",
                GetUser.getId(), bookID);
            sql2 = String.format("UPDATE book SET rental_count = rental_count + 1\n" +
                "WHERE book_no = '%d';", bookID);

            stmt = conn.createStatement();

            result = stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return result;
    }

    public int updatePenalty() throws SQLException {
        int result = 0;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sql = "UPDATE member SET penalty_time = penalty_time-1\n" +
                "WHERE penalty_time != 0;";

            stmt = conn.createStatement();

            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

}