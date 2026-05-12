package com.interfaceEx;

public class SamSungPhone extends PDA implements MobilePhone, Mp3Phone {

	@Override
	public void sendCall() {
		System.out.println("duu- duu--");
	}

	@Override
	public void receiveCall() {
		System.out.println("ringringring");
	}

	@Override
	public void play() {
		System.out.println("실행");

	}

	@Override
	public void stop() {
		System.out.println("정지");

	}

	@Override
	public void sendSMS() {
		System.out.println("메세지 보내기");

	}

	@Override
	public void receiveSMS() {
		System.out.println("메세지가 왔습니다.");

	}
	public void flash() {
		return;
	}
	public void schedule() {
		System.out.println("일정 관리 합니다.");
	}
}
