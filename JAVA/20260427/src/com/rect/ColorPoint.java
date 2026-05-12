package com.rect;

public class ColorPoint extends Point2{

	private String color;
	
	public void setColor(String color) {
		this.color = color;
	}
	
	// 컬러 점의 좌표 출력
	
	public void colorshowPoint() {
		System.out.println(color);
		showPoint();
		
	}
	
	
	
	
}
