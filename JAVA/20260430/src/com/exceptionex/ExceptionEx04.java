package com.exceptionex;

/*		다중 catch문
 * 			상위 클래스 순서로 예약
 * 		
 * 
 * 
 */

public class ExceptionEx04 {
		
	public static void main(String[] args) {
		System.out.println(1);
		System.out.println(2);
		try {
			System.out.println(3);
			System.out.println(0/0);
			System.out.println(4);
		}catch (ArithmeticException ae) {

			System.out.println("true");

		System.out.println("ArithmeticException");
		}catch (Exception e) {
			System.out.println("Exception");
		}
		
	}
}
