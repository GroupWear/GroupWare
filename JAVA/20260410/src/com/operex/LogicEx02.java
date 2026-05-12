package com.operex;

/*
 *  문]
 *  	사용자로부터 하나의 문자를 입력 받아서 숫자인지, 영문자인지를 판정하는 프로그램 구현.
 * 		//Scanner 클래스를 이용하여 하나의 문자를 입력받음.	
 * 		1. 입력처리
 * 		2. 조건문(if)
 *  
 */


// 외부 라이브러리를 선언 
// Scanner 클래스를 사용하기 위해 아래의 문장을 추가.
import java.util.Scanner;

public class LogicEx02 {

	public static void main(String[] args) {
		// 스캐너 객체를 생성
		// 객체를 생성하는 방법 : 클래스명 객체명 = new 생성자함수();
		// 		생성자함수명은 클래스명과 동일함(같아야함)
		Scanner sc = new Scanner(System.in);
		char ch = ' ';
		
		System.out.print("문자입력 : ");
		String str = sc.nextLine(); // 콘솔에 문자나 숫자를 작성할수있게됨.
		// Scanner 클래스에 문자를 입력받을 수 있는 메소드를 호출해서
		ch = str.charAt(0);//문자열의 첫번째 문자만 추출해서 저장. ex) 12323 = 1
		
		System.out.println(ch);
		if('0' <= ch && ch <= '9') {
			System.out.println("입력하신 문자는 숫자입니다.");
		}
		if(('a' <=ch && ch <= 'z')||('A' <= ch && ch <= 'Z')) {
			System.out.println("입력하신 문자는 영문자입니다.");
		}
	}

}
