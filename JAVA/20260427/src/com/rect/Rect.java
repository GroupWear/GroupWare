package com.rect;

import com.card.SuperTest2;

public class Rect extends SuperTest2{

	private int w, h;
	public Rect() {
		
	}
	
	public void calc(int w, int h ) {
		this.w = w;
		this.h = h;
		area = this.w*this.h;
		write();
	}
	
	@Override
	public void write() {
		System.out.println("w : "+w+", h : "+h);
		System.out.println("사각형의 넓이 : "+area);
		
	}
	
}
