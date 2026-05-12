package com.swtichex;

/*
 * 		문]
 * 			사용자로부터 임의의 두 정수와 연산자를 입력받아
 * 			해당 연산을 처리하는 프로그램을 구현하시오.
 * 			단, switch case문을 활용.
 * 			
 * 			결과 
 * 			첫번째 정수 : 10
 * 			연산자 [+,-,*,/] : +
 * 			두번째 정수 : 14
 * 			
 * 
 * 			10 + 14 = 24
 */

import java.io.*;

public class SwitchEx02 {

	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int n1, n2;
		char ch;
		double result = 0;
		
		System.out.print("첫번째 정수 : ");
		n1 = Integer.parseInt(br.readLine());
//		메소드	설명
//		Integer.parseInt()	String → int
//		Double.parseDouble()	String → double
//		바꿔주는 역할. 
		
		System.out.print("연산자 [+,-,*,/]");
		ch = br.readLine().charAt(0);
		
		System.out.print("두번째 정수 : ");
		n2 = Integer.parseInt(br.readLine());
		
		 
		
		switch (ch) {
		case '+' :
			result = n1 + n2;
			break;
		case '-' :
			result = n1 - n2;
			break;
		case '*' :
			result = n1 * n2;
			break;
		case '/' :
			result = (double)n1 / n2;
			break;
			
		default:System.out.println("오류");
			
			break;
		}
		// if(price !=0)
		System.out.printf("%d %c %d = %.2f", n1, ch, n2, result);
	}

}
