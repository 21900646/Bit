package bitedu.bipa.lesson1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestCenter {
	public static void main(String[] args) {
		TestCenter tc = new TestCenter();
		tc.startTest();
	}
	
	public void startTest() {
		System.out.println("시험지를 배부합니다.");
		ArrayList<Person> data = null;
		data = this.readyData(); // 객체에 저장.
		
		System.out.println("시험을 시작합니다.");
		GisaQuiz quiz = new GisaQuiz(data);
		
		String answer = null;
		answer = quiz.solveQuiz1();
		this.submitAnswer(1, answer);
		
		answer = quiz.solveQuiz2();
		this.submitAnswer(2, answer);
		
		answer = quiz.solveQuiz3();
		this.submitAnswer(3, answer);
		
		answer = quiz.solveQuiz4();
		this.submitAnswer(4, answer);
		
		System.out.println("답안지를 모두 제출합니다.");
		System.out.println("시험을 종료합니다.");
	}
	
	private ArrayList<Person> readyData() {
        File csv = new File("C://Users//HE-Jeon//Desktop//더존비즈온//교육//practice//Abc1115.csv"); //경로 바꾸기!!
        BufferedReader br = null;
        String line = "";

        ArrayList<Person> persons = new ArrayList<>();

        try{
            br = new BufferedReader(new FileReader(csv));
            while((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ", ");
                persons.add(new Person(Integer.parseInt(st.nextToken()),
                    st.nextToken(),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    st.nextToken(),
                    st.nextToken(),
                    st.nextToken()
                ));
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return persons;
	}

	private void submitAnswer(int num, String answer) {
		System.out.println(num+"번째 답변은 "+answer);
	}
}
