package com.classex;

import java.util.*;
public class RectangleMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		Rectangle r = new Rectangle();
		
		System.out.print("가로 입력 : ");
		r.width = sc.nextInt();
		System.out.print("높이 입력 : ");
		r.height = sc.nextInt();
		
		int area = r.getArea();
		System.out.println("사각형의 넓이 : "+area);
		
		
	}

}
