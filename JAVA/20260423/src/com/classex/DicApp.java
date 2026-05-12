package com.classex;
import java.util.*;

/*		문]
 *			
 *			한영단어 검색 프로그램
 *			한글 단어 : 희망
 *			희망은 hope
 *			한글 단어 : 아가
 *			아가는 사전에 없습니다.
 * 			한글 단어 : 그만
 *			프로그램을 종료합니다.
 */
public class DicApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("한영 단어 검색 프로그램....");
		while(true) {
			System.out.print("한글 단어 : ");
			String kor = sc.next();
			
			if(kor.equals("그만")) {
				System.out.println("프로그램을 종료합니다.");
				return;
			}
			// Dictionary 클래스의 kor2Eng() 메소드 호출하여 결과를 리턴받음
			String eng = Dictionary.kor2Eng(kor);
			// 반환값이 null이면 저의 사전에 없습니다.
			if(eng == null) {
				System.out.println(kor+"은 사전에 없습니다.");
			}else
			// null이 아니면 한글단어와 영어단어 출력
				System.out.println(kor+"은 "+eng);
			}
	}
}
