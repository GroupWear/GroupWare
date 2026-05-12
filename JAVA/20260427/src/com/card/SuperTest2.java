package com.card;

public class SuperTest2 {

	protected double area;
	private String title;
	
	public SuperTest2() {	
		
	}
	public SuperTest2(String title) {
		this.title = title;
	}
	
	public void write() {
		System.out.println(title+area);
	}
	
	
}
