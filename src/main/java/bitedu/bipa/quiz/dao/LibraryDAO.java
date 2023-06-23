package bitedu.bipa.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import bitedu.bipa.quiz.util.ConnectionManager;
import bitedu.bipa.quiz.vo.BookUseStatusVO;
import bitedu.bipa.quiz.vo.UserVO;

public class LibraryDAO {

	private ConnectionManager manager;
	
	// 비즈니스 판별은 로직에서
	public LibraryDAO() {
		this.manager = ConnectionManager.getInstance();
	}
	
	
	public UserVO selectUser(String userId) throws SQLException {
		UserVO user = null;
		String sql = "select user_status,max_book,service_stop from book_user where user_id = ?";
		Connection con = manager.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, userId);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			user = new UserVO();
			user.setUserId(userId);
			user.setUserState(rs.getString(1));
			user.setAvailableBook(rs.getInt(2));
			user.setServiceStop(rs.getTimestamp(3));
		}
		
		manager.closeConnection(rs, pstmt, con);
		return user;
	}
	

	public ArrayList<BookUseStatusVO> selectBookInfoByUser(String userId) throws SQLException {
	      ArrayList<BookUseStatusVO> list = null;
	      list = new ArrayList<BookUseStatusVO>();
	      StringBuilder sb = new StringBuilder("select i.book_isbn,i.book_title,i.book_author,s.* ");
	      sb.append("from book_copy c  inner join (book_info i) on c.book_isbn = i.book_isbn ");
	      sb.append("inner join book_use_status s on s.book_seq = c.book_seq ");
	      sb.append("where s.user_id = ? and s.borrow_start between date_add(now(), interval -1 month) and now()");
	      
	      String sql = sb.toString();
	      Connection con = manager.getConnection();
	      PreparedStatement pstmt = con.prepareStatement(sql);
	      pstmt.setString(1, userId);
	      ResultSet rs = pstmt.executeQuery();
	      BookUseStatusVO vo = null;
	      while(rs.next()) {
	         vo = new BookUseStatusVO(rs.getInt(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8));
	         vo.setBookIsbn(rs.getString(1));
	         vo.setBookTitle(rs.getString(2));
	         vo.setBookAuthor(rs.getString(3));
	         list.add(vo);
	      }
	      
	      manager.closeConnection(rs, pstmt, con);
	      
	      return list;
	   }
	
	public boolean updateUserStopStatus(String userId, Timestamp stopDate) throws SQLException {
		boolean flag = false;
		String sql = "update book_user set user_status= ?, service_stop = ? where user_id = ?";
		try {
			Connection con = manager.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "01");
			pstmt.setTimestamp(2, stopDate);
			pstmt.setString(3, userId);
			int affectedCount = pstmt.executeUpdate();
			if(affectedCount>0) {
				flag = true;
				//System.out.println("success");
			}
			manager.closeConnection(null, pstmt, con);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return flag;
	}
	
	//대출 - use 테이블에 추가
	public boolean insertUseRental(String userId, int bookSeq) {
		boolean flag = false;
		
		StringBuilder bookUseSql = new StringBuilder("insert into book_use_status(book_seq, user_id, borrow_start, borrow_end) ")
				.append("value(?, ?, now(), date_add(now(), interval 13 day)) ")
				.append("on duplicate key update borrow_start = now(), borrow_end = date_add(now(), interval 13 day), return_date = null;");
		Connection con = manager.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(bookUseSql.toString());
			pstmt.setInt(1, bookSeq);
			pstmt.setString(2, userId);
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			manager.closeConnection(null, pstmt, con);
		}
		
		return flag;
	}
	
	//대출 - copy 테이블 수정
	public boolean updateCopyRental(String userId, int bookSeq) {
		boolean flag = false;
		
		StringBuilder copySql = new StringBuilder("update book_copy set book_position = concat('BB-', ?) where book_seq = ?;");
		Connection con = manager.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(copySql.toString());
			pstmt.setString(1, userId);
			pstmt.setInt(2, bookSeq);
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			manager.closeConnection(null, pstmt, con);
		}
		
		return flag;
	}
	
	//대출 - use 테이블에 추가
	public boolean updateUserRental(String userId) {
		boolean flag = false;
		
		StringBuilder userSql = new StringBuilder("update book_user set max_book = max_book - 1 where user_id = ?;");
		Connection con = manager.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(userSql.toString());
			pstmt.setString(1, userId);
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			manager.closeConnection(null, pstmt, con);
		}
		
		return flag;
	}
	
	public boolean updateBookReturn(String userId,int bookNum) throws SQLException {
	      boolean flag = false;
	      StringBuilder sb = new StringBuilder("update book_use_status set return_date = now() where user_id = ? and book_seq = ? and return_date IS NULL;");

//	       sb.append("update book_user set max_book = max_book + 1 where user_id = 'user1';");
//	       sb.append("update book_copy set book_position = 'BS-0001' where book_seq = 27;");
	       String sql = sb.toString();
	       Connection con = manager.getConnection();
	       PreparedStatement pstmt = con.prepareStatement(sb.toString());

	       pstmt.setString(1, userId);
	       pstmt.setInt(2, bookNum);

	       int aff = pstmt.executeUpdate();

	       if (aff > 0) {
	          flag = true;
	       }

	       manager.closeConnection(null, pstmt, con);

	       return flag;
	    }
	    
	    // 대출가능권수 + 1
	    public boolean updateBookReturn2(String userId) throws SQLException {
	       boolean flag = false;
	       StringBuilder sb = new StringBuilder("update book_user set max_book = max_book + 1 where user_id = ?;");

	       Connection con = manager.getConnection();
	       PreparedStatement pstmt = con.prepareStatement(sb.toString());
	       pstmt.setString(1, userId);

	       int aff = pstmt.executeUpdate();

	       if (aff > 0) {
	          flag = true;
	       }

	       manager.closeConnection(null, pstmt, con);

	       return flag;
	    }
	    
	    // book postion 변경
	    public boolean updateBookReturn3(int bookNum) throws SQLException {
	       boolean flag = false;
	       StringBuilder sb = new StringBuilder("update book_copy set book_position = 'BS-0001' where book_seq = ?;");
	       
	       Connection con = manager.getConnection();
	       PreparedStatement pstmt = con.prepareStatement(sb.toString());
	       pstmt.setInt(1, bookNum);

	       int aff = pstmt.executeUpdate();

	       if (aff > 0) {
	          flag = true;
	       }

	       manager.closeConnection(null, pstmt, con);

	       return flag;
	    }
	   
	   public BookUseStatusVO selectBookInfo(int bookSeq) throws SQLException {
	       //BookUseStatusVO list = new BookUseStatusVO();
	        BookUseStatusVO vo = new BookUseStatusVO();
	       StringBuilder sb = new StringBuilder("SELECT bc.book_seq, bi.book_isbn, bi.book_title, bi.book_author ");
	       sb.append("FROM book_copy bc ");
	       sb.append("LEFT OUTER JOIN book_info bi ON bc.book_isbn = bi.book_isbn ");
	       sb.append("WHERE bc.book_seq = ?;");
	       String sql = sb.toString();
	       Connection con = manager.getConnection();
	       PreparedStatement pstmt = con.prepareStatement(sql);
	       pstmt.setInt(1, bookSeq); // 인덱스는 1부터 시작
	       ResultSet rs = pstmt.executeQuery();
	       while (rs.next()) {
	           int bookSeq1 = rs.getInt("book_seq");
	           String bookIsbn = rs.getString("book_isbn");
	           String bookTitle = rs.getString("book_title");
	           String bookAuthor = rs.getString("book_author");

	           //BookUseStatusVO vo = new BookUseStatusVO();
	           vo.setBookSeq(bookSeq1);
	           vo.setBookIsbn(bookIsbn);
	           vo.setBookTitle(bookTitle);
	           vo.setBookAuthor(bookAuthor);

	           //list = vo;
//	           list.add(vo);
	       }

	       manager.closeConnection(rs, pstmt, con);

	       //return list;
	       return vo;
	   }
	
}
