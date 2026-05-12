package forExam;

/*		문]
 * 			두 개의 정수를 입력받아 그 사이에 존재하는 정수들의
 * 			합계를 구하는 프로그램을 구현하시오.
 * 			ex) 3 5 => 3+4+5 : 12
 */

import java.util.*;
public class ForExam04 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n1, n2, ex, sum = 0;
		
		System.out.print("두 개의 정수 입력 : ");
		n1 = sc.nextInt();
		n2 = sc.nextInt();
		
		// n2 < n1 = n1 < n2
		// 똑같다. 아래를 쓰면 변수명을 하나 지워도 돼서 메모리가 절약됨.
		// n2 = n2 ^ n1;
		//	n1 = n1 ^ n2;
		// n2 = n2 ^ n1;
		
		if(n2 < n1) {
			ex = n2;
			n2 = n1;
			n1 = ex;
			}
		for(int i = n1; i <=n2;i++){
			sum += i;
		}
		System.out.printf("%d %d : %d",n1,n2,sum);
	}

}
