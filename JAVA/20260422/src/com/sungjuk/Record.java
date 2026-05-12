package com.sungjuk;

/*
 * 		속성만 존재하는 클래스
 * 		
 * 		속성 : 이름, 국어, 영어, 수학, 총점, 평균, 석차
 * 				국어, 영어, 수학 => 배열로 선언
 * 
 */
public class Record {
	String name;
	int[] score = new int[3]; // 0 : 국어, 1 : 영어, 2: 수학
	int tot;
	double avg;
	int rank; 
}
