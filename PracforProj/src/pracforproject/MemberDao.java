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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MemberDao implements Serializable {
	private static final long serialVersionUID = -6480609900420593203L;

	static Member vo = new Member();
	static String path = "D:\\javavoca\\memberdb";
	static String memberListFile = path + "\\memberListFile";
	static String form = ".txt";
	static ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	static List<Member> memberList = new ArrayList<>();
	static Scanner sc=new Scanner(System.in);
	public Pattern p;
	public String idRegex = "^[a-zA-Z0-9]{5,12}$";
	public String phoneRegex = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$";
	public String passwordRegex = "^(?=.*[a-z||A-Z])(?=.*[!@#$%^&+=_])(?=.*[0-9]).{8,}$";
	public MemberDao() {}
	
	// 파일, List 새로고침
	@SuppressWarnings("unchecked")
	public static void init() {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(memberListFile + form);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				memberList.clear();
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(memberListFile + form)));
				memberList.addAll((List<Member>) ois.readObject());
			} catch (FileNotFoundException e) {
				System.out.print("");
			} catch (IOException e) {
				System.out.println();
			} catch (ClassNotFoundException e) {
				System.out.println();
			}
		}
	}

	// 회원가입
	public void signUp() {
		sc = new Scanner(System.in);
		getId();
		getPassword();
		getPhoneNumber();
		memberList.add(vo);
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(memberListFile + form)));
			oos.writeObject(memberList);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(vo.getMemberID() + "님 Vocatest에 가입하신것을 환영합니다");
		init();
		VocaExRun.mainScreen();
	}
	// ID 입력받기
	public String getId() {
		System.out.println("사용 하실 아이디 (영문, 숫자로만 이루어진 5 ~ 12자 이하만 사용 가능합니다) : ");
		String memberId = sc.next();
		signupIdChecker(memberId);
		return memberId;
	}
	// 비밀번호 입력받기
	public String getPassword() {
		System.out.println("사용 하실 비밀번호 : (영문,숫자,특수문자가 포함된 8자 이상만 사용 가능합니다.) :");
		String memberPassword = sc.next();
		signupPasswordChecker(memberPassword);
		return memberPassword;
	}
	// 핸드폰 번호 입력 받기
	public String getPhoneNumber() {
		System.out.println("핸드폰 번호를 입력하세요 : \t(-를 제외한 번호만 입력하세요.)");
		String phoneNumber = sc.next();
		signupPhoneNumberChecker(phoneNumber);
		return phoneNumber;
	}

	// 아이디 유효성 체크
	public void signupIdChecker(String memberId) {
		if (!Pattern.matches(idRegex, memberId)) {
			System.out.println("양식에 맞지 않는 아이디입니다.");
			System.out.println();
			getId();
		} else if (idDuplicateChecker(memberId) == true) {
			System.out.println("이미 사용하고 있는 아이디입니다.");
			System.out.println();
			getId();
		} else
			vo.setMemberID(memberId);
	}

	// 아이디 중복 체크
	public boolean idDuplicateChecker(String memberId) {
		return memberList.stream().anyMatch(memberList -> memberList.getMemberID().equals(memberId));
	}

	// 비밀번호 유효성 체크
	public void signupPasswordChecker(String memberPassword) {
		if (!Pattern.matches(passwordRegex, memberPassword)) {
			System.out.println("양식에 맞지 않는 비밀번호 입니다.");
			System.out.println();
			getPassword();
		} else
			vo.setMemberPassword(memberPassword);
	}

	// 핸드폰 번호 유효성 체크
	public void signupPhoneNumberChecker(String phoneNumber) {
		if (!Pattern.matches(phoneRegex, phoneNumber)) {
			System.out.println("양식에 맞지 않는 번호입니다.");
			System.out.println();
			getPhoneNumber();
		} else if (phoneNumberDuplicateChecker(phoneNumber) == true) {
			System.out.println("이미 사용하고 있는 번호입니다.");
			System.out.println();
			getPhoneNumber();
		} else
			vo.setPhoneNumber(phoneNumber);
	}

	// 핸드폰 번호 중복 체크
	public boolean phoneNumberDuplicateChecker(String phoneNumber) {
		return memberList.stream().anyMatch(memberList -> memberList.getPhoneNumber().equals(phoneNumber));
	}

	// 로그인
	public void login() {
		sc = new Scanner(System.in);
		System.out.print("아이디:");
		String memberId = sc.nextLine();
		System.out.println("비밀번호:");
		String memberPassword = sc.nextLine();
		if (loginChecker(memberId, memberPassword) == true) {
			System.out.println(memberId + "님 환영합니다!");
			int targetIndex = memberList.stream().map(Member::getMemberID).collect(Collectors.toList())
					.indexOf(memberId);
			vo.setMemberID(memberList.get(targetIndex).getMemberID());
			vo.setMemberPassword(memberList.get(targetIndex).getMemberPassword());
			vo.setPhoneNumber(memberList.get(targetIndex).getPhoneNumber());
			vo.setMemberLevel(memberList.get(targetIndex).getMemberLevel());
			vo.setLastLoginDate(memberList.get(targetIndex).getLastLoginDate());
			VocaDao.loginSuccess(vo);
		} else {
			System.out.println("아이디나 비밀번호가 틀립니다");
			login();
		}
	}

	// 로그인 체크
	public boolean loginChecker(String memberId, String memberPassword) {
//		return memberList.stream().anyMatch(memList -> memList.getMemberID().equals(memberId))
//				&& memberList.stream().anyMatch(memList -> memList.getMemberPassword().equals(memberPassword));
		boolean checker=false;
		for(Member m : memberList) {
			if(m.getMemberID().equals(memberId)&&m.getMemberPassword().equals(memberPassword)) checker=true;
		}
		return checker;
	}
	
	// 회원정보 수정
	public void memberUpdate(){
		sc= new Scanner(System.in);
		System.out.print("아이디:");
		String memberId=sc.next();
		System.out.print("비밀번호:");
		String memberPassword=sc.next();
		if(loginChecker(memberId, memberPassword)==true) {
			updateGetter(memberId);
			VocaExRun.mainScreen();
		}else {
			System.out.println("아이디와 비밀번호가 틀립니다.");
			memberUpdate();
		}
	}
	public void updateGetter(String memberId) {
		System.out.println(memberList);
		sc= new Scanner(System.in);
		System.out.print("변경하실 비밀번호를 입력하세요");
		String newPassword=sc.next();
		if (!Pattern.matches(passwordRegex, newPassword)) {
			System.out.println("양식에 맞지 않는 비밀번호 입니다.");
			System.out.println();
			updateGetter(memberId);
		}else {
			System.out.println("비밀번호 변경 완료");
			for(Member m : memberList) {
				if(m.getMemberID().equals(memberId)) {
					m.setMemberPassword(newPassword);
				}
			}
		}
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(memberListFile + form)));
			oos.writeObject(memberList);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	// 회원 탈퇴
	public void memberWithdrawal(){
		sc= new Scanner(System.in);
		System.out.print("아이디:");
		String memberId=sc.next();
		System.out.print("비밀번호:");
		String memberPassword=sc.next();
		if(loginChecker(memberId, memberPassword)==true) {
			System.out.println("탈퇴하시려면 1번 아니면 2번을 눌러주세요.");
			int withdraw = sc.nextInt();
			if(withdraw==1) {
				int target= memberList.stream().map(Member::getMemberID).collect(Collectors.toList()).indexOf(memberId);
				memberList.remove(target);
				try {
					File userFile = new File("D:\\javavoca\\"+memberId+"의 학습파일.txt");
					userFile.delete();
					oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(memberListFile + form)));
					oos.writeObject(memberList);
					oos.flush();
					oos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				init();
				VocaExRun.mainScreen();
			}if(withdraw==2) {
				VocaExRun.mainScreen();
			}
		}else {
			System.out.println("아이디와 비밀번호가 틀립니다.");
			memberWithdrawal();
		}
	}
	
	
}
