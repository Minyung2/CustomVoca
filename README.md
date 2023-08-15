# CustomVoca
교육중 처음으로 진행한 개인프로젝트였습니다. 첫 개인프로젝트를 수행했다는점에 의미를 두고있습니다.<br>
사용자의 레벨 선정 이후 매일 단어 학습, 복습, 퀴즈를 통해 학습하는 영어 단어장 개인프로젝트입니다.

## 🔨 Technology Stack(s)
Backend - Java
- 최초 회원 가입시 CSV로 로컬에 단어장 저장
- 레벨 테스트로 최초 로그인시 사용자 레벨 지정
- 단어 학습, 복습, 퀴즈
- 퀴즈에서 틀린 문제는 다시 풀 수 있게 저장

## 구현 스크린샷
#### 메인화면
<img src="https://github.com/Minyung2/CustomVoca/assets/90165157/0589cc79-73ba-4050-9185-2b8d7ea618b2" width="800" height="400"/>
<br>

#### 로그인화면
<img src="https://github.com/Minyung2/CustomVoca/assets/90165157/13f2d528-e9a6-481e-ac97-bf9859c4a723" width="800" height="600"/>
<img src="https://github.com/Minyung2/CustomVoca/assets/90165157/0e15bf75-7b81-4a0b-99ff-51b88fb00a87" width="800" height="400"/>




#### 학습화면
![image](https://github.com/Minyung2/CustomVoca/assets/90165157/01b262a6-c5ef-46fd-afa6-8ce850f60419)






## - 프로젝트 진행 시 어려웠던 점
- 퀴즈의 답 순서를 섞어주는 방식 및 중복 - 객관식 퀴즈에서 정답 순서를 섞고 중복 때문에 총 문제수가 적게 나오는 경우가 발생하여 중복은 배열에서 빼 버리고 문제와 정답을 섞고 나머지 선택지는 단어장에서 랜덤으로 섞어 버리는 방식으로 구현하였습니다.
- 콘솔의 Scanner inputMismatchException - 스캐너로 nextInt를 받을 때, 숫자가 아닌 경우 inputMismatchException이 뜨는 경우를 회피 하기 위해 !sc.hasNextInt()를 사용하였습니다
