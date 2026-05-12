package com.Exam;

/*		문]
 * 			원화를 입력받아 달러로 바꾸는 프로그램을 구현하시오.
 * 			
 * 			1$ = 1500원으로 가정하여 계산함.
 * 
 * 			결과
 * 			원화 입력 : 4500
 * 			4500원은 3.0$입니다.
 * 
 * 
 * 
 */

import java.util.*;
public class Exam01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		final double doller = 1500.0;//변수를 상수화시켜 고정값을 유지시킴.
		
		System.out.print("원화 입력 :");
		int won = sc.nextInt();
		
		double change = won/doller;
		
		System.out.printf("%d원은 $%.1f입니다.",won,change);
		}
		
	}

//* 			출력 결과
//* 			금액 입력 : 65376
//* 			오만원권 : 1매
//* 			만원권 : 1매
//* 			천원권 : 5매
//* 			100원짜리 동전 3개
//* 			50원짜리 동전 1개
//* 			10원짜리 동전 2개
//* 			1원짜리 6개.