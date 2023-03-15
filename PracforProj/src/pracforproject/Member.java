package pracforproject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Member implements Serializable {
	private static final long serialVersionUID = -6480609900420593203L;	
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");// Date 포맷
	public String nowDate = simpleDateFormat.format(new Date());
	
	private String memberID;
	private String memberPassword;
	private String phoneNumber;
	private String lastLoginDate;
	private int memberLevel;
	
	
	// 생성자
	public Member() {}
	
	public Member(String memberID, String memberPassword, String phoneNumber) {
		super();
		this.memberID = memberID;
		this.memberPassword = memberPassword;
		this.phoneNumber=phoneNumber;
		memberLevel=0;
		lastLoginDate=null;
	}
	// getter, setter
	String getMemberID() {
		return memberID;
	}
	void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	String getMemberPassword() {
		return memberPassword;
	}
	void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}
	int getMemberLevel() {
		return memberLevel;
	}
	void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	@Override
	public String toString() {
		return memberID+ " "+memberPassword+" "+phoneNumber+ " " +memberLevel;
	}
}
