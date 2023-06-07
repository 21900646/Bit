package bipa.edu.dto;

import java.util.Date;

public class BookDTO {
    private int bookNo;
    private String name;
    private int state;

    private int grade;

    private int rentalCount;
    private String writer;
    private Date registerDate;

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public int getBookNo() {
        return bookNo;
    }

    public void setBookNo(int bookNo) {
        this.bookNo = bookNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public BookDTO(int bookNo, String name, int state, int grade, int rentalCount, String writer,
                   Date registerDate) {
        this.bookNo = bookNo;
        this.name = name;
        this.state = state;
        this.grade = grade;
        this.rentalCount = rentalCount;
        this.writer = writer;
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "책 번호 = " + bookNo +
            ", 책 이름 = " + name +
            ", 상태 = " + state +
            ", 등급 = " + grade +
            ", 대출 횟수 = " + rentalCount +
            ", 저자 = " + writer +
            ", 등록일 = " + registerDate;
    }
}
