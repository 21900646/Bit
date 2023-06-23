package bitedu.bipa.quiz.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionManager {
	private static ConnectionManager manager;
	
	private ConnectionManager() {
		
	}
	
	public static ConnectionManager getInstance() {
		if(manager==null) {
			manager = new ConnectionManager();
		}
		return manager;
	}
	
	public Connection getConnection() {
		Connection con = null;
		Properties prop = new Properties();
		try {
			//prop.load(new FileReader(new File("./data/db.properties")));
			prop.load(new FileReader(new File("D:/dev/test/db.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jdbcURL = prop.getProperty("jdbcURL");//"jdbc:mysql://localhost:3306/bitedu";
		String driver = prop.getProperty("driver");//"com.mysql.cj.jdbc.Driver";
		String id = prop.getProperty("id");//"root";
		String pwd = prop.getProperty("pwd");//"1234";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcURL,id,pwd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
						
		return con;
	}
	
	public void closeConnection(ResultSet rs, Statement stmt, Connection con) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
