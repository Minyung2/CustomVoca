package pracforproject;

public class Member {
	private int userIdx;
	private String userID;
	private String userPassword;
	private int userLevel;
	public Member(int userIdx, String userID, String userPassword, int userLevel) {
		super();
		this.userIdx = userIdx;
		this.userID = userID;
		this.userPassword = userPassword;
		this.userLevel = userLevel;
	}
	int getUserIdx() {
		return userIdx;
	}
	void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}
	String getUserID() {
		return userID;
	}
	void setUserID(String userID) {
		this.userID = userID;
	}
	String getUserPassword() {
		return userPassword;
	}
	void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	int getUserLevel() {
		return userLevel;
	}
	void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}	
}
