package com.Exam;

/*		문]
 * 			삼각형의 변의 길이를 나타내는 정수 3개를 입력받고
 * 			이 세개의 정수로 삼각형을 만들수 있는 지를 판별하는 프로그램.
 * 			단, 삼각형이 되려면 두 변의 합이 다른 한변의 합보다 커야함.
 * 	
 *			결과
 *			정수 세개 입력 : 3 4 5
 *			삼각형이 된다.
 * 
 */
import java.util.*;

public class Exam05 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int a, b, c;
		System.out.print("정수 세개 입력 : ");
		a = sc.nextInt();
		b = sc.nextInt();
		c = sc.nextInt();
		
//		a + c > b
//		a + b > c
//		b + c > a
		
		if(((a + c) > b) || ((a + b) > c) || ((b + c) > a)) {
			System.out.println("삼각형이 된다.");
		}else {
			System.out.println("삼각형이 안된다.");
		}
	}

}
