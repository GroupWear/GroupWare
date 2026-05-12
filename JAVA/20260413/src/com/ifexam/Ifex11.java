package com.ifexam;

/*
 * 	중첩 if 문
 * 
 * 	- if문의 블럭 내의 또 다른 if문을 포함시키는 구조를 의미하며, 이를 중첩 if문이라 함.
 * 
 * 	형식 
 * 		if(조건식1) {
 * 			if(조건식2){
 * 				조건식2가 참일 때 수행하는 문장
 * 			}else {
 *				조건식2가 거짓일 때 수행하는 문장
 *			}
 *		}else {
 *				//조건식1이 false일때 수행하는 문장
 *
 */

import java.util.*;
public class Ifex11 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int score = 0;
		
		char grade = ' ', oper = '0';
		
		System.out.print("변수 입력 : ");
		score = sc.nextInt();
		
		if(score >= 90) {
			grade = 'A';
			if(score >= 96) {
				oper = '+';
			}else if(score < 96) {
				oper = '-';
			}
		}else if(score >= 80) {
			grade = 'B';
			if(score >= 86) {
				oper = '+';
			}else if(score < 86) {
				oper = '-';
			}
		}else if(score >= 70) {
			grade = 'C';
			if(score >= 76) {
				oper = '+';
			}else if(score < 76) {
				oper = '-';
			}
		}else if(score >= 60) {
			grade = 'D';
			if(score >= 66) {
				oper = '+';
			}else if(score < 66) {
				oper = '-';
			}
		}else {
			grade = 'F';
		}
		
		System.out.printf("당신의 학점은 %c%c 입니다.", grade, oper);
		sc.close();
	}
	
}	
	
