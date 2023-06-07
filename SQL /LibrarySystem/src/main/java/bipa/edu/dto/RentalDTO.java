package bipa.edu.dto;

import java.util.Date;

public class RentalDTO {
    private int rentalNo;
    private Date returnDate;
    private String memberId;
    private int bookNo;

    public int getRentalNo() {
        return rentalNo;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getBookNo() {
        return bookNo;
    }

    @Override
    public String toString() {
        return "RentalDTO{" +
            "rentalNo=" + rentalNo +
            ", returnDate=" + returnDate +
            ", memberId='" + memberId + '\'' +
            ", bookNo=" + bookNo +
            '}';
    }
}
