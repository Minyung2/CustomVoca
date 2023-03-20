package pracforproject;
//package pracforproject;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class MemberDao {
//	private Connection conn;
//	private PreparedStatement pstmt;
//	private ResultSet rs;
//	// Connector 
//	public MemberDao() {
//		try {
//			String dbURL = "jdbc:mysql://localhost:3306/dict"; 
//			String dbID = "testdb"; 
//			String dbPassword = "1234"; 
//			Class.forName("com.mysql.cj.jdbc.Driver");  
//			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	// 로그인
////	public int login(String userID, String userPassword) { 
////		String SQL = "SELECT userPassword FROM Members WHERE userID = ?"; 
////		try {
////			pstmt = conn.prepareStatement(SQL);
////			pstmt.setString(1,  userID);
////			rs = pstmt.executeQuery(); 
////			if (rs.next()) {
////				if (rs.getString(1).contentEquals(userPassword)) {
////				
////					return 1; 
////				}
////				else {
////					return 0; 
////				}
////			}
////			rs.close();
////			pstmt.close();
////			conn.close();
////			return -1;
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return -2;  
////	}
//	public int login(String userID, String userPassword) { 
//		String SQL = "SELECT userPassword FROM Members WHERE userID = ?"; 
//		try {
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setString(1,  userID);
//			rs = pstmt.executeQuery(); 
//			if (rs.next()) {
//				if (rs.getString(1).contentEquals(userPassword)) {
//				
//					return 1; 
//				}
//				else {
//					return 0; 
//				}
//			}
//			rs.close();
//			pstmt.close();
//			conn.close();
//			return -1;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return -2;  
//	}
//	
//	public void singUp(String userId, String userPassword) {
//		String SQL = "INSERT into members (userID, UserPassword) values(?,?)";
//		try {
//			pstmt = conn.prepareStatement(SQL);
//			pstmt.setString(1, userId);
//			pstmt.setString(2, userPassword);
//			int count = pstmt.executeUpdate();
//			if(count > 0 ) {
//				System.out.println("가입 완료");
//				Menus.mainScreen();
//			}else {
//				System.out.println("가입 실패");
//				Menus.signUp();
//			}
//			rs.close();
//			pstmt.close();
//			conn.close();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
