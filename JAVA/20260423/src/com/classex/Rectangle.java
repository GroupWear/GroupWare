package com.classex;

/*		문]
 * 			다음 멤버필드를 가지고 있는 직사각형을 표현 Rectangle클래스를 작성하시오.
 * 			
 * 			1. int 타입의 x, y, width, height 값을 매개변수로 받아 필드를 
 * 			초기화 하는 생성자
 * 			2. int square(); 사각형의 넓이를 리턴
 * 			3. void show(); 사각형좌표와 넓이를 출력 
 * 			4. boolean contains(Rectangle r) : 매개변수로 받은 참조변수 r이 
 * 				현재의 사각형안에 있으면 true를 리턴, 사각형을 벗어났으면 false로 리턴.
 *  		
 *  		main()은 아래와 같다.
 *  		
 *  		Rectangle r = new Rectangle(2, 2, 8, 7)
 *  		Rectangle s = new Rectangle(5, 5, 6, 6)
 *  		Rectangle t = new Rectangle(1, 1, 10, 10)
 *  
 *  		r.show();
 *  		System.out.println("s의 면적은 "+s.square());
 *  		
 *  		if(t.contains(r)) {
 *  			System.out.printlb("t는 r을 포함한다.");
 * 				System.out.printlb("t는 s를 포함한다.");	
 *  		}
 *  		
 *  
 *  		출력결과 
 *  		(2, 2)에서 크기가 8 X 7인 사각형
 *  		s의 면적은 36입니다.		
 *  		t는 r을 포함한다.
 *  
 */


public class Rectangle {
	
		private int x;
		private int y;
		private int width;
		private int height;
		
		
		public Rectangle(int x, int y, int width, int height) {
			this.x =x;
			this.y =y;
			this.width = width;
			this.height = height;
		}
		public void show() {
			System.out.println("("+x+", "+y+")에서 크기가 "+width+" X "+height+"인 사각형");
		}
		public int square() {
			return width*height;
		}
		public boolean contains(Rectangle r) {
			if(x < r.x && y < r.y && x+width > r.x + r.width && y+height > r.y+r.height) {
				return true;
			}else {
				return false;
			}
		}
		
	public static void main(String[] args) {
		
		Rectangle r = new Rectangle(2, 2, 8, 7);
		Rectangle s = new Rectangle(5, 5, 6, 6);
		Rectangle t = new Rectangle(1, 1, 10, 10);
		 
		r.show();
		System.out.println("s의 면적은 "+s.square());
		
		if(t.contains(r)) {
			System.out.println("t는 r을 포함한다.");
			System.out.println("t는 s를 포함한다.");	
		}
		
		
	
	}

}
