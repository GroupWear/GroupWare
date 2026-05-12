package com.inner;

public class MemberInner {

	int a = 10;
	private int b = 100;
	static int c = 200;
	
	class Inner {//내부 클래스	
		public void printData() {
			System.out.println("int a : "+a);
			System.out.println("int b : "+b);
			System.out.println("int c : "+c);
		}
	}
	
	public static void main(String[] args) {
//		MemberInner m = new MemberInner();
//		MemberInner.Inner mi = m.new Inner();
		MemberInner.Inner mi = new MemberInner().new Inner(); // 똑같다.
				
		mi.printData();
	}
}
