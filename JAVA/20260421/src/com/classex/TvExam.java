package com.classex;

public class TvExam {

	public static void main(String[] args) {
		// Tv 객체 생성
		Tv t; // Tv 인스턴스를 참조하기 위한 참조변수선언
		// 참조변수는 초기화안해도 자동으로초기화되어서 0이라는 값으로 시작함.
		t = new Tv(); // Tv인스턴스 생성
		System.out.println("현재 채널은 "+t.channel+"입니다.");
		t.channel = 7; // Tv 인스턴스 멤버변수인 channel에 값을 대입했다.
		System.out.println("현재 채널은 "+t.channel+"입니다.");
		t.channelDown();
		System.out.println("현재 채널은 "+t.channel+"입니다.");
		
		
		System.out.println(t.color);
		System.out.println(t.power);
		t.power();
		System.out.println(t.power);
	}

}
