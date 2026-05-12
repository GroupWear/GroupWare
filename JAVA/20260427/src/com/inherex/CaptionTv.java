package com.inherex;

public class CaptionTv extends Tv {

	boolean caption;//캡션상태(온과 오프로 활용)
	
	void displayCaption(String text) {
		if(caption) {//캡션의 상태가 true일때 매개변수로 전달은 text를 출력
			System.out.println(text);
		}
	}
	
}
