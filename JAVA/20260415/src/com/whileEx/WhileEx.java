package com.whileEx;
/*		문]
 * 			임의의 정수를 입력받아 입력받은 다음 그 수만큼 3의 배수를 구하는 프로그램을 작성하시오.
 * 		
 * 			정수 입력 : 5
 * 			3 6 9 12 15
 * 			while문 활용
 */

import java.util.Scanner;

public class WhileEx {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int n,i=1;
		System.out.print("정수 입력 : ");
		n = sc.nextInt();
		
//		while(i <=n) {
//			System.out.print(i*3+"\t");
//			i++;
//		}
		do {
			System.out.print(i*3+"\t");
			i++;
		}while(i <=n);
	}

}
