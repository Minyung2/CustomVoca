package pracforproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
 * 객체를 생성해서 메소드를 호출하면 Connection 객체를 리턴하는 객체를 생성할 클래스 설계하기
 */

public class DBConnect {
	
	// Connection 객체의 참조값을 담을 필드 선언

	// 생성자에서 Connection 객체를 얻어오는 작업을 한다.
	
	static String url = "jdbc:mysql://localhost/Dict";
	static String driver = "com.mysql.jdbc.Driver";
	static String id = "testdb";
	static String pw = "1234";

	public static Connection getConnection() throws Exception {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, id, pw);
		return conn;
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn, PreparedStatement ps) {
		if(ps!=null) {
			try {
				ps.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		close(conn);
	}
}