package bitedu.bipa.quiz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.midi.Soundbank;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bitedu.bipa.quiz.service.LibraryBookService;
import bitedu.bipa.quiz.vo.BookUseStatusVO;
import bitedu.bipa.quiz.vo.UserBookStatusVO;
import bitedu.bipa.quiz.vo.UserVO;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution solution = new Solution();
		solution.getUserInfo("user1");
	}
	
	public void getUserInfo(String userId) {
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
		//JSON형태로 변환한뒤에 파일로 저장
		try {
			this.saveUserInfo(userId,json.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveUserInfo(String userId, String userInfo) throws IOException {
		String path = String.format("D:/dev/test/%s.json",userId);
		File file = new File(path);
		FileWriter fw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(fw);
		pw.print(userInfo);
		pw.close();
		fw.close();
	}
}
