package bit.java.lottomachine;

//데이터클래스(DTO, VO)
public class LottoBall {
	private int number;
	
	// ball한테 중복을 물어봐야함.
	// 뽑힌 정보를 가지고 있어야함.
	private boolean isSelected; 
	
	//get, set
	public int getNumber() {
		return number;
	}
	
	// 또는 private으로 바꾸기. (왜냐, 은닉성에서 벗어나기 때문에)
//	public void setNumber(int number) {
//		this.number = number;
//	}
	
	public LottoBall(int number) {
		this.number = number;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}	

	@Override
	public String toString() {
		return String.valueOf(this.number); // this.number+""
	}
}
