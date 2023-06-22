package bitedu.bipa.quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bitedu.bipa.quiz.service.LibraryBookService;
import bitedu.bipa.quiz.vo.BookUseStatusVO;
import bitedu.bipa.quiz.vo.UserBookStatusVO;

public class LibraryListener extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 여기 수정
		//String data = this.getData(req.getParameter("userId"));
		
		String data = this.getData(req.getParameter("userId"));

		
		System.out.println("working");
		resp.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = resp.getWriter();
		
		out.print(data);
		out.close();
	}
	
	@Override
	   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      // TODO Auto-generated method stub
	      LibraryBookService service = new LibraryBookService();
	      
	      String[] bookSeqs = req.getParameterValues("bookSeqs");
	      
	      
	      int cnt = 0;
	      for(String bookSeq : bookSeqs) {
	         boolean bool;
	         try {
	            bool = service.rentalBookByIdSeq(req.getParameter("userId"), Integer.parseInt(bookSeq));
	            if(bool) {
	               System.out.println("대출 완료.");
	               cnt++;
	            } else {
	               System.out.println("대출 실패.");
	            }
	         } catch (NumberFormatException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         }
	      }

	      resp.setContentType("text/html; charset=UTF-8");//네트워크에 관련된 작업?
	      PrintWriter writer = resp.getWriter();
	      String rtn = "";
	      if(cnt > 0) {
	         rtn = cnt + "권 대출 완료되었습니다.\n실패 : " + (bookSeqs.length - cnt) + " 권";
	      } else {
	         rtn = "대출 실패했습니다.";
	      }
	      writer.print(rtn);
	      writer.close();
	   }
	
	
//	@Override
//	   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//	      // TODO Auto-generated method stub
//	       // 요청 파라미터 읽기
//	      
//	      LibraryBookService lbs = new LibraryBookService();
//	      lbs.bookReturn("user1", req.getParameter("TEST"));
//	      
//	      
//	       String testValue = req.getParameter("TEST");
//
//	       // TEST 값 출력
//	       System.out.println("TEST: " + testValue);
//	      
//
//	       // 응답 보내기 (생략 가능)
//	       resp.setContentType("text/plain");
//	       resp.getWriter().println("TEST 값 출력 완료");
//	   }

	
	private String getData(String userId) {
		String result = null;
		LibraryBookService lbs = new LibraryBookService();
		
		// TODO 여기 수정
		HashMap<String, Object> data = lbs.getUserStatus(userId); 
		HashMap<String,UserBookStatusVO>  map1 = (HashMap<String,UserBookStatusVO>)data.get("userInfo");
		HashMap<String,ArrayList<BookUseStatusVO>>  map2 = (HashMap<String,ArrayList<BookUseStatusVO>>)data.get("bookInfo");
		
		UserBookStatusVO status = map1.get("user");
		ArrayList<BookUseStatusVO> list = map2.get("total");
		ArrayList<BookUseStatusVO> allReturned = map2.get("allReturned");
		ArrayList<BookUseStatusVO> notReturned = map2.get("notReturned");
		ArrayList<BookUseStatusVO> expectingReturn = map2.get("expectingReturn");
		

		JSONObject json = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject user = new JSONObject();
		JSONObject book = new JSONObject();
		
		JSONArray array1 = new JSONArray();
		JSONArray array2 = new JSONArray();
		JSONArray array3 = new JSONArray();
		JSONArray array4 = new JSONArray();
		
		JSONArray array5 = new JSONArray();
		array5.add(status);
		
		user.put("user", array5);
		
		for(BookUseStatusVO vo : list) {
			array1.add(vo);
		}
		book.put("list", array1);
		
		for(BookUseStatusVO vo : allReturned) {
			array2.add(vo);
		}
		book.put("returned", array2);
		
		for(BookUseStatusVO vo : notReturned) {
			array3.add(vo);
		}
		book.put("notReturned", array3);
		
		for(BookUseStatusVO vo : expectingReturn) {
			array4.add(vo);
			//System.out.println(vo);
		}
		book.put("expectingReturn", array4);
		
		
		info.put("userInfo", user);
		info.put("bookInfo", book);
		
		json.put("data", info);

		System.out.println(json.toJSONString());
		
		result = json.toJSONString();
		return result;
	}
}
