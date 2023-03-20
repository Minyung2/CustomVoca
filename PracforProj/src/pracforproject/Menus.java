package pracforproject;

import java.util.Scanner;

public class Menus {

	static Scanner sc = new Scanner(System.in);
	static DBConnect dbConn = new DBConnect();
	static boolean flag = false;
	static VocaDao dao = new VocaDao();

	// start
	public static void start() {
		System.out.println("1.학습 2.복습 3.시험 4.종료");
		int input = sc.nextInt();
		if (input == 1)
			dailyStudy();
		if (input == 2)
			review();
		if (input == 3)
			test();
		if (input == 4)
			flag = true;
	}

	public static void dailyStudy() {
		dao.dailyStudy(1);
		int input;
		while (true) {
			System.out.println("\n메인메뉴로 가시려면 1을 입력하세요");
			input = sc.nextInt();
			if (input == 1) {
				start();
			}
		}
	}

	public static void review() {
		while (true) {
			dao.reviewList();
			System.out.println("이전 단계로 돌아가시려면 0을 입력하세요.");
			System.out.println("복습 하실 날짜의 번호를 입력하세요");
			int input = sc.nextInt();
			if (input == 0) start();
			else dao.review(input);
		}
	}

	public static void test() {
		dao.testList();
		System.out.println("이전 단계로 돌아가시려면 0을 입력하세요.");
		System.out.println("시험 볼 날짜의 번호를 입력하세요");
		int input = sc.nextInt();
		if (input == 0) start();
		else dao.test(input);
	}

	public static void wrongReview() {
	}

	public static void wrongReviewTest() {
	}

}
