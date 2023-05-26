package bitedu.lesson2.baseball;

import java.util.ArrayList;
import java.util.Comparator;



public class GisaQuiz {
	private GisaDAO dao;
	
	public GisaQuiz() {
		dao = new GisaDAO();
	}

	
	public String solveQuiz1() {
		String answer = null;
		
//		StringBuffer sb = new StringBuffer("SELECT ID FROM stud_table\r\n" + 
//				"WHERE RegionCode = 'B'\r\n" + 
//				"ORDER BY Kor + Eng desc, ID asc\r\n" + 
//				"LIMIT 4, 1;");
//		String sql = sb.toString();
		
		String sql = "SELECT ID FROM stud_table\r\n" + 
				"WHERE RegionCode = 'B'\r\n" + 
				"ORDER BY Kor + Eng desc, ID asc\r\n" + 
				"LIMIT 4, 1;";
		
		int stdNo = dao.selectQuiz(sql);
		answer = String.valueOf(stdNo);
		return answer;
	}
	
	
	public String solveQuiz2() {
		String answer = null;
		
		String sql = "SELECT Kor + Eng as sum FROM stud_table\r\n" + 
				"WHERE RegionCode = \"B\"\r\n" + 
				"ORDER BY Kor + Eng DESC\r\n" + 
				"LIMIT 1;";
		
		int stdNo = dao.selectQuiz(sql);
		answer = String.valueOf(stdNo);
		
		return answer;
	}
	
	
	public String solveQuiz3() {
		String answer = null;
		
		String sql = "SELECT SUM(Total + Point) AS SUM FROM (SELECT *,\r\n" + 
				"CASE\r\n" + 
				"WHEN ABC = 'A' THEN 5\r\n" + 
				"WHEN ABC = 'B' THEN 15\r\n" + 
				"WHEN ABC = 'C' THEN 20\r\n" + 
				"END AS Point\r\n" + 
				"FROM stud_table\r\n" + 
				"WHERE Eng + Math >= 120\r\n" + 
				") A;";
		
		int stdNo = dao.selectQuiz(sql);
		answer = String.valueOf(stdNo);
		
		return answer;
	}

	public String solveQuiz4() {
		String answer = null;
		
		String sql = "SELECT COUNT(ID) AS sum FROM (SELECT *,\r\n" + 
				"CASE\r\n" + 
				"WHEN RegionCode = 'A' THEN 5\r\n" + 
				"WHEN RegionCode = 'B' THEN 10\r\n" + 
				"WHEN RegionCode = 'C' THEN 15\r\n" + 
				"END AS Point\r\n" + 
				"FROM stud_table\r\n" + 
				"WHERE ABC = \"A\" OR ABC = \"B\") A\r\n" + 
				"WHERE Kor + point >= 50;";
		
		int stdNo = dao.selectQuiz(sql);
		answer = String.valueOf(stdNo);
		
		return answer;
	}
	
}
