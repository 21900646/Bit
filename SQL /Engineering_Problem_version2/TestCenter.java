package bitedu.lesson2.baseball;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TestCenter {
	public static void main(String[] args) throws SQLException {
		GisaDAO dao = new GisaDAO();
		TestCenter tc = new TestCenter();
		tc.startTest();
	}
	
	
	public void startTest() {
		System.out.println("시험지를 배부합니다.");
		
		System.out.println("시험을 시작합니다.");
		GisaQuiz quiz = new GisaQuiz();
		
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
	
	
	private void submitAnswer(int num, String answer) {
		System.out.println(num+"번째 답변은 "+answer);
	}
}
