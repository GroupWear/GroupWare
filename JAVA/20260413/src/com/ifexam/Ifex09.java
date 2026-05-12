package com.ifexam;

/*		
 * 		문]
 * 			사용자로부터 임의의 두 정수와 연산자를 입력받아
 * 			해당 연산자를 처리하는 프로그램을 구현하시오.
 * 
 * 			결과
 * 				첫번째 정수 입력 : 10
 * 				두번째 정수 입력 : 14
 *				연산자 입력[+,-,*,/] : + 
 *				10 + 14 = 24
 * 				단, if문 활용
 */
import java.io.*;
import java.util.*;
public class Ifex09 {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		
		int n1, n2, result;
		char op;
		
		System.out.print("첫번째 정수 입력 : ");
		n1 = Integer.parseInt(br.readLine());
		
		System.out.print("두번째 정수 입력 : ");
		n2 = Integer.parseInt(br.readLine());
		
		System.out.print("연산자 입력 [+,-,*,/] : ");
		op = (char)System.in.read();
		
		
		
		if(op == '+') {// +
			result = n1 + n2;
			System.out.printf("%d %c %d = %d", n1, op, n2, result);
		}else 
			if(op == '-') {// -
			result = n1 - n2;
			System.out.printf("%d %c %d = %d", n1, op, n2, result);
		}else 
			if(op == '*') {// *
			result = n1 * n2;
			System.out.printf("%d %c %d = %d", n1, op, n2, result);
		}else 
			if(op == '/') {// /
				result = n1 / n2;
				System.out.printf("%d %c %d = %.2f", n1, op, n2, (float)result);
		}else System.out.println("오류");
		
	}

}
