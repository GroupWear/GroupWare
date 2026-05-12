package forExam;

/*		문]
 *	 		하나의 정수를 입력받은 입력받은 정수의 팩토리얼을 구하는 프로그램을 구현하시오.
 *			
 *			n! = 7
 *
 * 			1 * 2 * 3 * 4 * 5 * 6 * 7 = 5040
 * 		
 * 			결과 
 * 			정수 입력 : 7
 * 			7의 !값은 : 5040
 */

import java.util.*;
public class ForExam05 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("정수 입력 : ");
		int n1 = sc.nextInt();
		int gop = 1;
		
		for(int i = 1; i <= n1;i++) {
			gop *= i;
		}
		System.out.printf("%d의 !값은 : %d",n1,gop);
		
	}

}
