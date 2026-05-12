package com.interfaceEx;

public interface PhoneInterfce {
	
	final int TIMEOUT = 10000; // 상수 메소드
	void sendCall(); // 추상메소드
	void receiveCall();// 추상메소드
	
	default void printLog() {// void printLog()를쓰면 오류가 나는데 default를 앞에 붙이면 댐 default메소드
		System.out.println("** Phone **");
	}
	
}
