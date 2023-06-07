package bipa.edu.dao;

import bipa.edu.dto.BookDTO;
import bipa.edu.util.ConnectionManager;
import bipa.edu.util.GetUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public List<BookDTO> getBooksOrderByRecentDateDesc() throws SQLException, ClassNotFoundException {
        List<BookDTO> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            String sql = "SELECT * FROM book ORDER BY register_date DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookNo = rs.getInt("book_no");
                String name = rs.getString("name");
                int state = rs.getInt("state");
                int grade = rs.getInt("grade");
                int rentalCount = rs.getInt("rental_count");
                String writer = rs.getString("writer");
                Date registerDate = rs.getDate("register_date");

                BookDTO bookDTO = new BookDTO(bookNo, name, state, grade, rentalCount, writer, registerDate);
                list.add(bookDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return list;
    }

    /*
        'getConnection' 메소드를 호출하여 데이터베이스 연결을 가져온다.
        "SELECT * FROM book ORDER BY rental_count DESC" SQL 쿼리를 생성하여 'PreparedStatement' 객체를 생성한다.
        SQL 쿼리를 실행하고 결과를 'ResultSet' 객체로 받는다.
        'ResultSet' 에서 도서 정보를 추출하여 'BookDTO' 객체를 생성하고, 이를 'list' 리스트에 추가한다.
        데이터베이스 연결 및 자원을 안전하게 해제하기 위해 'finally' 블록에서 'ResultSet', 'PreparedStatement', 'Connection' 객체를 순서대로 닫는다.
        최종적으로 정렬된 도서 리스트인 'list' 를 반환한다.
        이 메소드를 통해 대여 횟수에 따라 도서를 정렬하여 조회할 수 있다.
     */
    public List<BookDTO> getBooksOrderByRentalCountDesc() throws SQLException {
        List<BookDTO> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM book ORDER BY rental_count DESC";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookNo = rs.getInt("book_no");
                String name = rs.getString("name");
                int state = rs.getInt("state");
                int grade = rs.getInt("grade");
                int rentalCount = rs.getInt("rental_count");
                String writer = rs.getString("writer");
                Date registerDate = rs.getDate("register_date");

                BookDTO bookDTO = new BookDTO(bookNo, name, state, grade, rentalCount, writer, registerDate);
                list.add(bookDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return list;
    }


    /*
        'name' 과 'writer' 매개변수로 전달된 검색어를 사용하여 SQL 쿼리를 생성.
        'getConnection' 메소드를 호출하여 데이터베이스 연결을 가져옴.
        생성된 SQL 쿼리에 전달할 매개변수를 설정한다.
        %를 검색어 앞에 추가하여 와일드카드로 사용하며, LIKE 연산자를 통해 부분 일치 검색을 수행한다.
        'executeQuery' 메소드를 호출하여 SQL 쿼리를 실행하고, 결과를 'ResultSet' 객체로 받는다.
        반복문을 통해 'ResultSet' 에서 도서 정보를 추출하여 'BookDTO' 객체를 생성하고, 이를 'books' 리스트에 추가한다.
        데이터베이스 연결 및 자원을 안전하게 해제하기 위해 'finally' 블록에서 'ResultSet', 'PreparedStatement', 'Connection' 객체를 순서대로 닫는다.
        최종적으로 검색된 도서 리스트인 'books' 를 반환한다.
        이 메소드를 통해 도서 검색 기능을 구현할 수 있다.
     */
    public List<BookDTO> searchBooks(String name, String writer) throws SQLException {
        List<BookDTO> books = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();

            String sql = "SELECT * FROM book WHERE name LIKE ? OR writer LIKE ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + name);
            pstmt.setString(2, "%" + writer); // "%" + writer + "%"

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookNo = rs.getInt("book_no");
                String bookName = rs.getString("name");
                int state = rs.getInt("state");
                int grade = rs.getInt("grade");
                int rentalCount = rs.getInt("rental_count");
                String bookWriter = rs.getString("writer");
                Date registerDate = rs.getDate("register_date");

                BookDTO book = new BookDTO(bookNo, bookName, state, grade, rentalCount, bookWriter, registerDate);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
        }

        return books;
    }

    public BookDTO getBook(int bookNo) throws SQLException {
        BookDTO book = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            String sql = "select *\n" +
                "from Book\n" +
                "where book_no = ?;";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookNo);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                book = new BookDTO(rs.getInt("book_no"),
                    rs.getString("name"),
                    rs.getInt("state"),
                    rs.getInt("grade"),
                    rs.getInt("rental_count"),
                    rs.getString("writer"),
                    rs.getDate("register_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return book;
    }

    /**
     * @author 김민석
     * 대출한 목록 보여주는 메소드
     * @return
     * @throws SQLException
     */
    public List<BookDTO> getAllRent() throws SQLException {
        List<BookDTO> bookDTOList = new ArrayList<>();
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sql = "select * from book\n" +
                "where book_no in (select book_no from rental \n" +
                "Where member_id = ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, GetUser.getId());
            rs = pstmt.executeQuery();
            while(rs.next()){
                bookDTOList.add(new BookDTO(
                    rs.getInt("book_no"),
                    rs.getString("name"),
                    rs.getInt("state"),
                    rs.getInt("grade"),
                    rs.getInt("rental_count"),
                    rs.getString("writer"),
                    rs.getDate("register_date")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(pstmt != null) {
                pstmt.close();
            }
        }
        return bookDTOList;
    }

    /**
     * @author 김민석
     * 현재날짜 - retal테이블 return_date을 뺀 값을 member - penalty_time에 저장
     * @param bookNo
     * @throws SQLException
     */
    public int savePenaltyTime(int bookNo) throws SQLException {
        int result = 0;
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE member\n" +
                "JOIN rental ON member.member_id = rental.member_id\n" +
                "SET member.penalty_time = GREATEST(DATEDIFF( CURDATE(), rental.return_date), member.penalty_time)\n" +
                "WHERE member.member_id = ? AND rental.book_no = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, GetUser.getId());
            pstmt.setInt(2, bookNo);
            result = pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    /**
     * @author 김민석
     * rental 동작 시 사용한 list, table에서 sql사용해서 delete
     * @param reservationNo
     * @throws SQLException
     */
    public int returnBook(int reservationNo) throws SQLException {
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        int result = 0;

        try{
            String sql = "DELETE FROM rental WHERE book_no = (?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationNo);
            result = pstmt.executeUpdate();
            pstmt.close();
            if(result > 0) {
                System.out.println(reservationNo + "번 책이 반납되었습니다.");
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    /**
     * @author 김민석
     * @return
     * @throws SQLException
     */
    public int getPenaltyTime() throws SQLException {
        int penaltyTime = 0;
        conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select penalty_time from member " +
                "where member_id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, GetUser.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                penaltyTime = rs.getInt("penalty_time");
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return penaltyTime;
    }
}
