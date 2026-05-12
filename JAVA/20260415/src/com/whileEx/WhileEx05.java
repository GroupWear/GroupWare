package com.whileEx;

/*		문]
 * 			-50~1까지 수를 출력하는 프로그램을 구현하시오.
 * 			단, 한줄에 5개씩만출력, 수들사이에 간격은 탭간격.
 */
public class WhileEx05 {

	public static void main(String[] args) {
		
		int i = -50;
		
		while(i <= 1) {
			System.out.printf("%2d\t",i);
			i++;
			if(-i % 5 ==0 ) {
				System.out.println();
			}
		}
	}
}
