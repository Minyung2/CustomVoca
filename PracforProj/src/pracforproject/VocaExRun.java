package pracforproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VocaExRun {

	public static void main(String[] args) {
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
//		Date nowDate = new Date();
//		String studyDate = simpleDateFormat.format(nowDate); 
//		String testDate = simpleDateFormat.format(nowDate);
//		
//		System.out.println(studyDate);
		VocaDao dao = new VocaDao();
		Voca dto = new Voca();
		dao.init();
		
		while(Menus.flag==false) {
			Menus.start();
		}
		
	}
}
