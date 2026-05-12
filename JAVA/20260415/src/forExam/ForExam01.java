package forExam;

/*		문]
 * 			0에서 부터 입력받은 정수까지의 합계를 구하는 프로그램을 구현하시오.
 * 
 */

import java.util.*;


public class ForExam01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n, sum= 0;
		
		System.out.print("정수 입력 : ");
		n = sc.nextInt();
		
		
		for(int i = 0; i <= n; i++) {
			sum += i;
		}
		System.out.printf("0부터 %d까지의 합계 : %d",n,sum);
	}

}
