package com.person;

public class Student extends Person {

	public void set() {
		age = 30; // 부모클래스의 default 멤버 접근가능.
		name = "가"; // 부모클래스의 public 멤버 접근 가능.
		height = 175; // 부모클래스의 protected 멤버 접근가능.
		// weight = 65; // 부모클래스의 멤버필드가 private라서 접근 불가능.
		setWeight(65); // private 멤버인 weight는 setter 메소드로 접근하여, 사용가능.
		
	}
	public void view() {
		System.out.println("나이 : "+age+", 이름 : "+name+", 신장 : "+height+", 몸무게 : "+getWeight());
	}
	
}
