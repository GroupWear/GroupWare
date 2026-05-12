package com.inner;

/*		AnonyInner(익명) 내부 클래스
 * 			- 익명이란 -> 이름이 없는 것을 의미함, 정의된 클래스의 이름이 없다라는 의미.
 * 		
 * 			- Event 와 관련이 있다.
 * 			- interface 구현이 필요없다.
 * 			- 일반 메소드와 내부에서 정의부를 가질 수 있다.
 * 			- abstract를 상속받을 수 있다.
 * 			- 반드시 final로 선언해야한다.
 * 			- implements를 사용할 때는 한개만 사용가능하다.
 * 
 * 		구조 
 * 			class Outer {
 * 				Inner inner = new Inner(){ 익명 내부 클래스
 * 				};
 * 				public void methodA() {// 멤버 메소드
 *	 				new Inner() {
 * 					}
 * 				}
 * 			}
 */

abstract class Testabst {
		int data = 10000;
		public abstract void printData();
		
}


public class AnonyInner {

	Testabst inn = new Testabst() {
		
		@Override
		public void printData() {
			System.out.println(data);
			
		}
	};
			
	public static void main(String[] args) {

		AnonyInner a = new AnonyInner();
		a.inn.printData();
		
		
	}

}
