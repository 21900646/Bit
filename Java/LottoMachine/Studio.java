package bit.java.lottomachine;

import java.util.*;

public class Studio{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Studio sbs = new Studio();
		sbs.onAir();
	}

		//로또 머신과 로또 볼을 준비해야 함.
		public LottoMachine ready() {
			//머신 준비
			LottoMachine machine = new LottoMachine();
			
			//볼 준비
			ArrayList<LottoBall> balls = new ArrayList<LottoBall>();
			for(int i=0; i<45; i++) {
				balls.add(new LottoBall(i+1));
			}
			
			//머신에 볼 넣기
			machine.setBals(balls);
			return machine;
		}
		
		//로또 머신에게 볼을 뽑도록 지시.
		public void onAir() {
			LottoMachine machine = this.ready();
			System.out.println("추첨을 시작합니다.");
			LottoBall[] balls = machine.startMachine();
			//balls의 내용을 출력.
			
			System.out.println("제XXX회 로또 번호는");
			
			//sort하기.
			LottoBall temp;
			for (int i = 0; i < balls.length; i++) {
				for (int j = 0; j < i; j++) {
					if(balls[i].getNumber() < balls[j].getNumber()) {
						temp = balls[i];
						balls[i] = balls[j];
						balls[j] = temp;
					}
				}
			}
			
			for(LottoBall ball : balls) {      // 하나씩 balls리스트에서 꺼내주기.
				System.out.print(ball + "번 ");
			}
			
			System.out.println("이었습니다.");
		}
}
