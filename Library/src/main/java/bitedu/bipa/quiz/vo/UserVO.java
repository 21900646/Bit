package bitedu.bipa.quiz.vo;

import java.sql.Timestamp;

public class UserVO {
	
	private int userSeq;
	private String userId;
	private String userPass;
	private String userPhoneNumber;
	private String userState;
	private String userGrade;
	private int availableBook;
	private Timestamp serviceStop;
	
	public UserVO() {}
	public UserVO(int userSeq, String userId, String userPass, String userPhoneNumber, String userState,
			String userGrade, int availableBook, Timestamp serviceStop) {
		super();
		this.userSeq = userSeq;
		this.userId = userId;
		this.userPass = userPass;
		this.userPhoneNumber = userPhoneNumber;
		this.userState = userState;
		this.userGrade = userGrade;
		this.availableBook = availableBook;
		this.serviceStop = serviceStop;
	}
	
	
	
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	public String getUserState() {
		return userState;
	}
	public void setUserState(String userState) {
		this.userState = userState;
	}
	public String getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}
	
	public int getAvailableBook() {
		return availableBook;
	}
	public void setAvailableBook(int availableBook) {
		this.availableBook = availableBook;
	}
	public Timestamp getServiceStop() {
		return serviceStop;
	}
	public void setServiceStop(Timestamp serviceStop) {
		this.serviceStop = serviceStop;
	}
	@Override
	public String toString() {
		return "{"
				+ "\"userSeq\":\"" + userSeq + "\", \"userId\":\"" + userId + "\", \"userPass\":\"" + userPass + "\", \"userPhoneNumber\":\""
				+ userPhoneNumber + "\", \"userState\":\"" + userState + "\", \"userGrade\":\"" + userGrade + "\", \"serviceStop\":\""
				+ serviceStop + "\"}";
	}
	
}
