package com.Exam;

/*		문]
 * 			세개의 정수를 입력받아 세개의 정수 중 중간크기의 수를 출력하는
 * 			프로그램을 구현하시오.
 * 
 * 			정수 세개 입력 : 20 100 33
 * 			중간 크기 : 33
 * 
 */
import java.util.*;
public class Exam04 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int mid;// 중간값을 저장할 변수
		System.out.print("정수 세개 입력 : ");
		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = sc.nextInt();
		
		mid = a;//첫번째 정수값을 중간값으로 초기화함.
		
		// a > b > c
		// c > b > a
		// b > a > c
		// c > a > b
		// b > c > a
		// a > c > b
		
		if((a >= b && b >= c)||(c >= b && b >= a)) {
			mid = b;
		}else if((b >= a && a >= c)||(c >= a && a >= b)) {
			mid = a;
		}else if((b >= c && c >= a)||(a >= c && c >= b)) {
			mid = c;
		}
		System.out.printf("중간 값은 %d", mid);
			
			
//		if((a >= b && a <= c) || (c <= a && a <= b)) {
//			mid = a;
//		}else if((b >= a && b <=c) || (b >= c && b <= a)) {
//			mid = b;
//		}else {
//			mid = c;
//		}
//		
	}
}
