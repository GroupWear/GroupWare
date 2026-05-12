package com.Ex;

// 문자 연산
/* 	유니 코드 문자 
 * 		0 ~ 9 : 48 ~ 57
 * 		A ~ Z : 65 ~ 90
 * 		a ~ z : 97 ~ 122
 */
public class OperEx04 {

	public static void main(String[] args) {
		char a = 'a'; //97
		char d = 'd'; //100
		char zero = '0'; //48
		char two = '2'; // 50
		
		System.out.printf("'%c' - '%c' = %d%n", d, a, d-a);
		
		System.out.printf("'%c' - '%c' = %d%n", two, zero, two-zero);
		
		System.out.printf("'%c' = %d%n", a, (int)a);
		System.out.printf("'%c' = %d%n", d, (int)d);
		System.out.printf("'%c' = %d%n", zero, (int)zero);
		
	}

}
