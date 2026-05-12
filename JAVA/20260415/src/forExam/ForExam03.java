package forExam;

/*		문]
 * 			무한대로 두 정수를 입력받아 합계를 구하는 프로그램을 구현하시오.
 * 
 * 			두개의 정수 입력 : 5 4
 * 			두수의 합계 : 9
 * 			두개의 정수 입력 : 5 4
 * 			두수의 합계 : 9
 * 			두개의 정수 입력 : 5 4
 * 			두수의 합계 : 9
 */

import java.util.*;
public class ForExam03 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n, n1;
		
		for( ; ; ) {
			System.out.print("두개의 정수 입력 : ");
			n = sc.nextInt();
			n1 = sc.nextInt();
			System.out.printf("두수의 합계 : %d%n",n+n1);
		}
		
	}

}
