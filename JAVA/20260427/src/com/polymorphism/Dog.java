package com.polymorphism;

public class Dog extends Animal{

	private String name;
	
	public Dog() {
		name = getClass().getSimpleName(); // 현재 클래스의 name을 이름으로 저장하는 방법
	}
	@Override
	public String scream() {
		return "멍멍";
	}
	public String getName() {
		return name;
	}
	
}
