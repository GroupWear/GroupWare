package com.sungjuk;

/*		속성(필드) : 인원수, Record 객체 배열
 * 		기능(메소드) : 
 * 						입력 : set() : 인원수 입력받는 기능 - 객체배열을 선언해서 인원 수 만큼 객체를 생성함
 * 						계산 : input() : 데이터 입력, 총점 계산, 평균계산
 * 								ranking() : 석차를 구하는 기능
 * 						출력 : print() : 결과를 출력하는 기능
 * 
 */

import java.util.*;
public class Sungjuk {
	Scanner sc = new Scanner(System.in);
	
	//주요 속성(변수, 필드)
	int inwon;
	
	//객체 배열 선언
	Record[] re;
	
	// 1. 인원수 입력기능(메소드)
	public void set() {
		
		do {
			System.out.print("인원 수(1~100) : ");
			inwon = sc.nextInt();
		}while( inwon > 100 || inwon < 1);
		//입력받은 인원수로 객체배열을 생성함.
		// Record 클래스의 배열 변수를 inwon만큼 생성
		// inwon만큼 객체배열의 개수를 설정
		// 인스턴스는 생성하지않았음.
		re = new Record[inwon];
		
	}
	
	// 데이터 입력, 총점 계산, 평균 계산
	public void input() {
		String[] title = {"국어점수 : ","영어점수 : ", "수학점수 : "}; 
				
				for(int i = 0; i < inwon; i++) {
					//  객체 배열을 생성해서 (메모리 공간을 할당)
					re[i] = new Record(); // 매우 중요
					
					System.out.print((i+1)+"번째 이름 입력 : ");
					re[i].name = sc.next();
					
					// 각 과목별 점수를 입력받음
					for(int j =0; j < title.length;j++) {
						// 과목별 점수를 입력받기 위한 안내 메시지 출력
						System.out.print(title[j]);
						
						// 점수를 입력받아서 Record 클래스의 score 배열에 저장함.
						re[i].score[j] = sc.nextInt();
						
						//입력받은 각 과목을 누적해서 총점을 구한다.
						re[i].tot += re[i].score[j];
								
								
					}
					//평균 계산
					re[i].avg = re[i].tot/3.0;
		}
	}
	// 석차 계산
	public void ranking() {
		int i, j;
		
		// 모든 학생의 등수를 1로 초기화
		for(i = 0; i < inwon;i++) {
			re[i].rank = 1;
		}
		for(i = 0; i< inwon-1;i++) {
			for(j = i+1; j < inwon;j++) {
				if(re[i].avg > re[j].avg) {
					re[j].rank++;
				}else if(re[j].avg > re[i].avg) {
					re[i].rank++;
				}
			}
		}
	}
	// 석차를 포함한 결과 출력
	public void display() {
		ranking();
		System.out.println();
		System.out.println("이름  국어  영어  수학\t  총점  평균\t  석차");
		for(int i = 0; i < inwon; i++) {
			// 이름 출력
			System.out.printf("%s",re[i].name);
			// 성적 출력
			for(int j = 0; j < 3; j++) {
			System.out.printf("%5d",re[i].score[j]);
		 }
			//총점 , 평균, 석차
			System.out.printf("\t%d",re[i].tot);
			System.out.printf("\t%.2f",re[i].avg);
			System.out.printf("\t%d",re[i].rank);
			System.out.println();
		}
		
	}
}

