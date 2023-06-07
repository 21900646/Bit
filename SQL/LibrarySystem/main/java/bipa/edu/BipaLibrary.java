package bipa.edu;

import bipa.edu.dto.BookDTO;
import bipa.edu.service.*;
import bipa.edu.util.ConnectionManager;
import bipa.edu.util.GetUser;
import java.util.Scanner;

public class BipaLibrary {
    UserService userService;
    BookService bookService;
    ReservationService reservationService;
    RentalService rentalService;
    Scanner scanner;
    GetUser getUser;


    public BipaLibrary() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.rentalService = new RentalService();
        this.reservationService = new ReservationService();
        this.scanner = new Scanner(System.in);
        this.getUser = GetUser.getInstance();
    }

    public static void main(String[] args) {
        BipaLibrary bipaLibrary = new BipaLibrary();
        bipaLibrary.start();
    }

    private void start() {
        // TODO : 시연 시 풀기
        //rentalService.penaltyCalTimer();

        System.out.println("=============================================");
        System.out.println("       < BIPA 도서관에 오신걸 환영합니다! >");
        System.out.println("=============================================");

        boolean isContinue = true;
        while (isContinue) {
            isContinue = startMenu();
        }
        System.out.println("\n< 도서관 서비스를 종료합니다 >");
    }

    private boolean startMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("             << BIPA 도서관 메뉴 >>");
        if(GetUser.getId() != null) {
            System.out.println("( 현재 [" + GetUser.getId() + "] 회원으로 로그인 된 상태입니다 )");
        } else {
            System.out.println("     ( 현재 비회원으로 로그인 되지 않은 상태입니다 )");
        }
        System.out.println("---------------------------------------------");
        System.out.println("1. 검색");
        System.out.println("2. 로그인");
        System.out.println("3. 로그아웃");
        System.out.println("4. 프로그램 종료");
        System.out.println("---------------------------------------------");
        System.out.print("원하는 서비스의 번호를 입력하세요. >>>> ");

        int menu = Integer.parseInt(scanner.next());
        if (menu == 1) {
            menuBookList();
            return true;
        } else if (menu == 2) {
            while (!userService.login()) {
                System.out.println("\n< 로그인에 실패하였습니다. >\n");
            }
            menuAfterLogin();
            return true;
        } else if (menu == 3) {
            if(GetUser.getId() == null) {
                System.out.println("---------------------------------------------");
                System.out.println("실패 : 로그인 되어 있지 않습니다!!");
            } else {
                userService.logout();
            }
            startMenu();
            return true;
        } else if (menu == 4) {
            System.out.println("\n< 도서관 서비스를 종료합니다 >");
            System.exit(0);
            ConnectionManager.closeConnection();
        }
        System.out.println("입력이 잘못되었습니다.");
        return true;
    }

    private void menuAfterLogin() {
        System.out.println("---------------------------------------------");
        System.out.println("1. 대출 / 예약");
        System.out.println("2. 대출 현황 및 반납");
        System.out.println("---------------------------------------------");
        System.out.print("원하는 서비스의 번호를 입력하세요. >>>> ");

        int menu = Integer.parseInt(scanner.next());
        if (menu == 1) {
            menuBookList();
        }
        if (menu == 2) {
            rentalService.returnBook();
            startMenu();
        }
    }

    private void menuBookList() {
        System.out.println("---------------------------------------------");
        System.out.println("1. 신규 도서 목록");
        System.out.println("2. 인기 도서 목록");
        System.out.println("3. 전체 검색");
        System.out.println("4. 초기 화면으로 이동");
        System.out.println("---------------------------------------------");
        System.out.print("원하는 서비스의 번호를 입력하세요. >>>> ");

        int menu = Integer.parseInt(scanner.next());
        if (menu == 1) {
            bookService.recentBook();
            selectRentalOrReservation();
        } else if (menu == 2) {
            bookService.bestBook();
            selectRentalOrReservation();
        } else if (menu == 3) {
            while (true){
                System.out.print("책 이름 또는 작가 이름을 입력하세요 >>>> ");
                String input = scanner.next();
                if(bookService.searchBooksAndPrint(input, input)) break;
            }
            selectRentalOrReservation(); //어차피 로그인 안되면 다 막혀있음 (대출/예약)
        } else if (menu == 4) {
            startMenu();
        }
    }

    private void selectRentalOrReservation() {
        System.out.println("---------------------------------------------");
        System.out.println("1. 대출");
        System.out.println("2. 예약");
        System.out.println("3. 초기 화면");
        System.out.println("---------------------------------------------");
        System.out.print("원하는 서비스의 번호를 입력하세요. >>>> ");

        Scanner scanner = new Scanner(System.in);
        int menu = Integer.parseInt(scanner.next());
        BookDTO bookDTO = null;

        if (menu == 1) {
            while (true) {
                System.out.print("\n대출할 책 번호를 입력하세요. >>>> ");

                if ((bookDTO = bookService.getBookDto(Integer.parseInt(scanner.next()))) != null) {
                    rentalService.rental(bookDTO);
                    break;
                }
                System.out.println("올바른 책 번호가 아닙니다.");
            }
            menuBookList();
        }
        if (menu == 2) {
            System.out.print("예약할 책 번호를 입력하세요. >>>> ");
            boolean result =
                reservationService.reservation(Integer.parseInt(scanner.next())); // TODO 2
            if (result) {
                menuAfterLogin();
            } else {
                menuBookList();
            }
        }
        if (menu == 3) {
            startMenu();
        }
        scanner.close();
    }

}
