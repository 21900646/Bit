package bitedu.bipa.lesson1;

public class Person {
	private int id;
	private String email;
	private int kor;
	private int eng;
	private int math;
	private int sci;
	private int soc;
	private int total;
	private String teacherCode;
	private String ABC;
	private String regionCode;

	public Person(int id, String email, int kor, int eng, int math, int sci, int soc, int total, String teacherCode, String ABC, String regionCode) {
		this.id = id;
		this.email = email;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		this.sci = sci;
		this.soc = soc;
		this.total = total;
		this.teacherCode = teacherCode;
		this.ABC = ABC;
		this.regionCode = regionCode;
	}
	
	public int getID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getKor() {
		return kor;
	}
	
	public int getEng() {
		return eng;
	}
	
	public int getMath() {
		return math;
	}
	
	public int getSci() {
		return sci;
	}
	
	public int getSoc() {
		return soc;
	}
	
	public int getTotal() {
		return total;
	}
	
	public String getTeacherCode() {
		return teacherCode;
	}
	
	public String getABC() {
		return ABC;
	}
	
	public String getRegionCode() {
		return regionCode;
	}	
	
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", kor=" + kor +
            ", eng=" + eng +
            ", math=" + math +
            ", sci=" + sci +
            ", soc=" + soc +
            ", total=" + total +
            ", teacherCode='" + teacherCode + '\'' +
            ", ABC='" + ABC + '\'' +
            ", regionCode='" + regionCode + '\'' +
            '}';
    }
}
