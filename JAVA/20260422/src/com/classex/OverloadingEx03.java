package com.classex;

/*		문]
 * 			메소드의 오버로딩을 활용하여
 * 			사각형의 넓이와 삼각형의 넓이를 출력하는 프로그램을 구현하시오.
 */

public class OverloadingEx03 {
	
	public int area(int w, int h) {
		return w * h;
	}
	public double area(int r) {
		return (double)r * r * 3.141592;
	}
	
	public void write(int result) {
		System.out.println("사각형의 넓이 : "+result);
	}
	public void write(double result) {
		System.out.println("원의 넓이 : "+result);
	}
	
	public static void main(String[] args) {
		OverloadingEx03 oe3 = new OverloadingEx03();
		
		int rect = oe3.area(10, 5);
		double cir = oe3.area(10);
		
		oe3.write(rect);
		oe3.write(cir);
		
		
		
		
		
	}
 
}
