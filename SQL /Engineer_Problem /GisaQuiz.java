package bitedu.lesson1.baseball;


import java.util.*;


public class GisaQuiz {
	private ArrayList<Person> dataList;
	
	public GisaQuiz(ArrayList<Person> dataList) {
		this.dataList = dataList;
	}
	
	public String solveQuiz1() {
		// 지역코드가 B인 자료
		// 국어점수 + 영어점수 만들어주기.
		// 내림차순으로 정렬.
		// 다섯번째 학번 출력
		
		ArrayList<Person> regionB = new ArrayList<Person>();
		String answer = null;
		
		for (Person p : dataList) {
			if(p.getRegionCode().equals("B")) {
				regionB.add(p);
			}
		}
		
		regionB.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p0, Person p1) {
                 
                   int sum1 = p0.getKor() + p0.getEng();
                   int sum2 = p1.getKor() + p1.getEng();
                   if (sum1 == sum2)
                         return 0;
                   else if (sum1 < sum2)
                         return 1;
                   else
                         return -1;
           }
        });
		
		answer = Integer.toString(regionB.get(4).getID());
		return answer;
	}
	
	public String solveQuiz2() {
		// 지역코드가 B인 자료
		// 국어점수 + 영어점수 만들어주기.
		// 내림차순으로 정렬.
		// 다섯번째 학번 출력
		int max = 0;
		
		for (Person p : dataList) {
			if(p.getRegionCode().equals("B")) {
				max = Math.max(p.getKor() + p.getEng(), max);
			}
		}

		return Integer.toString(max);
	}
	
	public String solveQuiz3() {
		//(영어점수 + 수학점수)가 120점 이상인 자료
		// (총점 + 점수포인트) 합계를 출력하시오.

		int answer = 0;
		
		for (Person p : dataList) {
			if (p.getEng() + p.getMath() >= 120) {
				if(p.getABC().equals("A")) {
					answer += p.getTotal() + 5;
				}
				if(p.getABC().equals("B")) {
					answer += p.getTotal() + 15;
				}
				if(p.getABC().equals("C")) {
					answer += p.getTotal() + 20;
				}
			}
		}

		return Integer.toString(answer);
	}

	public String solveQuiz4() {
		// 성취도가 A, B인 자료
		// (국어점수 + 지역 점수포인트)의 50 이상인 자료의 건수
		
		ArrayList<Person> regionAB = new ArrayList<Person>();
		int count = 0;
		
		for (Person p : dataList) {
			if(p.getABC().equals("B") || p.getABC().equals("A")) {
				regionAB.add(p);
				if(p.getRegionCode().contentEquals("A")) {
					if(p.getKor() + 5 >= 50) count++;
				}
				if(p.getRegionCode().contentEquals("B")) {
					if(p.getKor() + 10 >= 50) count++;
				}
				if(p.getRegionCode().contentEquals("C")) {
					if(p.getKor() + 15 >= 50) count++;
				}
			}
		}
		
		return Integer.toString(count);
	}
	
}
