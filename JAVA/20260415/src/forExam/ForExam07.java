package forExam;

/*		문]
 * 			임의의 정수를 입력받아 입력받은 정수의 배수를 구하는 프로그램을 작성하시오.
 * 			
 * 			정수 입력 : 3
 * 			3 6 9 12 15
 * 			
 */

import java.util.*;
public class ForExam07 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("정수 입력 : ");
		int n = sc.nextInt();
		
		
		for(int i=1; i <= n ; i++) {
			if(i % 3 == 0) {
				System.out.print((i*3) +"\t");
			}
			
		}
	}

}
