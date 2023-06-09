package pracforproject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Voca implements Serializable{

	private static final long serialVersionUID = -6480609900420593203L;	// Serializable (ObejctWriter)
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");// Date 포맷 지정
	public String nowDate = simpleDateFormat.format(new Date());	// 현재 날짜 기록용
	public String inputDate; 	// 검색 날짜
	
	private String engWord;		// 단어
	private String meaning;		// 뜻
	private int wordLevel;		// 단어 수준
	private int answerCorrection;  // default 0(미학습), 1(정답), -1 (오답)
	private String studyDate = nowDate; 	// 학습일

	
	
	public Voca() {}
	// 생성자
	public Voca(String engWord, String meaning, int wordLevel) {
		this.engWord = engWord;
		this.meaning = meaning;
		this.wordLevel = wordLevel;
		answerCorrection = 0;
		studyDate=null;
	}
	// getter, setter
	String getEngWord() {
		return engWord;
	}
	void setEngWord(String engWord) {
		this.engWord = engWord;
	}
	String getMeaning() {
		return meaning;
	}
	void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public Voca(String engWord, String meaning, int wordLevel, int answerCorrection, String studyDate,
			String testDate) {
		super();
		this.engWord = engWord;
		this.meaning = meaning;
		this.wordLevel = wordLevel;
		this.answerCorrection = answerCorrection;
		this.studyDate = studyDate;
	}
	int getWordLevel() {
		return wordLevel;
	}
	void setWordLevel(int wordLevel) {
		this.wordLevel = wordLevel;
	}
	int getAnswerCorrection() {
		return answerCorrection;
	}
	void setAnswerCorrection(int answerCorrection) {
		this.answerCorrection = answerCorrection;
	}
	
	String getStudyDate() {
		return studyDate;
	}
	void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}
	
	@Override
	public String toString() {
		return getEngWord()+"/"+getMeaning()+"/"+getWordLevel()+"/"+getAnswerCorrection()+"/"+getStudyDate()+"\n";
	}
	
	
	
	
}
