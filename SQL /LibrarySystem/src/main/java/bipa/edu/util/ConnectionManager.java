package bipa.edu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection conn;
    public static Connection getConnection() {
        if(conn != null) {
            return conn;
        }
        try {
            //JDBC Driver 등록
            Class.forName("com.mysql.cj.jdbc.Driver");

            //연결하기
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bipa_library",
                "root",
                "9999"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
                System.out.println("===================================연결 종료==================================");
            } catch ( SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
