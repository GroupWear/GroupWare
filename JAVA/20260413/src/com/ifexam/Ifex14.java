package com.ifexam;

/*
 * 		문]
 * 			사용자로부터 임의의 년도를 입력받아 입력받은 년도가 윤년인지 평년인지를
 * 			판정하는 프로그램을 구현하시오.
 * 			단, 조건문은 삼항 연산자를 활용하시오.
 * 
 * 			윤년의 판정 조건
 * 		
 * 			윤년 : 년도가 4의 배수이면서 100의 배수가 아니거나, 400의 배수.
 * 			그렇지 않으면 평년.
 * 			4의 배수이면서 100의 배수가 아니면 윤년.
 * 			400의 배수이면 윤년.
 * 
 * 			결과
 * 			년도 입력 : 2000
 * 			2000 ==> 윤년
 * 
 * 			년도 입력 : 2013
 * 			2013 ==>	평년 
 */

import java.util.*;
public class Ifex14 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n;
		String s;
		
		System.out.println("년도 입력 : ");
		n = sc.nextInt();
		
//		
//		if((n % 4 == 0)&&(n% 100 != 0)||(n % 400 == 0)) {
//				s = "윤년";
//		}else {
//			s = "평년";
//		}
		
//		s = ((n % 4 == 0)&&(n% 100 != 0)||(n % 400 == 0)) ? "윤년" :  "평년";
		
		s = ((n % 100 != 0)&&(n % 400 == 0)||(n % 4 == 0)) ? "윤년": "평년";
		
		
		
		System.out.printf(n+ "==>" +s);
		
	}

}
