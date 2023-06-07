package bipa.edu.service;

import bipa.edu.dao.UserDAO;
import bipa.edu.util.GetUser;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    private UserDAO dao;
    Scanner scanner = new Scanner(System.in);

    public UserService() {
        this.dao = new UserDAO();
    }

    public boolean login() {
        boolean result = false;
        System.out.print("아이디를 입력하세요. >>>> ");
        String id = scanner.next();
        System.out.print("비밀번호를 입력하세요. >>>> ");
        String pwd = scanner.next();
        try {
            if (dao.login(id, pwd)) {
                GetUser.setId(id);
                System.out.println("---------------------------------------------");
                System.out.println("[" + GetUser.getId() + "] 아이디로 로그인되었습니다.");
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void logout() {
        GetUser.setId(null);
        System.out.println("---------------------------------------------");
        System.out.println("로그아웃을 완료하였습니다.");
    }
}
