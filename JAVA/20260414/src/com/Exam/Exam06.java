package com.Exam;

/*		문]
 * 			1 ~ 99까지의 정수를 입력받고 입력받은 정수 중 3, 6, 9 중 하나가 있는 경우
 * 			"박수 짝"을 출력하고, 두개가 있는 경우 "박수 짝짝"을 출력하는 프로그램을 구현하시오.
 * 
 * 			결과 
 * 			정수 입력 : 33
 * 			"박수 짝짝"
 * 			
 */

import java.util.*;

public class Exam06 {

	public static void main(String[] args) {
		// 변수 선언 -> 정수를입력받아 저장할 변수
		// 			   -> 3, 6, 9를 카운트래서 저장할 변수
		int  n, cnt = 0;// count
		Scanner sc = new Scanner(System.in);
		
		System.out.print("1 ~ 99까지의 정수 입력 : ");
		n = sc.nextInt();
		
		//정수의 범위 체크
		if(n>=1 && n<=99){
		
		int ten, one;
		
		ten = n/10;// 십의 자리
		
		if(ten != 0 && ten % 3 == 0) {// 3으로 나눠짐과 동시에 0이 아닐때, 카운트가 1오름
			cnt++;
		}
		
		one = n%10;// 일의 자리
		if(one != 0 && one % 3 == 0) {// 3으로 나눠짐과 동시에 0이 아닐때, 카운트가 1오름
			cnt++;
		}
		if(cnt == 2) {//카운트를 활용해서 공식을 만들어 사용
			System.out.println("박수 짝짝");
		}else if(cnt == 1) {
			System.out.println("박수 짝");
		}else 
			System.out.println("박수 없음");
		
		}else {
			System.out.println("정수의 범위를 벗어났습니다.");
		}
		
		
		
		
		
	}

}
