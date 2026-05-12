package com.array;

/*	배열의 개념
 * 		1. 배열은 같은 자료형들끼리 모아 두는 하나의 묶음이다.
 * 			- 배열은 같은 타입의 여러 변수를 하나의 묶음으로 다루는 것이다.
 * 		2. 자바에서 하나의 배열은 하나의 객체로 인식된다. 
 * 		3. 같은 자료형이 여러개 반복될때 이를 하나의 변수명으로 관리하며, 
 * 			각각의 구분은 순차적인 첨자(인덱스)를 사용한다.
 * 			첨자(인덱스)의 시작은 0 에서 부터 시작한다.
 * 		4. 참조 자료형(객체==배열)의 크기는 4byte임. 
 * 
 * 	1차원배열
 * 		배열의 선언방법
 * 			int[] 배열명; 또는 int 배열명[];
 * 		
 * 		배열의 초기화
 * 			배열명 = new 자료형[개수] 
 * 			배열명 = {값1, 값2, 값3}
 * 			배열명 = new 자료형[]{값1, 값2, 값3}
 * 
 * 		*****
 * 			new 에 의해서 할당되면 자동으로 초기화된다.
 * 			==> 자료형의 초기값으로 할당된다. 
 */

public class ArrayEx01 {

	public static void main(String[] args) {
		//1차원 배열선언
		//0, 1, 2, 3, 4 
		// score int[] = new int[5];
		int[] score = new int[10]; // 5번까지가 아니라 인덱스가 5가지라는뜻 실제는 4까지있음.
		// int[] score;
		// score = new int[5]; == 같음
//		score[0] = 50;
//		score[1] = 60;
//		score[2] = 70;
//		score[3] = 80;
//		score[4] = 90;
		int v=1;
		for(int i = 0; i < score.length;i++) {
			score[i] = v*10;
			v++;
		}
		
		for(int i = 0; i< score.length;i++) {
			System.out.print(score[i]+"\t");
		}
		System.out.println();
		
		//배열을 활용 반복문
		for(int t : score) {
			System.out.print(t+"\t");
		}
		System.out.println();
		System.out.println("----------------------");
		char[] ch = new char[4];
		
		ch[0] = 'j';
		ch[1] = 'a';
		ch[2] = 'v';
		ch[3] = 'a';
		
		for(int i = 0;i<4;i++) {
			System.out.print(ch[i]+"\t");
		}
		System.out.println();
		for(char aa : ch) {
			System.out.print(aa+"\t");
		}
	}

}
