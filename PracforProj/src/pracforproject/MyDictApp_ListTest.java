package pracforproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

// 메소드 생성 연습 : 파일 읽어오기, 파일 저장하기, 단어 추가하기, 단어 전체보기
public class MyDictApp_ListTest {
	static Scanner sc = new Scanner(System.in);
	static List<Word> mydicts = new ArrayList<>(); // 단어장 역할을 하는 컬렉션(List) 입니다.

	public static void main(String[] args) {
		String filename = "D:\\javavoca\\datavoca.txt"; // 사용자가 선택할 수 있는 기능 -> main 메소드 안에서 구현
		boolean flag = true;
		try {
			dictRead(filename);
		} catch (FileNotFoundException e1) {
// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 지정된 파일에 저장된 데이터 읽어와서 mydicts 리스트에 저장하기
		while (flag) {
			System.out.println("----------------------------------------------------- ");
			System.out.println("1.새로운 단어 추가 2.저장된 단어들 조회 3.파일에 저장 4.프로그램 종료 ");
			System.out.println("----------------------------------------------------- ");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			switch (sel) { // if로 한다면 sel 이 String 이므로 equals 메소드로 비교
			case "1": // switch문 case에 문자열 비교 가능.
				wordAdd();
				break;
			case "2":
				wordList();
				break;
			case "3":
				try {
					dictSave(filename);
				} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "4":
				System.out.println("프로그램 종료.");
				flag = false;
				break;
			default:
				break;
			}// switch end
		} // while end
	} // main end

	private static void dictRead(String filename) throws FileNotFoundException {
		Scanner fsc = null; // 파일 입력을 위한 객체 참조 변수
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		File file = new File(filename);
		fsc = new Scanner(file);
		while (fsc.hasNextLine()) {
			sb.append(fsc.nextLine()).append("*");
		}
		st = new StringTokenizer(sb.toString(), "*");
		List<String> kor;
		while (st.hasMoreTokens()) {
			Word w = new Word(st.nextToken());
			String temp = st.nextToken(); // 첫번째로 읽은 토큰은 english 필드값
// korean 필드값 -> List<String) 타입 객체로 변환해야함
			temp = temp.substring(1, temp.length() - 1);
// kor = Arrays.asList(temp.split(", "));
			kor = new ArrayList<String>(Arrays.asList(temp.split(", ")));
			w.setKoreans(kor);
			mydicts.add(w);
		}
		fsc.close();
	}

// mydicts 리스트를 filenames 인자값 파일명으로 저장. - Exception 처리 방법 1
	private static void dictSave_(String filename) { // filename 이 존재하지 않으면 -> filename 으로 파일 생성
		PrintWriter fpw = null;
		try {
			fpw = new PrintWriter(filename);
// 파일 출력 print
			fpw.println(mydicts.get(0));
			fpw.println(mydicts.get(1));
			fpw.close();
		} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// - Exception 처리 방법 2
// 현재 메소드에서 Exception 처리 안하고 호출한 main 메소드로 처리를 떠넘긴다.
	private static void dictSave(String filename) throws FileNotFoundException { // filename 이 존재하지 않으면 -> filename 으로
// 파일 생성
		PrintWriter fpw = new PrintWriter(filename);
// 파일 출력 print
		for (Word w : mydicts)
			fpw.println(w);
		fpw.close();
	}

// mydict 모두 확인
	private static void wordList() {
		System.out.println("[저장된 단어 조회] 입니다.");
		for (Word w : mydicts)
			System.out.println(w);
	}

// 단어 추가 메소드
	private static void wordAdd() {
		System.out.println("[새로운 단어 추가] 입니다.");
		do {
			System.out.println("English :");
			String eng = sc.nextLine().trim(); // 입력 받고 양 끝 공백제거
			System.out.println("우리말 뜻 :");
			String kor = sc.nextLine().trim();
// 지금 입력된 eng 단어가 mydicts 리스트에 있는지?
			int index = -1;
			for (int i = 0; i < mydicts.size(); i++) {
// 인덱스 1번째 Word 객체 가져오기
				Word w = mydicts.get(i);
				if (w.getEnglish().contentEquals(eng)) {
					index = i;
					break;
				}
			}
			List<String> koreans;
			if (index == -1) { // mydicts에 지금 입력된 eng 가 이미 있는지 검사
				koreans = new ArrayList<>();
				koreans.add(kor);
				mydicts.add(new Word(eng, koreans)); // mydicts 리스트에 Word 객체 추가 - 필드값
			} else { // mydicts 리스트에서 index 위치의 Word 객체를 가져와서 koreans 필드에 kor 추가
				Word w = mydicts.get(index);
				koreans = w.getKoreans();
				koreans.add(kor);
// mydicts.get(index).getKoreans().add(kor); // 위와같은코드
			}
			System.out.println("단어 추가를 계속 하시겠습니까?(y/n) >>> ");
		} while (sc.nextLine().toLowerCase().equals("y"));
	}
}