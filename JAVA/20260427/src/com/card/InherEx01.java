package com.card;

//자바는 단일 상속만 지원함
public class InherEx01 extends SuperTest1 {

	public InherEx01() {
		System.out.println("Sub Class 클래스 .......");
	}
	public void circle() {
		int r = 10;
		super.area = (double)r * r * 3.14;
		write("원의 넓이 : "+area);
	}
	public void rect() {
		int w = 20, h = 15;
		area = w*h;
		write("사각형의 넓이 : "+area);
	}
	
	
	public static void main(String[] args) {
		InherEx01 ob = new InherEx01();
		ob.circle();
		ob.rect();
	}

}
