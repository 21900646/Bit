package bitedu.bipa.quiz.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import bitedu.bipa.quiz.dao.LibraryDAO;
import bitedu.bipa.quiz.util.DateCalculation;
import bitedu.bipa.quiz.vo.BookUseStatusVO;
import bitedu.bipa.quiz.vo.UserBookStatusVO;
import bitedu.bipa.quiz.vo.UserVO;

public class LibraryBookService {
	
	private LibraryDAO dao;
	public LibraryBookService() {
		dao = new LibraryDAO();
	}
	//select * from book_use_status where user_id = 'user1' and borrow_start between '2023-6-1' and '2023-6-30';
	// select * from book_user
	 
	// 이용상태 대출정지기간의 정보를 정리
	private ArrayList<BookUseStatusVO> getNotReturnedBooks(ArrayList<BookUseStatusVO> bookList, Calendar criteriaDate){
		ArrayList<BookUseStatusVO> result = null;
		result = new ArrayList<BookUseStatusVO>();
		Timestamp stopServiceDate = null;
		//미반납도서 - 반납기간이 지나고 반납날짜가 비어있는 도서
		String userId = null;
		for(BookUseStatusVO book : bookList) {
			if( book.getBorrowEnd().getTime() <  criteriaDate.getTimeInMillis()&&book.getReturnDate()==null) {
				userId = book.getUserId();
				result.add(book);
				//System.out.println(book);
				Timestamp temp = DateCalculation.calcuStopDate(new Timestamp(criteriaDate.getTimeInMillis()), book.getBorrowEnd());
				if(stopServiceDate!=null) {
					stopServiceDate = temp.getTime()-stopServiceDate.getTime()>0?temp:stopServiceDate;
				} else {
					stopServiceDate = temp;
				}
			}
		}
		
		if(stopServiceDate!=null) {
			//DB user 정보 update
			try {
				dao.updateUserStopStatus(userId, stopServiceDate);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return result;
	}
	
	private ArrayList<BookUseStatusVO> getReturnedBooks(ArrayList<BookUseStatusVO> bookList){
		ArrayList<BookUseStatusVO> result = null;
		//기반납도서
		result = new ArrayList<BookUseStatusVO>();
		for(BookUseStatusVO book : bookList) {
			if(book.getReturnDate()!=null&&book.getBorrowEnd().getTime() >= book.getReturnDate().getTime()) {
				result.add(book);
			}
		}
		return result;
	}
	
	private ArrayList<BookUseStatusVO> getExpectingReturnBooks(ArrayList<BookUseStatusVO> bookList, Calendar criteriaDate){
		ArrayList<BookUseStatusVO> result = null;
		//반납예정도서
		result = new ArrayList<BookUseStatusVO>();
		for(BookUseStatusVO book : bookList) {
			if(book.getReturnDate()==null&&book.getBorrowEnd().getTime() >= criteriaDate.getTimeInMillis()) {
				result.add(book);
				//System.out.println(book);
			}
		}
		return result;
	}
	
	
	public HashMap<String, Object> getUserStatus(String userId){
      HashMap<String, Object> result = null;
      result = new HashMap<String, Object>();
      
      UserVO user = null;
      ArrayList<BookUseStatusVO> list = null;
   
      try {
         // TODO 여기 수정
         list = dao.selectBookInfoByUser(userId);
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      Calendar criteriaDate = Calendar.getInstance();
      
      // TODO 여기 수정
      criteriaDate.set(Calendar.HOUR_OF_DAY, 0);
      
      ArrayList<BookUseStatusVO> allReturned = this.getReturnedBooks(list);
      ArrayList<BookUseStatusVO> notReturned = this.getNotReturnedBooks(list, criteriaDate);
      ArrayList<BookUseStatusVO> expectingReturn = this.getExpectingReturnBooks(list,criteriaDate);
      
      try {
         user = dao.selectUser(userId);
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      HashMap<String, ArrayList<BookUseStatusVO>> bookInfo = new HashMap<>();
      bookInfo.put("total", list);
      bookInfo.put("allReturned", allReturned);
      bookInfo.put("notReturned", notReturned);
      bookInfo.put("expectingReturn", expectingReturn);
      
      UserBookStatusVO userState = new UserBookStatusVO();
      userState.setUserId(userId);
      userState.setAvailableBook(user.getAvailableBook());
      userState.setExpectingReturnBook(expectingReturn.size());
      userState.setTotalUsingBook(list.size());
      userState.setReturnedBook(allReturned.size());
      userState.setNotReturnedBook(notReturned.size());
      
      boolean bool = user.getServiceStop() != null ? user.getServiceStop().after(new Timestamp(criteriaDate.getTimeInMillis())) : true;
      if (notReturned.size() > 0 || bool) {
          userState.setUserState("대출정지");
          userState.setStopDate(DateCalculation.getDate(user.getServiceStop()));
       } else {
          userState.setUserState("정상");
          userState.setStopDate("-");
       }
      HashMap<String, UserBookStatusVO> userInfo = new HashMap<>();
      
      userInfo.put("user", userState);
      result.put("userInfo", userInfo);
      result.put("bookInfo", bookInfo);
      
      return result;
   }
	
	//대출 추가
	public HashMap<String, String> rentalBookByIdSeq(String userId, int bookSeq) throws SQLException {
		HashMap<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("msg", "");
		
		UserVO user = dao.selectUser(userId);
		
		if(!user.getUserState().equals("00")) {
			returnMap.put("msg", "대출 중지 상태입니다.");
			return returnMap;
		}
		if(user.getAvailableBook() < 1) {
			returnMap.put("msg", "대출 가능 수를 초과했습니다.");
			return returnMap;
		}
		
		if(user.getServiceStop() != null) {
			if(user.getServiceStop().after(new Timestamp(new Date().getTime()))) {
				String stopDate = user.getServiceStop().getYear() + "-" + user.getServiceStop().getMonth() + "-" + user.getServiceStop().getDate();
				returnMap.put("msg", stopDate + "까지 대출이 불가합니다.");
				return returnMap;
			}
		}
		
		boolean bool = false;
		bool = dao.insertUseRental(userId, bookSeq);
		if(bool) {
			bool = dao.updateCopyRental(userId, bookSeq);
		}
		if(bool) {
			bool = dao.updateUserRental(userId);
		}
		
		return returnMap;
	}
	
	// 책 대출
	public boolean bookReturn(String userId,int bookNum) {
      boolean flag = false;
      try {
         flag = dao.updateBookReturn(userId,bookNum);
         if(flag) {
            dao.updateBookReturn2(userId);   
         }
         if(flag) {
            dao.updateBookReturn3(bookNum);
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return flag;
      
   }

	   
	   
	   public BookUseStatusVO getBookListForRental(int bookSeq) {
	      BookUseStatusVO result = null;
	      result = new BookUseStatusVO();
	      
	      
	      try {
	         result = dao.selectBookInfo(bookSeq);
	      } catch(SQLException e) {
	         e.printStackTrace();
	      }
	      
	      //ArrayList<BookUseStatusVO> bookSearchForRental = this.searchBookInfo(list);

	      
	      //HashMap<String, BookUseStatusVO> bookList = new HashMap<>();
	      //bookList.put("bookSearchForRental", bookSearchForRental);
	      //bookList.put("bookSearchForRental",list);
	      
	      //result.put("bookList", bookList);

	      
	      return result;
	   }

}
