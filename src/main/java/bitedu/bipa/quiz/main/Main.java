package bitedu.bipa.quiz.main;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Calendar;

import bitedu.bipa.quiz.util.DateCalculation;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.test1();
		//main.test2(LocalDate.now());
	}
	
	public void test1() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(2023, 5, 14);
		System.out.println(c1.getTimeInMillis());
		c2.set(2023 ,5 ,16);
		//System.out.println(c2.getTimeInMillis());
		//c1.add(Calendar.YEAR, 0);
		//c1.add(Calendar.MONTH, 0);
		System.out.println(new Date(c1.getTimeInMillis()));
		//System.out.println(c1.getTimeInMillis());
		//c1.add(Calendar.DAY_OF_MONTH, 5);
		//System.out.println(c1.getTimeInMillis());
		boolean flag = c1.before(c2);
		long aaa = c2.getTimeInMillis() - c1.getTimeInMillis();
		double bbb = aaa/1000/3600/24;
		//System.out.println(c1.get(Calendar.DAY_OF_MONTH)+" "+flag);
		System.out.println(new Date(c1.getTimeInMillis()));
		//;
		//System.out.println(aaa+","+(c2.c1));
		
		Timestamp t1 = new Timestamp(c1.getTimeInMillis());
		Timestamp t2 = new Timestamp(c2.getTimeInMillis());
		int result = DateCalculation.calcuDiffDate(t2, t1);
		System.out.println(result);
	}
	
	public void test2(LocalDate endDate) {
		Date date = new Date(endDate.get(ChronoField.YEAR),endDate.getMonth().getValue()-1,endDate.getDayOfMonth());
		System.out.println(date);
	}

}
