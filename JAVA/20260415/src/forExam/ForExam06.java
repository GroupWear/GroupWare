package forExam;

/*		문]
 * 			사용자로부터 임의의 정수를 입력받아 1부터 입력받은 수까지의
 * 			전체의 합계, 짝수의 합, 홀수 합을 구하되, 각각 결과값을 아래와 같이
 * 			출력하도록 하시오.
 * 
 * 			임의의 정수 : 50 
 * 
 * 			결과 
 * 			1~10까지의 전체의 합계
 * 			1~10까지의 짝수의 합계
 * 			1~10까지의 홀수의 합계
 * 
 * 			1~20까지의 전체의 합계
 * 			1~20까지의 짝수의 합계
 * 			1~20까지의 홀수의 합계
 *
 * 			1~30까지의 전체의 합계
 * 			1~30까지의 짝수의 합계
 * 			1~30까지의 홀수의 합계
 * 
 * 			1~40까지의 전체의 합계
 * 			1~40까지의 짝수의 합계
 * 			1~40까지의 홀수의 합계
 * 
 * 			1~50까지의 전체의 합계
 * 			1~50까지의 짝수의 합계
 * 			1~50까지의 홀수의 합계
 * 
 * 			1~59까지의 전체의 합계 : 
 * 			1~59까지의 짝수의 합계 : 
 * 			1~59까지의 홀수의 합계 :  			
 */

import java.util.*;
public class ForExam06 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int sum = 0, even = 0,odd = 0,i;
		
		System.out.print("임의의 정수 : ");
		int n = sc.nextInt();
		
		
		for(i =1; i <= n ;i++) {
			sum += i;
			
			if(i % 2 == 0) {
				even += i;
			}else {
				odd +=i;
			}
		if(i % 10 == 0 ) {
		System.out.printf("1~%d까지의 전체의 합계 : %d%n",i,sum);
		System.out.printf("1~%d까지의 짝수의 합계 : %d%n",i,even);
		System.out.printf("1~%d까지의 홀수의 합계 : %d%n",i,odd);
		System.out.println();
		}else if (i==n)
		System.out.printf("1~%d까지의 전체의 합계 : %d%n",n,sum);
		System.out.printf("1~%d까지의 짝수의 합계 : %d%n",n,even);
		System.out.printf("1~%d까지의 홀수의 합계 : %d%n",n,odd);
		}
	}
}

	


