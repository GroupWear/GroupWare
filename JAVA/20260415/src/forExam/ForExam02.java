package forExam;

/*		문]
 * 			양의 실수만 입력받아 입력받은 수까지의 평균을 구하시오.
 * 			단, 음의 실수가 입력되는 합계를 출력하도록 구현하시오.
 * 
 *			결과 
 *				양의 실수 : 1.1
 *				양의 실수 : 2.2			
 *				양의 실수 : 3.3
 *				양의 실수 : 4.4
 *				음의 실수 : -5.5
 *				지금까지의 평균 : 2.2
 */

import java.util.*;
public class ForExam02 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		float sum = 0f, n=0f;
		int cnt= 0;
		for(cnt =0; n >= 0.0; cnt++ ) {
			sum += n;//누적합계
			System.out.print("양의 실수 : ");
			n = sc.nextFloat();
		}
		System.out.println("지금까지 입력한 양의 실수의 평균 : "+sum/cnt);
//		double n1, n2, n3, n4, n5;
//		
//		System.out.print("양의 실수 : ");
//		n1 = sc.nextDouble();
//		System.out.print("양의 실수 : ");
//		n2 = sc.nextDouble();
//		System.out.print("양의 실수 : ");
//		n3 = sc.nextDouble();
//		System.out.print("양의 실수 : ");
//		n4 = sc.nextDouble();
//		System.out.print("음의 실수 : ");
//		n5 = sc.nextDouble();
//		
//		double sum = n1+n2+n3+n4+n5;
//		double avg = (double)sum/5;
//		
//		System.out.printf("지금까지의 평균 : %.1f",(double)avg);
		
		
		
	}

}
