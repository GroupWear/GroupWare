package com.classex;

/*		문]	
 * 			Circle 클래스와 CircleManager클래스를 활용하여 다음과 같이 출력되도록 프로그램을 출력하시오. 
 * 
 * 			x, y, radius : 3.0 3.0 5
 * 			x, y, radius : 2.5 2.7 6
 * 			x, y, radius : 5.0 2.0 4
 * 			가장면적이 큰 원은 (2.5, 2.7) 6
 */
import java.util.*;

class Circle{
	
	private double x;
	private double y;
	private int radius;
	
	public Circle(double x, double y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void show() {
		System.out.println("x, y, radius : "+x+" "+y+" "+radius);
	}

	public double getArea() {
		return Math.PI * radius;
	}
	
}

public class CircleManager {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		double x,y;
		int radius;
		// Circle 크기가 3인 객체배열의 선언 
		Circle[] c = new Circle[3];
		
		
		for(int i = 0; i < c.length; i++) {
			System.out.print("x, y, radius : ");		
			x = sc.nextDouble(); 
			y = sc.nextDouble();
			radius = sc.nextInt();
			//객체 생성
			c[i] = new Circle(x,y,radius);
		}
		
		int bigIndex = 0;
		for(int i = 1; i < c.length; i++) {
			if(c[bigIndex].getArea() < c[i].getArea()) {
				bigIndex = i;
			}
		}
		System.out.println("가장 면적이 큰 원은 "+c);
		
		// 모든 Circle 객체 출력
		for(int i = 0; i < c.length; i++) {
			c[i].show();
		}
	}

}
