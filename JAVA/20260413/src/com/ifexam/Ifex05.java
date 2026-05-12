package com.ifexam;

/*
 *		문]
 *			임의의 정수를 입력받아 홀수인지 짝수인지를 판정하는 프로그램을 구현하시오. 
 * 		문]
 * 			입력받은 수가 3의 배수인지 아닌지를 판별하는 프로그램을 구현.
 */
import java.util.*;
public class Ifex05 {

	public static void main(String[] args) {
		
		System.out.print("정수를 입력 : ");
		
		Scanner sc = new Scanner(System.in);
		int a;
		a = sc.nextInt();
		
		if(a%2 == 0) {
			System.out.println("짝수입니다.");
		}else 
			System.out.println("홀수입니다.");
		
		if(a % 3 ==0) {
			System.out.println("3의 배수입니다.");
		}else System.out.println("3의 배수가 아닙니다.");
		
		sc.close();
		
	}

}
