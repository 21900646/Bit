package bitedu.lesson1.baseball;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class DatabaseWork {
	
	public void insertData(ArrayList<Person> data) throws SQLException {
		try {
            this.insert(data);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection con = null;
		String jdbcURL = "jdbc:mysql://localhost:3306/student";
		String driver = "com.mysql.cj.jdbc.Driver";
		String id = "java";
		String password = "akdldptmzbdpf0!";
		
		Class.forName(driver);
		con = DriverManager.getConnection(jdbcURL, id, password);
		
		return con;
	}
	
	private void insert(ArrayList<Person> list) throws ClassNotFoundException, SQLException {
		Connection con = this.getConnection();
		//SQL 작성
		String sql = "insert into stud_table values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		//쿼리 전송 통로 생성
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		for(Person dto : list) {
			pstmt.setInt(1, dto.getID());
			pstmt.setString(2, dto.getEmail());
			pstmt.setInt(3, dto.getKor());
			pstmt.setInt(4, dto.getEng());
			pstmt.setInt(5, dto.getMath());
			pstmt.setInt(6, dto.getSci());
			pstmt.setInt(7, dto.getSoc());
			pstmt.setInt(8, dto.getTotal());
			pstmt.setString(9, dto.getTeacherCode());
			pstmt.setString(10, dto.getABC());
			pstmt.setString(11, dto.getRegionCode());
			pstmt.executeUpdate();
		}
		
		//통로 정리
		pstmt.close();
		
		//커넥션 정리
		con.close();
	}
	
	private void testConnection() {
		Connection con = null;
		try {
			con = this.getConnection();
			if(con!= null) {
				System.out.println("connected");
				con.close();
			}else {
				System.out.println("fails");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Person> getStudentData() throws ClassNotFoundException, SQLException {
		ArrayList<Person> data = new ArrayList<Person>();

		//DB의 데이터를 가져와서 List로 변경
		
		//connection 생성
		Connection con = this.getConnection();
		
		//쿼리 생성
		String sql = "select * from stud_table";
		
		//쿼리 통로 생성
		Statement stmt = con.createStatement();
		
		//쿼리 실행
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			//쿼리 결과 받고 처리하기(List로 변경작업)
			Person p = new Person(rs.getInt("ID"), rs.getString("Email"), rs.getInt("Kor"), rs.getInt("Eng"),rs.getInt("Math"),rs.getInt("Sci"),rs.getInt("Soc"),rs.getInt("Total"),rs.getString("TeachCode"),rs.getString("ABC"),rs.getString("RegionCode"));
			data.add(p);
		}
		
		//통로 정리
		stmt.close();
		
		//커넥션 정리
		con.close();
		
		return data;
	}
}
