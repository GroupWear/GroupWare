package com.Exam;

/*		문]
 * 			2자리의 정수(10~99사이의 정수)를 입력받아 
 * 			십의 자리와 일의 자리가 같은지를 판정하는 프로그램을 구현하시오.
 * 			
 * 			출력 결과
 * 			두 자리 정수입력 : 77
 * 			10의 자리와 1의 자리가 같습니다.
 * 
 */
import java.io.*;
public class Exam02 {

	public static void main(String[] args) throws IOException{
		
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("두 자리 정수 입력 : ");
		int n = Integer.parseInt(sc.readLine()); 
		// 받자마자 무조건 문자열이기 때문에 int값으로 변환이 필요함 뒤에 계산을 해야해서
		
		if(n < 10 || n >99) {
			System.out.println("정수의 범위를 벗어났습니다.");
			return;
		}
		int ten = n/10;//십의 자리
		int il = n%10;//일의 자리
		
		if(ten == il) {
			System.out.println("10의자리와 일의 자리가 같습니다.");
		}else {
			System.out.println("10의자리와 일의 자리가 다릅니다.");
		}
		
		
}

}
