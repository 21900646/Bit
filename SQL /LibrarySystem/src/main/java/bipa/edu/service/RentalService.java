package bipa.edu.service;

import bipa.edu.dao.BookDAO;
import bipa.edu.dao.RentalDAO;
import bipa.edu.dto.BookDTO;
import bipa.edu.util.GetUser;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author 전하은
 */
public class RentalService {
    private RentalDAO rentalDAO;
    private BookDAO bookDAO;
    Scanner sc = new Scanner(System.in);

    public RentalService() {
        this.rentalDAO = new RentalDAO();
        this.bookDAO = new BookDAO();
    }

    /**
     * @param book
     * @return 대출 성공 시 true, 아니면 false return.
     */
    public boolean rental(BookDTO book) {
        String answer = "";

        try {
            answer = rentalDAO.getPenaltyDate();

            // 1. 회원인지 확인
            if (GetUser.getId() == null) {
                System.out.println("\n---------------------------------------------");
                System.out.println("실패 : 로그인 해야 이용 가능한 서비스입니다!!");
                System.out.println("---------------------------------------------\n");
                return false;
            }
            // 2. 대출가능한 책인지 확인하기.
            if (rentalDAO.isRental(book.getBookNo()) > 0) {
                System.out.println("\n---------------------------------------------");
                System.out.println("실패 : 해당 책은 이미 대출 상태입니다. ");
                System.out.println("---------------------------------------------\n");
                return false;
            }

            // 3. 대출할 수 있는 유저인지 확인하기. - stop date 확인.
            else if (!answer.equals("0")) {
                System.out.println("\n---------------------------------------------");
                System.out.println("실패 : " + GetUser.getId() + "님은 현재 연체상태입니다. ");
                System.out.println(answer + "일 뒤에 책을 빌리실 수 있습니다.");
                System.out.println("---------------------------------------------\n");
                return false;
            }


            // 4. 현재 유저가 빌릴 수 있는 책 개수를 넘지 않았는지.
            else if (rentalDAO.maxRental() <= rentalDAO.countRental()) {
                System.out.println("\n---------------------------------------------");
                System.out.println(GetUser.getId() + "님은 현재 최대 대출 가능한 개수의 책들을 대출한 상태입니다.");
                System.out.println("도서를 반납한 후에 대출이 가능합니다.");
                System.out.println("---------------------------------------------\n");
                return false;
            }

            // 5. 대출이 가능하다면, book과 rental에 저장. 단, 저장날짜는 +7로 한다. 또한 책에 있는 rental_count++할 것.
            if (rentalDAO.rentalBook(book.getBookNo()) > 0) {
                System.out.println("책 " + book.getName() + "가(이) 성공적으로 대출되었습니다. ");
                return true;
            } else {
                System.out.println("대출에 실패하였습니다.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void returnBook() {
        try {
            System.out.println("---------------------------------------------");
            System.out.println("              << 대출 도서 목록 >>              ");
            System.out.println("---------------------------------------------");

            List<BookDTO> returnlist = bookDAO.getAllRent();
            if (returnlist.isEmpty()) {
                System.out.println("               대출한 책이 없습니다.");
            }
            int cnt = 1;
            for (BookDTO dto : returnlist) {
                System.out.print(cnt++ + ") ");
                System.out.println(dto);
            }
            System.out.println("---------------------------------------------");

            while (true) {
                System.out.print("반납할 도서 번호를 입력하세요 (종료를 원하시면 0을 눌러주세요) >>>> ");
                int returnBookNo = sc.nextInt();
                if (returnBookNo == 0) {
                    break;
                }
                int penaltyResult = bookDAO.savePenaltyTime(returnBookNo);
                int result = bookDAO.returnBook(returnBookNo);
                if (result > 0) {
                    System.out.println("성공적으로 반납하였습니다.");
                    if (penaltyResult > 0) {
                        System.out.println("연체일은 " + bookDAO.getPenaltyTime() + "일 입니다.");
                    }
                    System.out.print("반납을 종료하시겠습니까? [Y/N]");
                    String answer = sc.next();
                    if (answer.equals("Y") || answer.equals("y")) {
                        break;
                    }
                } else {
                    System.out.println("-------------------------------------------");
                    System.out.println("반납에 실패하였습니다. 올바른 책 번호를 입력해주세요");
                    System.out.println("-------------------------------------------");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void penaltyCalTimer() {
        RentalDAO rental = new RentalDAO();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    rental.updatePenalty();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };


        Timer t = new Timer();

        //하루마다 패널티 감소
        //t.schedule(task, new Date(System.currentTimeMillis()),1000*60*60*24);

        // 5초마다 패널티 감소
        t.schedule(task, new Date(System.currentTimeMillis()), 1000 * 5);
    }
}
