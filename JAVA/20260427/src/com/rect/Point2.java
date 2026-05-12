package com.rect;

/*		문]
 * 			(x, y)의 한 점을 표현하는 Point2 클래스와 
 * 			이를 상속받아 색을 가지는 ColorPoint 클래스를
 * 			작성하고 구현하시오. 
 * 
 */
public class Point2 {
		private int x, y;
		
		public void set(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void showPoint() {//점의 좌표값 출력
			System.out.println("x : "+x+", y : "+y);
		}
		
		
		
}
