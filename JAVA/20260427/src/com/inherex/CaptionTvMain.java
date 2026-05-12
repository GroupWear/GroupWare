package com.inherex;

public class CaptionTvMain {

	public static void main(String[] args) {
		CaptionTv ctv = new CaptionTv();
		ctv.channel = 10;// 부모 클래스의 멤버필드
		ctv.channelUp();// 부모클래스의 멤버메소드 
		System.out.println(ctv.channel);
		
		ctv.displayCaption("dd");
		
		ctv.caption= true; //자식의 필드상태 
		ctv.displayCaption("dd");
	}

}
