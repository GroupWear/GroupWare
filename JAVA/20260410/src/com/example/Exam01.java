package com.example;
/* 문]
 * 		하나의 문자를 입력받아 그것의 char형 문자와 아스키코드값을 출력하는 
 * 		프로그램을 구현하시오.
 * 
 * 		문자 입력 : a
 * 		결과 : a = 97
 *  
 */

import java.util.Scanner;

public class Exam01 {

	public static void main(String[] args) {
		char ch=' ';// 잉여값이 안들어가게 초기화를 해줌.
		Scanner sc = new Scanner(System.in); // 키보드 입력을 받을 때 사용하는 코드. 
		
		System.out.print("문자입력 : ");// 
		String str = sc.next(); // sc.next(); 는 공백이 나올때까지의 값을 입력받음. ex) 가나다 = 가나다, 가나 다 = 가나.
				ch = str.charAt(0);//문자열 0번째에있는 문자를 ch에 저장.
		
		System.out.printf(" %c = %d", ch, (int)ch);
		
		
	}
}
