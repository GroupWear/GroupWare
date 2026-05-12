package com.inner;

/*		enum(열거체)
 * 	 		- 정수형 데이터를 자동으로 증가하여 상수로 만들어 쓰이는 게 하는 객체이다.
 * 			   열거형은 상수를 하나의 객체로인식하고, 여러 개의 상수 객체들을 한곳에 모아둔 것이다. 
 * 
 * 		열거형 구성
 * 			[접근제한자] enum [열거형명] {
 * 				상수1, 상수2, 상수3 
 * 			}
 * 
 * 
 */

public class EnumEx01 {

	public enum Lesson {
		JAVA, JSP, DATABASE
	}
	public static void main(String[] args) {
		Lesson le = Lesson.JAVA;
		System.out.println("Lesson : "+le);
		
		for(Lesson l: le.values()) {
			System.out.println(l+":"+l.ordinal());
		}
	}

}
