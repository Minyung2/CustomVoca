package pracforproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class VocaDao {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	static Voca vo = new Voca();
	static ArrayList<Voca> userData = new ArrayList<>();
	OutputStreamWriter osw = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	FileOutputStream fos = null;
	InputStreamReader isr = null;
	FileInputStream fis = null;
	String path = "D:\\javavoca\\";
	String userPath = "D:\\javavoca\\userdata.txt";
	File target = new File(path);
	File userFile = new File(userPath);
	File[] files = target.listFiles();
	String form = ".txt";
	String meta = "";
	Scanner sc = new Scanner(System.in);

// Connector

	public VocaDao() {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			String dbURL = "jdbc:mysql://localhost:3306/dict";
			String dbID = "testdb";
			String dbPassword = "1234";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void init() {
		String sql = "select * from project";
		if (!userFile.exists()) {
			try {
				userFile.createNewFile();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String engWord = rs.getString("engWord");
					String meaning = rs.getString("meaning");
					int wordLevel = rs.getInt("wordLevel");
					vo = new Voca(engWord, meaning, wordLevel);
					userData.add(vo);
				}
				oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userPath)));
				oos.writeObject(userData);
				oos.flush();
				oos.close();
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userPath)));
				userData = (ArrayList<Voca>) ois.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println(userData);
		}
	}

	// 학습
	public void dailyStudy(int level) {
		Random ran = new Random();
		File file = new File(path + vo.nowDate + form);
		if (file.exists()) {
			System.out.println("오늘 이미 학습하셨습니다.");
			System.out.println(vo.nowDate + " 학습 단어");
			try {
				fis = new FileInputStream(path + vo.nowDate + form);
				isr = new InputStreamReader(fis, "UTF-8");
				int c;
				while ((c = isr.read()) != -1) {
					meta += (char) c;
				}
				System.out.println();
				System.out.println(meta);
				meta = "";
				fis.close();
				isr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Menus.start();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 1; i < 11; i++) {
				int ranIdx = ran.nextInt(userData.size());
				Voca ranEle = userData.get(ranIdx);
				if (ranEle.getWordLevel() == level && ranEle.getStudyDate() == null) {
					System.out.println(ranEle.getEngWord() + "\t" + ranEle.getMeaning());
					userData.get(ranIdx).setStudyDate(vo.nowDate);
					try {
						fos = new FileOutputStream(file, true);
						osw = new OutputStreamWriter(fos);
						osw.write(ranEle.getEngWord() + ",\t" + ranEle.getMeaning() + "\n");
						osw.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (i == 1 && ranEle.getWordLevel() != level && ranEle.getStudyDate() != null) {
					i = 1;
				} else {
					i--;
				}
			}
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userPath)));
				oos.writeObject(userData);
				oos.flush();
				oos.close();
				osw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

// 복습

	public void reviewList() {
		int num = 1;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().contains("userdata"))
				System.out.println((num++) + "." + files[i].getName().replaceAll(form, ""));
		}
	}

	public void review(int input) {
		for (int i = 0; i < files.length; i++) {
			if ((i) == ((input) - 1)) {
				vo.inputDate = files[i].getName();
				try {
					fis = new FileInputStream(path + vo.inputDate);
					isr = new InputStreamReader(fis, "UTF-8");
					int c;
					while ((c = isr.read()) != -1) {
						meta += (char) c;
					}
					System.out.println();
					System.out.println(meta);
					meta = "";
					fis.close();
					isr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		vo.inputDate = "";
	}

	public void testList() {
		System.out.println(userData);
		int num = 1;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().contains("userdata"))
				System.out.println((num++) + "." + files[i].getName().replaceAll(form, ""));
		}
	}

	public void test(int input) {
		Random ran = new Random();
		int qSize = 0;
		String[] questions = new String[qSize];
		String[] answers = new String[qSize];
		String[] choiceAns = new String[qSize * 3];
		for (int i = 0; i < files.length; i++) {
			if ((i) == ((input) - 1)) {
				vo.inputDate = files[i].getName().replaceAll(form, "");
			}
		}
		// userData 전체를 돌려서 학습한 날짜 단어수 cnt
		for (int i = 0; i < userData.size(); i++) {
			if (vo.inputDate.equals(userData.get(i).getStudyDate())) {
				qSize += 1;

			}
		}
		for (int i = 0; i < userData.size(); i++) {
			if (vo.inputDate.equals(userData.get(i).getStudyDate())) {
				for (int j=0; j < qSize; j++) {
					questions[j] = userData.get(i).getEngWord();
					answers[j] = userData.get(i).getMeaning();
				}
			}
		}

		// 문제,정답 생성

		// 랜덤 정답 생성
		for (int i = 0; i < choiceAns.length; i++) {
			if (!vo.inputDate.equals(userData.get(i).getStudyDate())) {
				int ranNum = ran.nextInt(userData.size());
				choiceAns[i] = userData.get(ranNum).getMeaning();
			}
		}
		for (int i = 0; i < qSize; i++) {
			// 문제 출력
			System.out.println((i + 1) + ". " + questions[i]);

			// 선택지 생성
			String[] choices = new String[4];
			choices[0] = answers[i];
			choices[1] = choiceAns[i];
			choices[2] = choiceAns[i + 1];
			choices[3] = choiceAns[i + 2];

			// 선택지를 랜덤하게 섞음
			for (int j = 0; j < choices.length; j++) {
				int randomIndex = ran.nextInt(choices.length);
				String temp = choices[j];
				choices[j] = choices[randomIndex];
				choices[randomIndex] = temp;
			}
			for (int j = 0; j < choices.length; j++) {
				System.out.println("   " + (j + 1) + ". " + choices[j]);
			}
		}

	}
}