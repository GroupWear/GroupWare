package com.interfaceEx;

public class InterfaceEx04 {

	public static void main(String[] args) {
		SamSungPhone ss = new SamSungPhone();
		
		System.out.println(ss.calculate(1, 2));
		ss.stop();
		ss.play();
		ss.schedule();
		ss.sendSMS();
		ss.receiveSMS();
		
		
		

	}

}
