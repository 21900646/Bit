package bitedu.lesson1.baseball;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class DatabaseMain {
	public static void main(String[] args) throws IOException, SQLException {
		DatabaseMain main = new DatabaseMain();
		 try {
	        main.makeData(); //읽어서 객체에 넣기.
	      } catch (Exception e) {
	            e.printStackTrace();
	        }
		main.testStart(); //문제 하나씩 하기.
		
	}

	private void makeData() throws IOException, SQLException {
        ArrayList<Person> data = null;

        File file = new File("C://Users//HE-Jeon//Desktop//더존비즈온//교육//자바//practice//Abc1115.csv");
        

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        data = new ArrayList<Person>();
        Person dto = null;
        
        while((line=br.readLine())!=null) {
            String[] temp = line.split(",");
            int stdNo = Integer.parseInt(temp[0].trim());
            String email = temp[1].trim();
            int kor = Integer.parseInt(temp[2].trim());
            int eng = Integer.parseInt(temp[3].trim());
            int math = Integer.parseInt(temp[4].trim());
            int sci = Integer.parseInt(temp[5].trim());
            int hist = Integer.parseInt(temp[6].trim());
            int total = Integer.parseInt(temp[7].trim());
            String mgr = temp[8].trim();
            String acc = temp[9].trim();
            String loc = temp[10].trim();
            data.add(new Person(stdNo,email,kor,eng,math,sci,hist,total,mgr,acc,loc));
        }
        
        DatabaseWork work  = new DatabaseWork();
        work.insertData(data);
	}
	
	

	
	public void testStart() throws IOException, SQLException {
		ArrayList<Person> data = this.readyData();
		GisaQuiz quiz = new GisaQuiz(data);
		String answer = quiz.solveQuiz1();
		System.out.println(answer);
		answer = quiz.solveQuiz2();
		System.out.println(answer);
		answer = quiz.solveQuiz3();
		System.out.println(answer);
		answer = quiz.solveQuiz4();
		System.out.println(answer);
	}
	
	
	private ArrayList<Person> readyData(){
		ArrayList<Person> data = null;
		
		//File처리 대신 DB처리
		DatabaseWork work = new DatabaseWork();
		try {
			data = work.getStudentData();
			for(Person d : data) {
				System.out.println(d);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public void makeTable() throws SQLException {
		try{
			this.makeData();
		} catch (IOException e) {
			
		}
	}

}
