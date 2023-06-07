package bipa.edu.service;

import bipa.edu.dao.BookDAO;
import bipa.edu.dto.BookDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 박창우
 */
public class BookService {
    private BookDAO dao;

    public BookService() {
        this.dao = new BookDAO();
    }

    public BookDTO getBookDto(int bookNo) {
        BookDTO bookDTO = null;
        try {
            bookDTO = dao.getBook(bookNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookDTO;
    }
    public void recentBook() {
        ArrayList<BookDTO> list;
        System.out.println("---------------------------------------------");
        System.out.println("            << 신규 도서 목록 >>");
        System.out.println("---------------------------------------------");

        try {
            list = (ArrayList<BookDTO>) dao.getBooksOrderByRecentDateDesc();

            int cnt = 1;
            for (BookDTO book : list) {
                System.out.print(cnt++ + ") ");
                System.out.println(book);  // BookDTO 객체의 toString() 메소드를 통해 출력
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 인기 도서 목록
     * @return
     */
    public ArrayList<BookDTO> bestBook() {
        ArrayList<BookDTO> list = new ArrayList<>();
        System.out.println("---------------------------------------------");
        System.out.println("            << 인기 도서 목록 >>");
        System.out.println("---------------------------------------------");
        try {
            list = (ArrayList<BookDTO>) dao.getBooksOrderByRentalCountDesc();
            int cnt = 1;
            for (BookDTO book : list) {
                System.out.print(cnt++ + ") ");
                System.out.println(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 도서 검색 서비스
     * @param name
     * @param writer
     */
    public boolean searchBooksAndPrint(String name, String writer) {
        try {
            List<BookDTO> books = dao.searchBooks(name, writer);
            if (books.isEmpty()) {
                System.out.println("해당 도서가 존재하지 않습니다.");
                return false;
            } else{
                for (BookDTO book : books) {
                    System.out.println(book);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}
