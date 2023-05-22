package bit.java.lottomachine;

import java.util.*;


//로직클래스
public class LottoMachine {
	private ArrayList<LottoBall> balls;
	
	// 6개의 로또볼을 꺼내겠다.
	public LottoBall[] startMachine() {
		LottoBall[] selectedBalls = null;
		
		selectedBalls = new LottoBall[6];
		
		for(int i=0; i<selectedBalls.length; i++) {
			Collections.shuffle(balls);     //섞여주는 코드
			selectedBalls[i] = this.getBall();  
			System.out.println(selectedBalls[i]+"번이 선택되었습니다.");
			try {
				Thread.sleep(1500);  // 일정간격
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return selectedBalls;
	}
	
	private LottoBall getBall() {
		LottoBall ball = null;
		Random rnd = new Random();
		// 저장소에서 꺼낼 공 결정
		int index = rnd.nextInt(balls.size());
		//공을 꺼낸다
		ball = balls.get(index); // setBalls에서 넣고 나서.
		ball = balls.remove(index);
	return ball;
	}
	
	
	// 공 세팅.
	public void setBals(ArrayList<LottoBall> balls) {
		this.balls = balls;
	}
}
