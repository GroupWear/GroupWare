package com.whileEx;

/*		문]
 * 			1번 콜라 2번 사이다 3번 마운틴 
 * 			4번 초코송이 5번 에이스 6번 웨하스
 * 			가 나오는 프로그램을 구현하시오.
 * 			단, while문을 활용.
 * 				상품을 선택한다.
 * 				0을 입력하면 프로그램을 종료한다.
 */

import java.util.*;
public class WhileEx06 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int n = 0;
		
		System.out.println("=== 자판기 프로그램 ===");
		System.out.printf("%-12s %-12s %-12s%n", "1. 콜라", "2. 사이다", "3. 마운틴");
		System.out.printf("%-12s %-12s %-12s%n", "4. 초코송이", "5. 에이스", "6. 웨하스");
        System.out.println("0 입력 시 종료");
        System.out.println("========================");
        
		while(true) {
			System.out.print("몇 번? : ");
			
			int menu = sc.nextInt();
			switch (menu) {
			case 1:
				System.out.println("콜라");
				break;
			case 2:
				System.out.println("사이다");
				break;
			case 3:
				System.out.println("마운틴");
				break;
			case 4:
				System.out.println("초코송이");
				break;
			case 5:
				System.out.println("에이스");
				break;
			case 6:
				System.out.println("웨하스");
				break;
			case 0:
				return;
			default:System.out.println("오류");
				break;
			}
			
		}
		
		
	}

}
