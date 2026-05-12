package com.arraythree;

/*		문]
 * 			돈의 액수를 입력받아 오만원권, 만원권, 천원권, 500원짜리 동전, 100원짜리 동전,
 * 			50원짜리 동전, 10원짜리 동전, 1원짜리 동전 각 몇개로 변환되는지를
 * 			출력하는 프로그램을 구현하시오.
 * 
 * 			출력 결과
 * 			금액 입력 : 65376
 * 			오만원권 : 1매
 * 			만원권 : 1매
 * 			천원권 : 5매
 * 			100원짜리 동전 3개
 * 			50원짜리 동전 1개
 * 			10원짜리 동전 2개
 * 			1원짜리 6개.
 */
import java.util.*;

public class ArrayExam02 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int[] money = {50000,10000,1000,500,100,50,10,1};
		
		int don, result = 0;
		
		System.out.print("금액 입력 : ");
		don = sc.nextInt();
		
		for(int i = 0; i < money.length; i++) {
			
			result = don/money[i];// 몫 -> 동전 개수 
			
			if(result > 0) {// 동전의 개수가 존재하므로
				System.out.println(money[i]+"원 짜리 :"+result+"개");
				don = don%money[i]; // don 갱신
			}
		}
		
		
		
		
		
	}

}

