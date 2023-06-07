package bipa.edu.service;

import bipa.edu.dao.ReservationDAO;
import bipa.edu.util.GetUser;
import java.sql.SQLException;

/**
 * @author 양승아
 */
public class ReservationService {
    private ReservationDAO dao;
    private String currentId;

    public ReservationService() {
        this.dao = new ReservationDAO();
    }

    /**
     * 책 예약 메소드
     * login 하지 않았거나
     * 바로 대출이 가능한 상태거나
     * 이미 2명이 예약했거나
     * 본인이 3권을 예약한 상태라면 false return
     * 예약 성공 시 true return
     *
     * @param bookNo 예약할 책 번호
     */
    public boolean reservation(int bookNo) {
        boolean result = false;
        try {
            if ((currentId = GetUser.getId()) == null) {
                System.out.println("실패 : 로그인 해야 이용 가능한 서비스입니다!!");
                return false;
            } else if (dao.exitReservation(bookNo, currentId)) {
                System.out.println("실패 : 이미 예약하신 책입니다.");
                return false;
            }else if (dao.isThreeBookReservation(currentId)) {
                System.out.println("실패 : 이미 3권이 예약된 상태로 더 이상 예약할 수 없습니다.");
                return false;
            } else if (dao.isTwoMemberReservationWithBook(bookNo)) {
                System.out.println("실패 : 예약 가능한 인원이 모두 찼기 때문에 예약할 수 없습니다.");
                return false;
            } else if (!dao.isRental(bookNo)) {
                System.out.println("실패 : 바로 대출 가능한 상태입니다. 대출하기를 선택해주세요.");
                return false;
            }

            if(dao.insert(bookNo, currentId) > 0) {
                System.out.println("성공 : 예약이 정상적으로 완료되었습니다.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("실패 : 예약이 정상적으로 처리되지 않았습니다.");
        return result;
    }
}
