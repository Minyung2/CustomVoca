package pracforproject;

import java.util.Scanner;

public class VocaExRun {
	static Scanner sc = new Scanner(System.in);
	static DBConnect dbConn = new DBConnect();
	static VocaDao dao;
	static boolean flag = false;
	static MemberDao mdao = new MemberDao();
	static Voca vo = new Voca();
	static Member mvo = new Member();

	public static void main(String[] args) {
		MemberDao.init();
		dao = new VocaDao();
		while (flag != true) {
			mainScreen();
		}
	}

	public static void mainScreen() {
		while (flag != true) {
			int input;
			System.out.println("메인 화면 입니다.");
			System.out.println("1.회원가입 2.로그인 3.비밀번호 변경 4. 회원 탈퇴 0.종료");
			input = sc.nextInt();
			if (input == 1) {
				mdao.signUp();
			}
			if (input == 2) {
				mdao.login();
			}
			if (input == 3) {
				mdao.memberUpdate();
			}
			if (input == 4) {
				mdao.memberWithdrawal();
			}
			if (input == 0) {
				flag = true;
//				sc.close();
			}
		}
	}

	public static Member start(Member mvo) {
		while (flag != true) {
			int input;
			sc = new Scanner(System.in);
			System.out.println("1.학습 2.복습 3.시험 4.단어추가 5.단어검색 0.종료");
			input = sc.nextInt();
			if (input == 1)
				dailyStudy();
			if (input == 2)
				review();
			if (input == 3)
				test();
			if (input == 4)
				addWord();
			if (input == 5)
				finder();
			if (input == 0) {
				flag = true;
				sc.close();
			}

		}
		return mvo;
	}

	public static void dailyStudy() {
		dao.dailyStudy(mvo.getMemberLevel());
		int input;
		while (flag != true) {
			sc = new Scanner(System.in);
			toMainMessage();
			input = sc.nextInt();
			if (input == 0) {
				start(mvo);
			}
		}

	}

	public static void review() {
		System.out.println();
		dao.reviewList();
		while (flag != true) {
			toMainMessage();
			System.out.println("복습 하실 날짜의 번호를 입력하세요");
			int input = sc.nextInt();
			if (input == 0) {
				start(mvo);
			} else {
				dao.review(input);
			}
		}
	}

	public static void test() {
		while (flag != true) {
			dao.testList();
			toMainMessage();
			int input = sc.nextInt();
			if (input == 0)
				start(mvo);
			else
				dao.test(input);
		}
	}

	public static void addWord() {
		while (flag != true) {
			dao.addWord();
		}
	}

	public static void finder() {
		while (flag != true) {
			dao.finder();
		}
	}
	public static void toMainMessage() {
		System.out.println("\n메인메뉴로 가시려면 0을 입력하세요");
	}
}
