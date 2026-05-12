package com.ifexam;

/*
 * 		문]
 * 			점수와 학년을 입력받아 60점 이상하면 합격, 미만이면 불합격.
 * 			단, 4학년인경우 70점이상이어야 합격할 수 있다.
 * 			점수의 범위 : 0~100점까지
 * 			학년 : 1~4까지.
 */

import java.util.*;
public class Ifex12 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int score, year;
		
		System.out.print("점수 입력 : ");
		score = sc.nextInt();
		
		System.out.print("학년 입력 : ");
		year = sc.nextInt();
		
		if(score >= 60) {
			if(year != 4) {
				System.out.println("합격입니다.");
			}else if(score >= 70) {
				System.out.println("합격입니다.");
			}else {
				System.out.println("불합격입니다.");
			}
		}else 
			System.out.println("불합격입니다.");
		sc.close();
	}
	
	}


