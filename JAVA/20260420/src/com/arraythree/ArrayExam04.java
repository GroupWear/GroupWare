package com.arraythree;

/*
 * 		아래의 과목과 점수가 짝을 이루도록 2개의 배열을 작성하고
 * 		과목 이름을 입력받아 점수를 출력하는 프로그램을 작성하시오.
 * 		단, '그만'을 입력하면 프로그램을 종료
 * 		
 * 		String coures[] = {"HTML", "JAVA", "DATABASE", "JSP", "SPRING"};
 * 		int score[] = {95, 88, 76, 62, 55};
 * 
 * 		과목이름 : Jaba
 * 		없는 과목입니다.
 * 		과목 이름 : JAVA
 * 		JAVA의 점수는 88
 * 
 * 		과목이름 : 그만
 * 		
 * 		hit) 문자열을 비교할때는 equals()메소드를 이용함
 * 				== 비교연산자는 문자열에서는 사용할 수 없음
 * 				if(coures[i].equals(name)){ //과목명이 내가 입력한 문자열과 같으면 
 * 					int n = score[i];
 * 				}
 * 
 * 
 */

import java.util.*;
class ArrayExam04 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String coures[] = {"HTML", "JAVA", "DATABASE", "JSP", "SPRING"};
		int score[] = {95, 88, 76, 62, 55};
		
		//for( ; ;)
			while (true){
				System.out.print("과목 이름 : ");
				String name = sc.nextLine();
				if(name.equals("그만")){
					return;
				}
				int i;
				for(i=0; i< score.length;i++) {
				if(coures[i].equals(name)){
					System.out.printf("점수는 : %s%n",score[i]);
					break;
					}
				}
				//입력한 과목이 course 배열에 저장되어 있지 않으면
				if(i == coures.length) {
					System.out.println("없는과목입니다.");
				
			}
			
		}
	}
}

