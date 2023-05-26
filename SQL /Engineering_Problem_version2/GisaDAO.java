package bitedu.lesson2.baseball;

import java.sql.*;

public class GisaDAO {
	//DB관련 작업 전용 클래스
    String dbDriver = "com.mysql.cj.jdbc.Driver";
    String dbUrl = "jdbc:mysql://localhost:3306/student";
    String dbUser = "java";
    String dbPassword = "akdldptmzbdpf0!";
    
    
	//1번 문제
	public int selectQuiz(String sql){
		int stdNo = 0;
		
		try {
			Connection con = this.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				stdNo = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			con.close();
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return stdNo;
	}
	
	
	public Connection getConnection() {
		Connection connection = null;
        java.sql.Statement stmt = null;
        
        try 
        {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("DB Connection [성공]");
        } 
        catch (SQLException e) 
        {
            System.out.println("DB Connection [실패]");
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("DB Connection [실패]");
            e.printStackTrace();
        }
        return connection;
	}
}
