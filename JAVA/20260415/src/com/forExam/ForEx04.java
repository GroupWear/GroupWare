package com.forExam;

/*
 * 		1에서 10까지의 합계 구하되, 덧셈을 표시하여 출력하는 프로그램을 구현하시오.
 * 
 * 		출력 결과 : 1+2+3+4+5+6+7+8+9+10=55 
 * 		
 * 
 */

public class ForEx04 {

	public static void main(String[] args) {
		
		int sum = 0;
		
		for(int i=1; i<=10; i++) {
			sum += i;
			System.out.print(i);
			if( i<= 9) {
				System.out.print("+");	
			}else  {
				System.out.print("=");
				System.out.print(sum);
			}
			
		}
		
		
	}

}
