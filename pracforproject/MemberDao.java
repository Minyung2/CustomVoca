package pracforproject;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class MemberDao implements Serializable {
	private static final long serialVersionUID = -6480609900420593203L;

	static Member vo = new Member();
	static String path = "D:\\javavoca\\memberdb";
	static String memberListFile = path + "\\memberListFile";
	static String form = ".txt";
	static ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	static List<Member> memberList = new ArrayList<>();
	static Scanner sc = new Scanner(System.in);
	public String idRegex = "^[a-zA-Z0-9]{5,12}$";
	public String phoneRegex = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$";
	public String passwordRegex = "^(?=.*[a-z||A-Z])(?=.*[!@#$%^&+=_])(?=.*[0-9]).{8,}$";

	public MemberDao() {
	}

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
	    Member vo = new Member();
	    Scanner scanner = new Scanner(System.in);
	    vo.setMemberID(getValidId(scanner));
	    vo.setMemberPassword(getValidPassword(scanner));
	    vo.setPhoneNumber(getValidPhoneNumber(scanner));
	    memberList.add(vo);
	    fileWriter();

	    System.out.printf("%s님 Vocatest에 가입하신것을 환영합니다%n", vo.getMemberID());
	    VocaExRun.mainScreen();
	}

	// 아이디 입력받기
	private String getValidId(Scanner scanner) {
	    while (true) {
	        System.out.print("사용 하실 아이디 (영문, 숫자로만 이루어진 5 ~ 12자 이하만 사용 가능합니다) : ");
	        String memberId = scanner.next();

	        if (!isValidId(memberId)) {
	            System.out.println("양식에 맞지 않는 아이디입니다.");
	        } else if (isExistingMember(memberId)) {
	            System.out.println("이미 사용하고 있는 아이디입니다.");
	        } else {
	            return memberId;
	        }
	    }
	}

	// 비밀번호 입력받기
	private String getValidPassword(Scanner scanner) {
	    while (true) {
	        System.out.print("사용 하실 비밀번호 : (영문,숫자,특수문자가 포함된 8자 이상만 사용 가능합니다.) : ");
	        String memberPassword = scanner.next();

	        if (!isValidPassword(memberPassword)) {
	            System.out.println("양식에 맞지 않는 비밀번호 입니다.");
	        } else {
	            return memberPassword;
	        }
	    }
	}

	// 핸드폰 번호 입력 받기
	private String getValidPhoneNumber(Scanner scanner) {
	    while (true) {
	        System.out.print("핸드폰 번호를 입력하세요 : (-를 제외한 번호만 입력하세요.) ");
	        String phoneNumber = scanner.next();

	        if (!isValidPhoneNumber(phoneNumber)) {
	            System.out.println("양식에 맞지 않는 번호입니다.");
	        } else if (isExistingPhoneNumber(phoneNumber)) {
	            System.out.println("이미 사용하고 있는 번호입니다.");
	        } else {
	            return phoneNumber;
	        }
	    }
	}

	// 아이디 유효성 체크
	private boolean isValidId(String memberId) {
	    return Pattern.matches(idRegex, memberId);
	}

	// 비밀번호 유효성 체크
	private boolean isValidPassword(String memberPassword) {
	    return Pattern.matches(passwordRegex, memberPassword);
	}

	// 핸드폰 번호 유효성 체크
	private boolean isValidPhoneNumber(String phoneNumber) {
	    return Pattern.matches(phoneRegex, phoneNumber);
	}

	// 아이디 중복 체크
	private boolean isExistingMember(String memberId) {
	    return memberList.stream().anyMatch(member -> member.getMemberID().equals(memberId));
	}

	// 핸드폰 번호 중복 체크
	private boolean isExistingPhoneNumber(String phoneNumber) {
	    return memberList.stream().anyMatch(member -> member.getPhoneNumber().equals(phoneNumber));
	}
	// 로그인 아이디 비밀번호 입력 콘솔
	private String[] getLoginProcess() {
		sc = new Scanner(System.in);
		System.out.print("아이디:");
		String memberId = sc.nextLine();
		System.out.println("비밀번호:");
		String memberPassword = sc.nextLine();
		return new String[] { memberId, memberPassword };
	}

	// 로그인
	public void login() {
		String[] loginCredentials = getLoginProcess();
		String memberId = loginCredentials[0];
		String memberPassword = loginCredentials[1];
		if (isMemberValid(memberId, memberPassword) == true) {
			System.out.println(memberId + "님 환영합니다!");
			Member targetMember = memberList.stream().filter(m -> m.getMemberID().equals(memberId)).findFirst()
					.orElse(null);
			if (targetMember != null) {
				vo.setMemberID(targetMember.getMemberID());
				vo.setMemberPassword(targetMember.getMemberPassword());
				vo.setPhoneNumber(targetMember.getPhoneNumber());
				vo.setMemberLevel(targetMember.getMemberLevel());
				vo.setLastLoginDate(targetMember.getLastLoginDate());
				VocaDao.loginSuccess(vo);
			}
		} else {
			System.out.println("아이디나 비밀번호가 틀립니다");
			login();
		}
	}

	// 로그인 체크
	public boolean isMemberValid(String memberId, String memberPassword) {
		return memberList.stream()
				.anyMatch(m -> m.getMemberID().equals(memberId) && m.getMemberPassword().equals(memberPassword));
	}

	// 회원정보 수정
	public void memberUpdate() {
		String[] loginCredentials = getLoginProcess();
		String memberId = loginCredentials[0];
		String memberPassword = loginCredentials[1];
		if (isMemberValid(memberId, memberPassword) == true) {
			updateGetter(memberId);
			VocaExRun.mainScreen();
		} else {
			System.out.println("아이디와 비밀번호가 틀립니다.");
			memberUpdate();
		}
	}

	public void updateGetter(String memberId) {
		System.out.println(memberList);
		sc = new Scanner(System.in);
		System.out.print("변경하실 비밀번호를 입력하세요");
		String newPassword = sc.next();
		if (!Pattern.matches(passwordRegex, newPassword)) {
			System.out.println("양식에 맞지 않는 비밀번호 입니다.");
			System.out.println();
			updateGetter(memberId);
		} else {
			System.out.println("비밀번호 변경 완료");
			for (Member m : memberList) {
				if (m.getMemberID().equals(memberId)) {
					m.setMemberPassword(newPassword);
				}
			}
		}
		fileWriter();
	}

	// 회원 탈퇴 절차
	public void memberWithdrawal() {
	    String[] loginCredentials = getLoginProcess();
	    String memberId = loginCredentials[0];
	    String memberPassword = loginCredentials[1];
	    
	    if (isMemberValid(memberId, memberPassword)) {
	        System.out.println("탈퇴하시려면 1번 아니면 2번을 눌러주세요.");
	        int withdraw = 0;
	        // inputMismatchException을 피하는 방법
	        while (!sc.hasNextInt()) {
	            System.out.println("잘못된 입력입니다. 1번 아니면 2번을 눌러주세요.");
	            sc.next();
	        }
	        withdraw = sc.nextInt();
	        
	        switch (withdraw) {
	            case 1:
	                removeMember(memberId);
	                deleteLearningFile(memberId);
	                fileWriter();
	                VocaExRun.mainScreen();
	                break;
	            case 2:
	                VocaExRun.mainScreen();
	                break;
	            default:
	                System.out.println("잘못된 입력입니다.");
	                memberWithdrawal();
	                break;
	        }
	    } else {
	        System.out.println("아이디와 비밀번호가 틀립니다.");
	        memberWithdrawal();
	    }
	}
	// 멤버 삭제
	private void removeMember(String memberId) {
		memberList.removeIf(m -> m.getMemberID().equals(memberId));
	}
	// 파일 삭제
	private void deleteLearningFile(String memberId) {
		File userFile = new File("D:\\javavoca\\" + memberId + "의 학습파일.txt");
		userFile.delete();
	}
	// 파일 쓰기
	public void fileWriter() {
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

}
