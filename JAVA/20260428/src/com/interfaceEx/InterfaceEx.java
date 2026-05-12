package com.interfaceEx;

public class InterfaceEx implements InterEx{
	// 클래스를 만들때 인터페이스를 만들수있다
	// 만들게 되면 오버라이드가 자동으로 됨
	@Override
	public int getA() {
		
		return 30;
	}
	
	public static void main(String[] args) {
		InterfaceEx ie = new InterfaceEx();
		System.out.println(ie.getA());
	}
	
}
