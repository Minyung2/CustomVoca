package pracforproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VocaDao {
	static Connection conn;
	static PreparedStatement pstmt;
	static ResultSet rs;
	static Voca vo = new Voca();
	static ArrayList<Voca> userData = new ArrayList<>();
	static HashSet<String> studySet = new HashSet<>();
	static HashSet<String> testSet = new HashSet<>();
	static ArrayList<String> StudyDateList = new ArrayList<>();
	static ArrayList<String> testDateList = new ArrayList<>();
	static ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	static String path = "D:\\javavoca\\";
	static String userPath ;
	public String wordReg = "^[a-zA-Z]*$";
	public String meaningReg = "^[ㄱ-ㅎ가-힣 ]*$";
	public Pattern p;
	File target = new File(path);
	static File userFile;
	File[] files = target.listFiles();
	static String form = ".txt";
	static Scanner sc;
	static Member mvo = new Member();
	static MemberDao mdao = new MemberDao();
	// mysql Connector
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
	// 파일, List 새로고침
	@SuppressWarnings("unchecked")
	public static void init() {
		userFile = new File(userPath);
		if (!userFile.exists()) {
			
		} else {
			try {
				testSet.clear();
				testDateList.clear();
				studySet.clear();
				StudyDateList.clear();
				userData.clear();
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userPath)));
				userData.addAll((ArrayList<Voca>) ois.readObject());
				ois.close();
				for (Voca v : userData) {
					if (v.getStudyDate() != null && (v.getAnswerCorrection() == -1 || v.getAnswerCorrection() == 0))
						testSet.add(v.getStudyDate());
				}
				testDateList.addAll(testSet);
				for (Voca v : userData) {
					if (v.getStudyDate() != null)
						studySet.add(v.getStudyDate());
				}
				StudyDateList.addAll(studySet);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	// 학습
	public void dailyStudy(int memberLevel) {
		init();
		Random ran = new Random();
		boolean exist = false;
		for (Voca voca : userData) {
			if (voca.getStudyDate() != null && voca.getStudyDate().equals(vo.nowDate)) {
				exist = true;
				break;
			}
		}
		if (exist) {
			System.out.println("오늘 이미 학습하셨습니다.");
			System.out.println(vo.nowDate + " 학습 단어");
			System.out.println();
			for (Voca v : userData) {
				if (v.getStudyDate() != null && v.getStudyDate().equals(vo.nowDate)) {
					System.out.println(v.getEngWord() + "\t" + v.getMeaning());
				}
			}
			VocaExRun.start(mvo);
		} else {
			for (int i = 1; i < 11; i++) {
				int ranIdx = ran.nextInt(userData.size());
				Voca ranEle = userData.get(ranIdx);
				if (ranEle.getWordLevel() == mvo.getMemberLevel() && ranEle.getStudyDate() == null) {
					System.out.println(ranEle.getEngWord() + "\t" + ranEle.getMeaning());
					userData.get(ranIdx).setStudyDate(vo.nowDate);
				} else if (i == 1 && ranEle.getWordLevel() != mvo.getMemberLevel() && ranEle.getStudyDate() != null) {
					i = 1;
				} else {
					i--;
				}
			}
			fileWriter();
		}

	}

	// 복습 리스트 출력
	public void reviewList() {
		int num = 1;
		for (String s : StudyDateList) {
			System.out.println(num++ + ". " + s);
		}
	}
	// 복습
	public void review(int input) {
		for (int i = 0; i < StudyDateList.size(); i++) {
			if ((i) == ((input) - 1)) {
				vo.inputDate = StudyDateList.get(i);
				for (Voca v : userData) {
					if (v.getStudyDate() != null && v.getStudyDate().equals(vo.inputDate)) {
						System.out.println(v.getEngWord() + "\t" + v.getMeaning());
					}
				}
			}
		}
		vo.inputDate = "";
	}
	// 시험 리스트 출력
	public void testList() {
		int num = 1;
		if (testDateList.isEmpty() == true) {
			System.out.println("테스트 할 목록이 없습니다.");
		} else {
			System.out.println("시험 볼 날짜의 번호를 입력하세요");
			for (String s : testDateList) {
				System.out.println(num++ + ". " + s);
			}
		}
	}
	// 시험
	public void test(int input) {
		Random ran = new Random();
		int qSize = 0;

		for (int i = 0; i < testDateList.size(); i++) {
			if ((i) == ((input) - 1)) {
				vo.inputDate = testDateList.get(i);
			}
		}
		List<String> questions = new ArrayList<>();
		List<String> answers = new ArrayList<>();
		List<String> ranAnswers = new ArrayList<>();

		for (int i = 0; i < userData.size(); i++) {
			if (vo.inputDate.equals(userData.get(i).getStudyDate()) && (userData.stream().map(Voca::getAnswerCorrection)
					.collect(Collectors.toList()).get(i) == -1
					|| userData.stream().map(Voca::getAnswerCorrection).collect(Collectors.toList()).get(i) == 0)) {
				qSize += 1;
				questions.add(userData.get(i).getEngWord());
				answers.add(userData.get(i).getMeaning());
			}
		}

		for (int i = 0; i < qSize * 3; i++) {
			int ranNum = ran.nextInt(userData.size());
			if (!ranAnswers.contains(userData.get(ranNum).getMeaning())) {
				ranAnswers.add(userData.get(ranNum).getMeaning());
			}else {
				i--;
			}
		}
		int cnt = 0;
		List<String> wrongAns = new ArrayList<>();
		List<String> wrongQue = new ArrayList<>();

		for (int j = 0; j < qSize; j++) {
			int ranSeq = ran.nextInt(qSize);
			String tq = questions.get(j);
			String aq = answers.get(j);
			questions.set(questions.indexOf(questions.get(j)), questions.get(ranSeq));
			answers.set(answers.indexOf(answers.get(j)), answers.get(ranSeq));
			questions.set(questions.indexOf(questions.get(ranSeq)), tq);
			answers.set(answers.indexOf(answers.get(ranSeq)), aq);
		}
		for (int i = 0; i < qSize; i++) {
			sc = new Scanner(System.in);
			int targetIndex = userData.stream().map(Voca::getEngWord).collect(Collectors.toList())
					.indexOf(questions.get(i));
			userData.get(targetIndex).setStudyDate(vo.nowDate);

			System.out.println((i + 1) + ". " + questions.get(i));
			String[] choices = new String[4];
			choices[0] = answers.get(i); // 0
			choices[1] = ranAnswers.get(i); // 0
			choices[2] = ranAnswers.get(i + (qSize * 2) - qSize); // 10
			choices[3] = ranAnswers.get(i + (qSize * 3) - qSize); // 20
			for (int j = 0; j < choices.length; j++) {
				int randomIndex = ran.nextInt(choices.length);
				String temp = choices[j];
				choices[j] = choices[randomIndex];
				choices[randomIndex] = temp;
			}
			for (int k = 0; k < choices.length; k++) {
				System.out.println("   " + (k + 1) + ". " + choices[k]);
			}
			System.out.println("정답 입력 : ");
			int userAnswer = sc.nextInt();
			if (userAnswer == Arrays.asList(choices).indexOf(answers.get(i)) + 1) {
				System.out.println("정답입니다!");
				cnt++;
				userData.get(targetIndex).setAnswerCorrection(1);
				System.out.println(userData.get(targetIndex).getAnswerCorrection());
			} else {
				System.out.println("틀렸습니다! 정답은 " + (Arrays.asList(choices).indexOf(answers.get(i)) + 1) + "입니다.");
				wrongQue.add(questions.get(i));
				wrongAns.add(answers.get(i));
				userData.get(targetIndex).setAnswerCorrection(-1);
			}
			if (i == (qSize) - 1) {
				System.out.print("문제 수:" + qSize + " / 정답 수:" + cnt);
				System.out.println();
				System.out.print("틀린 문제 : ");
				for (int s = 0; s < wrongAns.size(); s++) {
					System.out.print(wrongQue.get(s) + ":" + wrongAns.get(s) + "/ ");
				}

				System.out.println();
				System.out.println();
				System.out.println("메인화면으로 이동합니다");
				vo.inputDate = "";

				fileWriter();
				VocaExRun.start(mvo);
			}
		}
	}
	// 로그인 성공시 파일 userId명으로 파일 생성
	public static void loginSuccess(Member mData) {
		sc=new Scanner(System.in);
		mvo.setMemberID(mData.getMemberID());
		mvo.setMemberPassword(mData.getMemberPassword());
		mvo.setPhoneNumber(mData.getPhoneNumber());
		mvo.setMemberLevel(mData.getMemberLevel());
		mvo.setLastLoginDate(mData.getLastLoginDate());
		mvo.setLastLoginDate(vo.nowDate);
		String sql = "select * from project";
		if (mvo.getMemberLevel() == 0) {
			userPath = path + "\\" + mvo.getMemberID() + "의 학습파일" + form;
			userFile = new File(userPath);
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
			VocaDao.init();
			levelTest(mvo);
		} else {
			userPath = path + "\\" + mvo.getMemberID() + "의 학습파일" + form;
			VocaDao.init();
			memberLevelUp();
			VocaExRun.start(mvo);
		}
	}
	
	// 단어 추가
	public void addWord() {
		sc = new Scanner(System.in);
		System.out.println();
		System.out.println("영단어:");
		String engWord = sc.nextLine();
		for(Voca v : userData) {
			if(v.getEngWord().equals(engWord)) {
				System.out.println("이미 존재하는 단어 입니다.");
				addWord();
				engWord="";
			}else if(!Pattern.matches(wordReg, engWord)) {
				System.out.println("영어로 입력하세요");
				addWord();
				engWord="";
			}
		}
		System.out.println("뜻:");
		String meaning = sc.nextLine();
		if(!Pattern.matches(meaningReg, meaning)) {
				System.out.println("한글로 입력하세요");
				addWord();
				meaning="";
		}
		System.out.println("단어 레벨 1(Beginner),2(Intermediate),3(Advanced) 중 설정.");
		int wordLevel = sc.nextInt();
		vo = new Voca(engWord,meaning, wordLevel);
		userData.add(vo);
		fileWriter();
		VocaExRun.start(mvo);
	}
	
	// 단어 검색
	public void finder() {
		int input=0;
		sc= new Scanner(System.in);
		while(VocaExRun.flag!=true) {
			System.out.println();
			System.out.println("1.영단어 검색 2.뜻으로 검색 0.이전단계");
			input = sc.nextInt();
			if(input==1) {
				sc.nextLine();
				System.out.println("찾을 영단어 뜻 검색: ");
				String engTarget=sc.nextLine();
				for(Voca v : userData) {
					if(v.getEngWord().equals(engTarget)){
						System.out.println(v.getMeaning());
					}
				}
			}if(input==2) {
				sc.nextLine();
				System.out.println("한글뜻으로 영단어 검색 : ");
				String meaningTarget=sc.nextLine();
				for(Voca v : userData) {
					if(v.getMeaning().contains(meaningTarget)) {
						System.out.print(v.getEngWord()+" ");
					}
				}
			}if(input==0) {
				init();
				VocaExRun.start(mvo);
			}
		}
	}
	
	// 최초 로그인시 레벨 테스트
	public static void levelTest(Member mvo) {
		init();
		sc=new Scanner(System.in);
		Random ran = new Random();
		int arraySize = 6;
		List<Voca> listForLevel = new ArrayList<>();
		List<String> ranAnswers = new ArrayList<>();

		for (int i = 1; i < arraySize; i++) {
			int ranIdx = ran.nextInt(userData.size());
			Voca ranEle = userData.get(ranIdx);
			if (ranEle.getWordLevel() == 1&& 
					!listForLevel.stream().anyMatch(list->list.getEngWord().equals(ranEle.getEngWord()))) {
				listForLevel.add(ranEle);
			} else {
				i--;
			}
		}
		for (int i = 1; i < arraySize; i++) {
			int ranIdx = ran.nextInt(userData.size());
			Voca ranEle = userData.get(ranIdx);
			if (ranEle.getWordLevel() == 2&& 
					!listForLevel.stream().anyMatch(list->list.getEngWord().equals(ranEle.getEngWord()))) {
				listForLevel.add(ranEle);
			} else {
				i--;
			}
		}
		for (int i = 1; i < arraySize; i++) {
			int ranIdx = ran.nextInt(userData.size());
			Voca ranEle = userData.get(ranIdx);
			if (ranEle.getWordLevel() == 3&& 
					!listForLevel.stream().anyMatch(list->list.getEngWord().equals(ranEle.getEngWord()))) {
				listForLevel.add(ranEle);
			} else {
				i--;
			}
		}
		
		Collections.shuffle(listForLevel);
		for (int a = 0; a < listForLevel.size()*3 + 1; a++) {
			int ranNum = ran.nextInt(userData.size());
			if (!listForLevel.stream().anyMatch(list->list.getEngWord().equals(userData.get(ranNum).getEngWord()))){
				ranAnswers.add(userData.get(ranNum).getMeaning());
			}
		}
		
		int cnt2=0;
		int cnt3=0;
		for (int i = 0; i < listForLevel.size(); i++) {
			System.out.println((i+1)+"."+ listForLevel.get(i).getEngWord());
			
			String[] choices = new String[4];
			choices[0] = listForLevel.get(i).getMeaning(); // 0
			choices[1] = ranAnswers.get(i); // 0
			choices[2] = ranAnswers.get(i + (listForLevel.size() * 2) - listForLevel.size()); // 10
			choices[3] = ranAnswers.get(i + (listForLevel.size() * 3) - listForLevel.size()); // 20
			for (int j = 0; j < choices.length; j++) {
				int randomIndex = ran.nextInt(choices.length);
				String temp = choices[j];
				choices[j] = choices[randomIndex];
				choices[randomIndex] = temp;
			}
			for (int k = 0; k < choices.length; k++) {
				System.out.println("   " + (k + 1) + ". " + choices[k]);
			}
			System.out.print("정답 입력 : ");
			int userAnswer = sc.nextInt();
			if(userAnswer == Arrays.asList(choices).indexOf(listForLevel.get(i).getMeaning()) + 1
					&& listForLevel.get(i).getWordLevel()==2) {
				System.out.println("정답입니다!");
				cnt2++;
			}else if(userAnswer == Arrays.asList(choices).indexOf(listForLevel.get(i).getMeaning()) + 1
					&& listForLevel.get(i).getWordLevel()==3) {
				System.out.println("정답입니다!");
				cnt3++;
			}else if(userAnswer == Arrays.asList(choices).indexOf(listForLevel.get(i).getMeaning()) + 1
					&& listForLevel.get(i).getWordLevel()==1) {
				System.out.println("정답입니다!");
			}else {
				System.out.println("오답입니다.");
			}
		}
		if(cnt3>3) {
			mvo.setMemberLevel(3);
			System.out.println(mvo.getMemberID()+"님의 레벨은 Advanced 입니다.");
		}else if(cnt2 > 3) {
			mvo.setMemberLevel(2);
			System.out.println(mvo.getMemberID()+"님의 레벨은 Intermediate 입니다.");
		}else {
			System.out.println(mvo.getMemberID()+"님의 레벨은 Beginner 입니다.");
			mvo.setMemberLevel(1);
		}
		userPath = path + "\\" + mvo.getMemberID() + "의 학습파일" + form;
		int targetIdx= MemberDao.memberList.stream().map(Member::getMemberID).collect(Collectors.toList())
				.indexOf(mvo.getMemberID());
		MemberDao.memberList.set(targetIdx, mvo);
		fileReadThenWriter();
		VocaExRun.start(mvo);
	}
	
	// 사용자 레벨업
	public static void memberLevelUp() {
		init();
		int level = mvo.getMemberLevel();
		List<Voca> existChecek = new ArrayList<>();
		for(Voca v : userData) {
			if(v.getWordLevel()==level&&v.getStudyDate()!=null) {
				existChecek.add(v);
			}
		}
		if(existChecek.size()==0) {
			mvo.setMemberLevel(level+1);
			userPath = path + "\\" + mvo.getMemberID() + "의 학습파일" + form;
			int targetIdx= MemberDao.memberList.stream().map(Member::getMemberID).collect(Collectors.toList())
					.indexOf(mvo.getMemberID());
			MemberDao.memberList.set(targetIdx, mvo);
			fileReadThenWriter();
			VocaExRun.start(mvo);
		}
	}
	@SuppressWarnings("unchecked")
	public static void fileReadThenWriter() {
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(userPath)));
			userData.addAll((ArrayList<Voca>) ois.readObject());
			ois.close();
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(MemberDao.memberListFile+form)));
			oos.writeObject(MemberDao.memberList);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		MemberDao.init();
	}
	 
	public void fileWriter() {
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(userPath)));
			oos.writeObject(userData);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	
}