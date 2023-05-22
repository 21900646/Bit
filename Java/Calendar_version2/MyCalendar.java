package bit.java.test1;

import java.util.*;

public class MyCalendar {
    public void viewMonth(int year, int month){
    	
        Calendar cal = Calendar.getInstance();
        
        System.out.println("\n");
        System.out.println(" "+year+"년"+month+" 월");

        
        // day count
     	int[] week = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
     	int[] dayCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
     		
     	for(int i = 1; i<week.length; i++) {
   			dayCount[i] += dayCount[i-1] + week[i-1];
  		}
     		
     	int days = (year-2000)*365 + dayCount[month-1] + (year-2000)/4;
     		
     		
     	// 시작 요일
     	int weekDay = days % 7 + 1;
     		
     		
     	// print
     	System.out.print("일    월    화    수    목    금    토\n");

     		
     	for (int i = 1; i <= week[month-1]; i++) {
     		
     		// 시작 전 빈칸
     		if (i == 1) {
     			for (int j = 1; j< weekDay; j++) {
     				System.out.printf("%.3s", "   ");
     			}
     		}
     		
   			System.out.printf(" " + i);
   			if (i < 10) {
   				System.out.print(" ");
   			}
   			
     		if (weekDay % 7 == 0) {
     			System.out.println();
     		}
     		weekDay ++;
   		}        
        
    }
}
