package com.inner;

/*		static 내부 클래스
 * 			static 내부 클래스로 어쩔 수 없이 정의하는 경우가 있는데
 * 			그것은 바로 내부 클래스안에 static 멤버 변수를 가지고 있다면 이때
 * 			어쩔 수 없이 해당 내부 클래스는 static 내부 클래스로 만들어 져야 한다.
 *			
 *		내부 클래스의 지정어로 static을 기입하면 된다. 
 *		따라서 클래스 내부에선 static 변수만 사용이 가능하다.
 *
 *		객체 생성 방법 
 * 		class Outer {
 * 		
 * 			static class Inner {
 * 			
 * 			
 * 			}
 * 		
 * 		}
 * 		Outer.Inner in = new Outer.Inner();
 * 		Outer.Inner in = new Outer.new Inner(); 이렇게 해도 사용가능.
 * 
 */

public class StaticInner {

	int a; //인스턴스 변수
	private int b = 100;
	static int c = 200; // 클래스 변수
	
	static class Inner {// 클래스안에 static을 사용하고 싶으면 클래스 앞에 static을 사용해야함.
			static int d=1000;
			public void printData() {
				// System.out.println("int a : "+a);// 오류 발생
				// System.out.println("int b : "+b);// 오류
				System.out.println("int c : "+c);
				System.out.println("int d : "+d);
			}
	}
	
	public static void main(String[] args) {
		StaticInner.Inner si = new StaticInner.Inner();
		si.printData();
		
	}
	
}
