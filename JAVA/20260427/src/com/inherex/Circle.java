package com.inherex;

public class Circle extends Shape {

	Point center; // 원의 원점 (0,0)
	int r;//반지름
	
	public Circle() {
			this(new Point(0, 0), 100);
	}
	public Circle(Point center, int r) {
		this.center = center;
		this.r = r;
	}
	@Override
	void draw() {//원의 정보를 출력하는 메소드
		System.out.printf("[center=(%d, %d), r= %d, color=%s]%n", center.x, center.y, r, color);
		
	}
}
