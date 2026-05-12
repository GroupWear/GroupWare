package com.ifexam;

/*
 * 		문]
 * 			사용자로부터 임의의 정수 세개를 입력받아,
 * 			작은 수부터 큰수를 순서대로 출력하는 프로그램을 작성하시오.
 * 			오름차순 정렬(작은수 -> 중간수 -> 큰수)
 * 
 * 			첫번째 정수 입력 : 16
 * 			두번째 정수 입력 : 8
 *			세번째 정수 입력 : 21
 *  	
 *  		정렬결과 : 8 16 21
 *  
 */
import java.util.*;
public class Ifex10 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n1, n2, n3;
		
		System.out.print("첫번째 정수 입력 : ");
		n1 = sc.nextInt();
		
		System.out.print("두번째 정수 입력 : ");
		n2 = sc.nextInt();
		
		System.out.print("세번째 정수 입력 : ");
		n3 = sc.nextInt();
		
//		n1>n2>n3
//		(n1 > n2 && n2> n3)
//		n1>n3>n2
//		(n1 > n3 && n3> n2)
//		n2>n1>n3
//		(n2 > n1 && n1> n3)
//		n2>n3>n1
//		(n2 > n3 && n3> n1)
//		n3>n2>n1
//		(n3 > n2 && n2> n1)
//		n3>n1>n2
//		(n3 > n1 && n1> n2)
		
		if((n1 > n2) && (n2> n3)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n1,n2,n3);
		}else 
			if((n1 > n3 && n3> n2)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n1,n3,n2);
		}else 
			if((n2 > n1 && n1> n3)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n2,n1,n3);
		}else 
			if((n2 > n3 && n3> n1)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n2,n3,n1);
		}else 
			if((n3 > n2 && n2> n1)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n3,n2,n1);
		}else 
			if((n3 > n1 && n1> n2)) {
			System.out.printf("정렬결과  : %d -> %d -> %d",n3,n1,n2);
		}else System.out.println("오류 입니다.");
		sc.close();
	}

}
