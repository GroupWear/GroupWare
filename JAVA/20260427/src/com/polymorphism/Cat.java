package com.polymorphism;

public class Cat extends Animal{

	private String name;
	
	public Cat() {
		name = "고양이";
	}
	
	@Override
	public String scream() {
		return "냥";
	}
	public String getName() {
		return name;
	}
}
