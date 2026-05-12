package com.classexam;
import java.util.*;
/*		반지름을 입력받아, 원의 넓이와 둘레를 구하시오.
 * 
 *		필드 : 반지름
 *		메소드 : 입력, 넓이, 둘레, 출력 
 * 
 * 
 */
class Circle {
	// 필드 선언
	double r;
	final double PI = 3.141592;// final을 붙히면 변수를 상수화시킴. 변수가 변경이 안댐.
	
	void input() {
		Scanner sc = new Scanner(System.in);
		System.out.print("반지름 : ");
		r = sc.nextDouble();
	}
	
	double area() {
		double result = r*r*PI;
		
		return result;
	}
	double length() {
		double result = (r*2)*PI;
		
		return result;
	}
	void print(double a, double l) {
		System.out.println();
		System.out.println("반지름 : "+r);
		System.out.printf("넓이 : %.2f%n",a);
		System.out.printf("둘레 : %.2f%n",l);
	}
}


public class Exam02 {

	public static void main(String[] args) {
		
		Circle c = new Circle();
		
		c.input();
		
		double a = c.area();
		double l = c.length();
		
		c.print(a, l);
		
	}

}
