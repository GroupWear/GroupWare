package com.interexam;

public class Tank extends GroupUnit implements Repairable{

	public Tank() {
		super(150);
		hitPoint = MAX_HP;
	}

	@Override
	public String toString() {
		return "Tank";
	}
	
	
}
