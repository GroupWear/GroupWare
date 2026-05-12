package com.classex;

public class Circle {
	/*
	 * 클래스 변수
	 * 		인스턴스변수 : 객체를 생성해야만 접근이  가능한 변수
	 * 		클래스변수 : 객체를 생성하지않고 클래스명 만으로도 접근이 가능한 변수.
	 * 					   클래스 변수는 자료형 앞에 static을 붙임.
	 */
	
	// 필드 선언 
	int radius;//인스턴스 변수 : 객체를 생성해야만 접근이  가능한 변수
	String name;
				    // 클래스 변수  : 객체를 생성하지않고 클래스명 만으로도 접근이 가능한 변수.
	public Circle() {// 기본 생성자.
	}
	
	// 원의 크기를 계산하는 메소드
	// 원의 넓이 : 반지름*반지름*3.14
	public double getArea() {
		return radius * radius *3.14;
		
	}
	
	public static void main(String[] args) {
		// 객체 생성 : 클래스명 객체명 = new 생성자함수 (); 
		// 				  생성자 함수 : 클래스명과 이름이 동일한 함수
		Circle pizza = new Circle();
		// 피자의 크기를 설정한다.
		// 반지름으로 피자의 크기를 설정한다.
		pizza.radius = 10;
		pizza.name = "글로벌피자";
		double area = pizza.getArea();
		System.out.println(pizza.name+"의 크기는 "+area);
		
		Circle donut = new Circle();
		
		donut.radius = 2;
		donut.name = "글로벌도넛";
		area = donut.getArea();
		
		System.out.println(donut.name+"의 크기는 "+area);
		
	}

}
