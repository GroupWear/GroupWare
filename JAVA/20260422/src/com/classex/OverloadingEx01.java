package com.classex;

/*		메소드 오버로딩
 * 			- 하나의 클래스(동일 클래스)에서 같은 이름으로 정의된 메소드가 여러번 정의되어 쓰이는 것을 의미한다.
 * 			- 같은 이름의 메소드에 인자(매개변수)가 다름
 * 			- 인자가 다르거나 (개수), 자료형이 다르거나, 인자의 순서가 다른경우를 의미함
 * 			- 같은 목적으로 비슷한 동작을 수행하는 메소드들을 모아 이름을 같게 만들어 일관성을 유지함.
 * 	
 * 		메소드 오버로딩 형식
 * 			- 접근제한자 반환형 	메소드명(자료형 인자, 자료형 인자, 자료형이 틀린경우)
 */

// 비 메소드 오버로딩일 경우

public class OverloadingEx01 {
	
	public void intLength(int a) {
		String s = String.valueOf(a);
		System.out.println("입력한 값의 길이 : "+s.length());
	}
	public void floatLength(float f) {
		String s = String.valueOf(f);
		System.out.println("입력한 값의 길이 : "+s.length());
	}
	public void StringLength(String str) {
		System.out.println("입력한 값의 길이 : "+str.length());
	}
	
	public static void main(String[] args) {
		
		OverloadingEx01 oe1 = new OverloadingEx01();
		
		oe1.intLength(1000);
		
		oe1.floatLength(3.14f);
		
		oe1.StringLength("10000");
	}

}
