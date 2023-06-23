package bitedu.bipa.quiz.vo;

public class UserBookStatusVO {
	// 대출도서권수, 반납도서, 미반납도서, 반납예정도서, 대출가능권수 이용상태 대출정지기간의 정보를 정리
	private String userId;
	private String userState;
	private String stopDate;
	private int totalUsingBook;
	private int returnedBook;
	private int notReturnedBook;
	private int expectingReturnBook;
	private int availableBook;
	
	public UserBookStatusVO() {}
	public UserBookStatusVO(String userId, String userState, String stopDate, int totalUsingBook, int returnedBook,
			int notReturnedBook, int expectingReturnBook, int availableBook) {
		super();
		this.userId = userId;
		this.userState = userState;
		this.stopDate = stopDate;
		this.totalUsingBook = totalUsingBook;
		this.returnedBook = returnedBook;
		this.notReturnedBook = notReturnedBook;
		this.expectingReturnBook = expectingReturnBook;
		this.availableBook = availableBook;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserState() {
		return userState;
	}
	public void setUserState(String userState) {
		this.userState = userState;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public int getTotalUsingBook() {
		return totalUsingBook;
	}
	public void setTotalUsingBook(int totalUsingBook) {
		this.totalUsingBook = totalUsingBook;
	}
	public int getReturnedBook() {
		return returnedBook;
	}
	public void setReturnedBook(int returnedBook) {
		this.returnedBook = returnedBook;
	}
	public int getNotReturnedBook() {
		return notReturnedBook;
	}
	public void setNotReturnedBook(int notReturnedBook) {
		this.notReturnedBook = notReturnedBook;
	}
	public int getExpectingReturnBook() {
		return expectingReturnBook;
	}
	public void setExpectingReturnBook(int expectingReturnBook) {
		this.expectingReturnBook = expectingReturnBook;
	}
	public int getAvailableBook() {
		return availableBook;
	}
	public void setAvailableBook(int availableBook) {
		this.availableBook = availableBook;
	}
	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\", \"userState\":\"" + userState + "\", \"stopDate\":\"" + stopDate
				+ "\", \"totalUsingBook\":\"" + totalUsingBook + "\", \"returnedBook\":\"" + returnedBook + "\", \"notReturnedBook\":\""
				+ notReturnedBook + "\", \"expectingReturnBook\":\"" + expectingReturnBook + "\", \"availableBook\":\"" + availableBook
				+ "\"}";
	}
	
	
}
