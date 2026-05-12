package com.classexam;

/*		문]
 * 			계산 프로그램에서 +, -, *, /, % 연산을 메소드로 작성하여 실행되는 프로그램을 구현하시오.
 * 
 * 			첫번째 정수 : 100 
 * 			연산자 입력 : / 
 * 			두번째 정수 : 20
 * 
 * 			결과 
 * 			100 /  20 = 5
 * 
 */
import java.io.*;

public class Exam05 {

	public static int plus(int x, int y) {
		return x+y;
	}
	public static int min(int x, int y) {
		return x-y;
	}
	public static int mul(int x, int y) {
		return x*y;
	}
	public static int div(int x, int y) {
		return x/y;
	}
	public static int per(int x, int y) {
		return x%y;
	}
	
	public static void display(int a, char b, int c, int d) {
		System.out.println();
		System.out.println(a+" "+b+" "+c+" = "+d);
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int su1, su2, sum = 0;
		char yon = 0;
		
		System.out.print("첫번째 정수 : ");
		su1 = Integer.parseInt(br.readLine());
		do {
			System.out.print("연산자 입력 :");
			yon = (char)System.in.read();
			System.in.skip(2);
			
		}while(yon != '+' && yon != '-' && yon != '*' && yon != '/' && yon != '%');
		System.out.print("두번째 정수 : ");
		su2 = Integer.parseInt(br.readLine());
		
		switch (yon) {
		case '+':
			sum = plus(su1, su2);
			break;
		case '-':
			sum = min(su1, su2);
			break;
		case '*':
			sum = mul(su1, su2);
			break;
		case '/':
			sum = div(su1, su2);
			break;
		case '%':
			sum = per(su1, su2);
			break;
		}
		display(su1,yon,su2,sum);
	}

}
