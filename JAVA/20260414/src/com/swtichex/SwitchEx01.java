package com.swtichex;

/*		switch ~ case 
 * 			- 조건식을 먼저 계산한 다음, 그 결과와 일치하는 case문으로 이동함.
 * 			이동한 case문 아래에 있는 문장을 수행하고 나서 break문을 만나면 
 * 			switch문을 종료한다.
 * 		
 * 		switch ~ case 실행 순서
 * 			1. 조건식을 계산한다.
 * 			2. 조건식을 결과와 일치하는 case문으로 이동한다.
 * 			3. 이동후 case문 아래있는 문장을 수행한다.
 * 			4. break문을 만나면 switch ~ case문을 종료하고 빠져나온다.
 * 
 * 		switch ~ case 문의 제약 조건
 *			1. switch ~ case문의 조건식 결과는 정수 또는 문자열이여야한다.
 *			2. case문의 값은 정수, 상수만 가능하며 중복불가.
 * 		
 * 		문]
 * 			세과목의 점수를 입력받아 총점과 평균을 구하고,
 * 			평균값을 이용하여 학점을 판정하는 프로그램을 구현하시오.
 * 			단, switch ~ case문을 활용.
 */

import java.util.*;

public class SwitchEx01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int kor, eng, mat, sum=0;
		double avg = 0;
		char grade = ' ';
		
		System.out.print("국어 점수 : ");
		kor  =sc.nextInt();
		
		System.out.print("영어 점수 : ");
		eng  =sc.nextInt();
		
		System.out.print("수학 점수 : ");
		mat  =sc.nextInt();
		
		sum = kor + eng + mat;
		
		avg = sum/3.0;
		
		// 학점 판정 
		switch ((int)(avg/10)) {
		case 9:
			grade = 'A';
			break;
			
		case 8:
			grade = 'B';
			break;
			
		case 7:
			grade = 'C';
			break;
			
		case 6:
			grade = 'D';
			break;
			
		default : 
			grade = 'F';
			break;
			
		}
		System.out.printf("총점 : %d, 평균 : %.2f%n",sum,avg);
		System.out.println("당신의 학점은 "+grade+"입니다.");
		sc.close();
	}

}
