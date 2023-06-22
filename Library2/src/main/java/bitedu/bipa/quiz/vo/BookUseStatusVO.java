package bitedu.bipa.quiz.vo;

import java.sql.Timestamp;

import bitedu.bipa.quiz.util.DateCalculation;

public class BookUseStatusVO {
	private int bookSeq;
	private String bookIsbn;
	private String bookTitle;
	private String bookAuthor;
	private String userId;
	private Timestamp borrowStart;
	private Timestamp borrowEnd;
	private Timestamp returnDate;
	
	public BookUseStatusVO(int bookSeq, String userId, Timestamp borrowStart, Timestamp borrowEnd,
			Timestamp returnDate) {
		super();
		this.bookSeq = bookSeq;
		this.userId = userId;
		this.borrowStart = borrowStart;
		this.borrowEnd = borrowEnd;
		this.returnDate = returnDate;
	}
	
	
	public int getBookSeq() {
		return bookSeq;
	}
	public void setBookSeq(int bookSeq) {
		this.bookSeq = bookSeq;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Timestamp getBorrowStart() {
		return borrowStart;
	}
	public void setBorrowStart(Timestamp borrowStart) {
		this.borrowStart = borrowStart;
	}
	public Timestamp getBorrowEnd() {
		return borrowEnd;
	}
	public void setBorrowEnd(Timestamp borrowEnd) {
		this.borrowEnd = borrowEnd;
	}
	public Timestamp getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}
	
	
	public String getBookIsbn() {
		return bookIsbn;
	}


	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}


	public String getBookTitle() {
		return bookTitle;
	}


	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}


	public String getBookAuthor() {
		return bookAuthor;
	}


	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}


	@Override
	public String toString() {
		return "{\"bookSeq\":\"" + bookSeq + "\", \"bookIsbn\":\"" + bookIsbn + "\", \"bookTitle\":\"" + bookTitle
				+ "\", \"bookAuthor\":\"" + bookAuthor + "\", \"userId\":\"" + userId + "\", \"borrowStart\":\"" + DateCalculation.getDate(borrowStart)
				+ "\", \"borrowEnd\":\"" + DateCalculation.getDate(borrowEnd) + "\", \"returnDate\":\"" + DateCalculation.getDate(returnDate) + "\"}";
	}

	
}
