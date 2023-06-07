package bipa.edu.dto;

import java.util.Date;

public class ReservationDTO {
    private int resevationNo;
    private Date reservationAt;
    private String memberId;
    private int bookNo;

    public ReservationDTO(int resevationNo, Date reservationAt, String memberId, int bookNo) {
        this.resevationNo = resevationNo;
        this.reservationAt = reservationAt;
        this.memberId = memberId;
        this.bookNo = bookNo;
    }

    public int getReservationNo() {
        return resevationNo;
    }

    public Date getReservationAt() {
        return reservationAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getBookNo() {
        return bookNo;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
            "resevationNo=" + resevationNo +
            ", reservationAt=" + reservationAt +
            ", memberId='" + memberId + '\'' +
            ", bookNo=" + bookNo +
            '}';
    }
}
