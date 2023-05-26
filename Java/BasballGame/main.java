//main.java

package Baseball;

import java.util.*;

public class main {
	public static void main(String[] args) {
		int[] rightAns = new int[4]; //computer num
		int[] result = new int[2]; // 0:strie, 1:ball
		int[] userNums;
		
		Scanner input = new Scanner(System.in);
		
		GameReady gameReady = new GameReady();
	    rightAns = gameReady.ready();
		
	     
		while(true) {
			
			// input
			while(true) {
				int flag = 0;
				
				System.out.print("숫자 4자리를 입력하세요>>>> ");
				String inputNum = input.nextLine();
				String[] userAns = inputNum.split("");
				userNums = Arrays.stream(userAns).mapToInt(Integer::parseInt).toArray();
				
				// user 중복처리
				for(int i=0; i<4; i++) {
					for(int j = 0; j<i; j++) {
						if(userNums[i] == userNums[j]) {
							System.out.println("중복되지 않는 숫자로 다시 입력해주세요.");
							flag ++;
						}
					}
				}
				if(flag == 0) break;
			}
			
			
			// 판정
			result = gameReady.compareCard(userNums, rightAns);
			System.out.println("\n<<<<<게임결과>>>>>");
			System.out.println(result[0] + "strike " + result[1] + "ball입니다!!!!");
			
			
			// 4 strike라면
			if(result[0] == 4) {
				System.out.println("정답입니다!!!");
				break;
			}
			
			// 게임 진행여부 체크
			System.out.print("\n게임을 계속 하시겠습니까?[Y/N] >>>>>>> ");
			String finish = input.nextLine();
			if(finish.equals("N")) {
				break;
			}
		}
		
		System.out.println("게임이 종료되었습니다. ");
	}
}
