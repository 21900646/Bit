package bitedu.bipa.quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import bitedu.bipa.quiz.service.LibraryBookService;
import bitedu.bipa.quiz.vo.BookUseStatusVO;

public class LibraryListener2 extends HttpServlet {
	@Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      // TODO Auto-generated method stub
      String data2 = this.getData2(Integer.parseInt(req.getParameter("bookSeq")));
      System.out.println("success load BookListForRental");
      resp.setContentType("text/html; charset=UTF-8");
      PrintWriter out = resp.getWriter();
      out.print(data2);
      out.close();

   }
   
   private String getData2(int bookSeq) {
      //String result = null;
      
      LibraryBookService lbs = new LibraryBookService();
      BookUseStatusVO data2 = lbs.getBookListForRental(bookSeq); 

      //HashMap<String,ArrayList<BookUseStatusVO>>  map1 = (HashMap<String,ArrayList<BookUseStatusVO>>)data2.get("bookList");
      //BookUseStatusVO list2 = (BookUseStatusVO) data2.get("bookList");
      
      JSONObject book = new JSONObject();


//	      JSONArray array1 = new JSONArray();
//	      
//	      for(BookUseStatusVO vo : list2) {
//	         array1.add(vo);
//	      }
      book.put("bookSearchForRental", data2);
      System.out.println(book.toJSONString());


      //return result;
      return book.toJSONString();
   }

}
