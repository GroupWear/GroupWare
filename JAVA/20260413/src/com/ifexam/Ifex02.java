package com.ifexam;

/*
 * 	if -> 가장 기본적인 조건문 -> (만약 ~ 이라면)
 *	만약(if) 조건이 참이면, {}안의 문장을 수행 
 *
 *	형식
 *		if(조건식){
 *			조건이 참일 때 수행하는 문장
 *		}else 
 *		조건이 거짓일 때 수행하는 문장;
 * 
 */
public class Ifex02 {

	public static void main(String[] args) {
		
//		int score = 57;
		
//		if(score > 60) {// 조건이 참일때 아래의 문장을 수행.
//			System.out.println("점수가 60보다 큽니다.");
//		}
		int x = 1;
			System.out.printf("x = %d 일때, 참일 것은 %n", x);
		
		if (x == 0) 
			System.out.println("x == 0 입니다.");
		
		if (x != 0) 
			System.out.println("x != 0 입니다.");
		
		if(!(x==0))
			System.out.println("!(x == 0 )");
		
		if(!(x!=0))
			System.out.println("!(x != 0 )");
		
		x = 1;
		
		}
		
}
