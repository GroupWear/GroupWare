package com.forExam;
// 구구단중 단을 입력받아 출력하는 프로그램을 구현하시오.

import java.util.*;
public class ForEx05 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int dan;
		System.out.print("단 입력 : ");
		dan = sc.nextInt();
		
		for(int i = 1; i<10; i++) {
			System.out.printf("%d X %d = %d%n",dan,i,dan*i);
		}
		sc.close();
	}

}
