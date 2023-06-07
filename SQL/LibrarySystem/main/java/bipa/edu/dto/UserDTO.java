package bipa.edu.dto;

public class UserDTO {
    private String memberId;
    private String password;
    private int grade;
    private int penaltyTime;

    UserDTO(String memberId, String password){
        this.memberId = memberId;
        this.password = password;
        this.grade = 0;
        this.penaltyTime = 0;
    }

    public String getMemberId() {
        return memberId;
    }


    public String getPassword() {
        return password;
    }


    public int getGrade() {
        return grade;
    }

    public int getPenaltyTime() {
        return penaltyTime;
    }

}
