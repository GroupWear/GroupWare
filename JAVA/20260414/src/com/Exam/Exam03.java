package com.Exam;

/*		문]
 * 			돈의 액수를 입력받아 오만원권, 만원권, 천원권, 500원짜리 동전, 100원짜리 동전,
 * 			50원짜리 동전, 10원짜리 동전, 1원짜리 동전 각 몇개로 변환되는지를
 * 			출력하는 프로그램을 구현하시오.
 * 
 * 			출력 결과
 * 			금액 입력 : 65376
 * 			오만원권 : 1매
 * 			만원권 : 1매
 * 			천원권 : 5매
 * 			100원짜리 동전 3개
 * 			50원짜리 동전 1개
 * 			10원짜리 동전 2개
 * 			1원짜리 6개.
 * 
 */

import java.util.*;
	
public class Exam03 {
	
	final static int oman = 50000;//static은 메인안에 쓸수없음.
	final static int man = 10000;
	final static int chon = 1000;
	final static int obak = 500;
	final static int bak = 100;
	final static int osib = 50;
	final static int sib = 10;
	final static int il = 1;

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int money, res;
		System.out.print("금액 입력 : ");
		money = sc.nextInt();
		
		res = money / oman;//오만원권 개수
		
		money = money % oman; // 입력된 금액에 오만원의 나눔의 나머지
		
		if(res > 0) {
			System.out.printf("오만원권 : %d매%n",res);
		}
		
		res = money / man; // 만원권 개수
		
		money = money %man;
		
		if(res > 0) {
			System.out.printf("만원권 : %d매%n",res);
		}
		
		res = money / chon; // 천원 개수
		
		money = money %chon;
		
		if(res > 0) {
			System.out.printf("천원 : %d매%n",res);
		}
		
		res = money / obak; // 오백원 개수
		
		money = money %obak;
		
		if(res > 0) {
			System.out.printf("오백원 : %d개%n",res);
		}
		
		res = money / bak; // 백원 개수
		
		money = money %bak;
		
		if(res > 0) {
			System.out.printf("백원 : %d개%n",res);
		}
		
		res = money / osib; // 오십원 개수
		
		money = money %osib;
		
		if(res > 0) {
			System.out.printf("오십원 : %d개%n",res);
		}
		
		res = money / sib; // 십원 개수
		
		money = money %sib;
		
		if(res > 0) {
			System.out.printf("십원 : %d개%n",res);
		}
		
		res = money / il; // 일원 개수
		
		money = money %il;
		
		if(res > 0) {
			System.out.printf("일원 : %d개%n",res);
		}
		sc.close();
	}

}
