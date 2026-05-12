package com.person;

public class Person {
	
	private int weight; // 현재 클래스에서만 접근이 가능함.
	int age; // default -> 같은 패키지 내에서 접근가능
	protected int height; // 같은 패키지 내에서 접근가능
	public String name; // 다른 패키지에서도 접근가능
	
	
	public int getWeight() { // private기 때문에 get, set 으로 접근해야함.
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
	
}
