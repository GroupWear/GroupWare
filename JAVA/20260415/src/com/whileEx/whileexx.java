package com.whileEx;

/*	문]
 * 		입력된 정수의 평균을 구하는 프로그램을 구현하시오.
 * 		
 * 		먼지 입력할 정수의 개수를 입력 받는다.
 * 		입력받은 수만큼 평균을 출력한다.
 * 		입력받은 수는 정수이지만, 평균은 실수로 처리한다.
 * 
 * 		출력결과
 * 		정수의 개수 : 4
 * 		정수입력 : 2
 * 		정수입력 : 7
 *  	정수입력 : 2
 *   	정수입력 : 4
 *    	입력한 정수의 평균 : 3.750000
 */
import java.util.*;

public class whileexx {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int i= 0, sum = 0;
		System.out.print("정수의 개수 : ");
		int n1 = sc.nextInt();
		
		while(i < n1) {
			System.out.print("정수 입력 : ");
			int n2 = sc.nextInt();
			sum += n2;
			i++;
		}
		
		float avg = (float)sum/n1;
		System.out.printf("입력한 정수의 평균 : %.6f",(float)avg);
		
	}

}
