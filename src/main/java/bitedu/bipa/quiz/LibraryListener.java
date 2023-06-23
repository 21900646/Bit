package bitedu.bipa.quiz;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
		// TODO Auto-generated method stub
		//클라이언트 정보는 HttpServletResponse가 갖고있따.
		String data = getData(req.getParameter("userId"));
		resp.setContentType("text/html; charset=UTF-8");//네트워크에 관련된 작업?
		PrintWriter writer = resp.getWriter();
		writer.print(data);
		writer.close();
		System.out.println("========================working========================");
	}
	
	private String getData(String userId) {
		String result = null;
		
		// 도서이용현황에 대한 정보를 가져와서 
		LibraryBookService lbs = new LibraryBookService();
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(req.getParameter("gbn").equals("rental")) {
			rentalBook(req, resp);
		} else if(req.getParameter("gbn").equals("return")) {
			returnBook(req, resp);
		}
	}
	
	private void rentalBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");//네트워크에 관련된 작업?
		String rtn = "";
		
		LibraryBookService service = new LibraryBookService();
		
		String[] bookSeqs = req.getParameterValues("bookSeqs");
		
		int cnt = 0;
		for(String bookSeq : bookSeqs) {
			HashMap<String, String> returnMap = new HashMap<String, String>();
			try {
				returnMap = service.rentalBookByIdSeq(req.getParameter("userId"), Integer.parseInt(bookSeq));
				if(returnMap.get("msg").equals("")) {
					cnt++;
				} else {
					rtn = returnMap.get("msg");
					break;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		PrintWriter writer = resp.getWriter();
		if(cnt > 0) {
			rtn = cnt + "권 대출 완료되었습니다." + (cnt != bookSeqs.length ? "\n실패 : " + (bookSeqs.length - cnt) + " 권 " + rtn : "");
		}
		writer.print(rtn);
		writer.close();
	}
	
	private void returnBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 // TODO Auto-generated method stub
        LibraryBookService service = new LibraryBookService();
        
        String[] bookSeqs = req.getParameterValues("bookSeqs");
        int cnt = 0;
        for(String bookSeq : bookSeqs) {
           boolean bool;
           try {
              bool = service.bookReturn(req.getParameter("userId"), Integer.parseInt(bookSeq));
              if(bool) {
                 System.out.println("반납 완료.");
                 cnt++;
              } else {
                 System.out.println("반납 실패.");
              }
           } catch (NumberFormatException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
           }
        }

        resp.setContentType("text/html; charset=UTF-8");//네트워크에 관련된 작업?
        PrintWriter writer = resp.getWriter();
        String rtn = "";
        if(cnt > 0) {
           rtn = cnt + "권 반납 완료되었습니다.\n실패 : " + (bookSeqs.length - cnt) + " 권";
        } else {
           rtn = "반납 실패했습니다.";
        }
        writer.print(rtn);
        writer.close();

	}
	
}
