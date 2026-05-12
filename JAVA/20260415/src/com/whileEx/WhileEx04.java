package com.whileEx;

/*		문]
 * 			-1^2+^2-3^2 +4^2 ......... + 100^2
 *		 	구하는 프로그램을 작성하시오.
 *			단, while문 사용
 * 
 */

public class WhileEx04 {

	public static void main(String[] args) {
		
		int i = 1, sum = 0, sum1 = 0, sum2 = 0, plus=1;
		
		while(i <= 100) {
			
//			if(i % 2 == 0) {
//				sum1 += i*i;
//			}else
//			if(i % 2 == 1) {
//				sum2 += (-i*i);
//			}
//			sum = sum1+sum2;
			
			plus = -plus;
			
			sum += plus*(i*i);
			i++;
		}
		System.out.printf("결과 : %d", sum);
		
		
		
	}

}
