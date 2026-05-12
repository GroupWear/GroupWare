package com.ifexam;

/*	
 * 	문]
 * 		사용자로부터 임의의 정수를 입력받아 입력받는 정수가 양수인지, 음수인지, 0인지를 판정하는
 * 		프로그램을 구현하시오.
 * 		단, 조건삼항 연산자를 이용하여 구현하시오.
 * 
 * 		결과 
 * 
 * 		정수 입력 : -12 
 * 		-12 ==> 음수
 * 		
 * 		정수 입력 : 12 
 * 		12 ==> 양수
 * 
 * 		정수 입력 : 0 
 * 		0 ==> 영
 */

import java.util.*;

public class Ifex13 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String s;
		int n;
		System.out.print("정수 입력 : ");
		n = sc.nextInt();
		
		/*
		if(n < 0) {
			s = "음수";
		}else if(n == 0) {
			s = "영";
		}else {
			s = "양수";
		}
		*/
		
		s = (n < 0) ? "음수" : (n == 0) ? "영":"음수";
		
		System.out.printf(n+" ==> "+s);
		
		sc.close();
	}

}
