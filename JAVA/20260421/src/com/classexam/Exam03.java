package com.classexam;
import java.util.*;

/*		더하기, 빼기, 곱하기, 나누기 기능을 가지고 있는 클래스를 설계
 * 
 * 		필드 : 정수 두개
 * 		기능 : 더하기, 빼기, 곱하기, 나누기 기능
 * 		클래스 명 : Calc
 * 		필드 명 : a, b
 * 		메소드 명 : add, sub, com, div
 * 
 */
class Calc {
	int a, b;
	
	int add(int a, int b) {
		int c = a + b;
		return c;
	}
	int sub(int a, int b) {
		return a - b;
	}
	int com(int a, int b) {
		int result;
		result = a*b;
		return result;
	}
	int div(int a, int b) {
		int c = a / b;
		return c;
	}
	void print ( int d, int e, int f, int g) {
		System.out.println("더하기 : "+d);
		System.out.println("빼기 : "+e);
		System.out.println("곱하기 : "+f);
		System.out.println("나누기 : "+g);
	}
	
}




public class Exam03 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int a; 
		int b;
		
		int d,e,f,g;
		
		Calc ca = new Calc();
		
		System.out.print("정수 : ");
		a = sc.nextInt();
		System.out.print("정수 : ");
		b = sc.nextInt();
		
		d = ca.add(a, b);
		e = ca.sub(a, b);
		f = ca.com(a, b);
		g = ca.div(a, b);
		
		ca.print (d,  e,  f,  g);
		
	}

}
