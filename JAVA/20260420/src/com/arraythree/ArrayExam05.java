package com.arraythree;

/*		문]	
 * 			컴퓨터와 사용자 사이의 가위, 바위, 보 게임을 구현하시오.
 * 			사용자가 먼저 시작하고, 가위,바위, 보 중 하나를 입력하고 엔터키를 치면
 * 			컴퓨터는 랜덤으로 세 가지 중 하나를 선택하여 출력함
 * 			사용자가 입력한 값과 랜덤으로 출력된 값을 비교하여, 누가 이겼는지를 판단하는 프로그램을 구현하시오.
 * 			단, 그만을 입력하면 프로그램을 종요한다.
 * 				
 * 			배열, 조건문, 반복문, 문자열을 이용하여 구현.
 * 			String str[] = {"가위", "바위", "보"};
 * 			int n = {int}{Math.random() *3)
 * 					n = 0, 1, 2;
 * 
 * 			if(str[n].equals("가위")
 * 
 */

import java.util.*;

public class ArrayExam05 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String str[] = {"가위", "바위", "보"};
		
		
		while(true) {
			System.out.print("사용자 : ");
			String my = sc.nextLine();
			if(my.equals("그만")) {
				return;
			}
			 int n = (int)(Math.random() *3);
			 String com = str[n];
			 
			 System.out.println("컴퓨터 : "+com);
			if(my.equals(com)) {
					 System.out.println("비겼습니다.");
			}else if(// 문자열을 비교할때 equals을 씀
				 (my.equals("가위") && com.equals("보")) ||
				 (my.equals("바위") && com.equals("가위")) ||
				 (my.equals("보") && com.equals("바위"))
			) {
				System.out.println("이기셨습니다.");
			 }else {
				 System.out.println("졌습니다.");
					}
				}
			}
		}


