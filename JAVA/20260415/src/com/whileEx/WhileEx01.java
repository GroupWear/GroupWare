package com.whileEx;

//하나의 정수를 입력받아 그 수만큼"Hello World"를 출력하는 프로그램을 구현하시오.
import java.util.*;
public class WhileEx01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int n;
		System.out.print("정수 입력 : ");
		n = sc.nextInt();
		
		// 변수 선언 및 초기화
		int i =1;
//		while(i <=n) {
//			System.out.println("Hello World");
//			//증감값
//			i++;
//		}
		do {//조건식
			System.out.println("Hello World");
			i++;
		}while(i <=n);
	}

}
