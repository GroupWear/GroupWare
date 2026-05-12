package com.inner;

/*	 Local 내부 클래스
 * 		Local 내부클래스는 특정 메소드 안에서 정의되는 클래스를 의미함
 * 		특정 메소드 안에서 선언되는 지역변수와 같은 것임
 * 		메소드가 호출될때 생성할 수 있으며 메소드의 수행이 다 끝나면 지역변수와 같이 자동으로 소멸된다.
 * 
 * 		************ 잘 사용하지 않는다.
 * 		메소드 내부에 클래스가 존재하기 때문에 메소드 실행시 실행된다.
 * 		
 * 		구조 
 * 		class Outer {
 * 			
 * 			void method() {
 * 				class Inner {
 * 				
 * 				}
 * 			}
 * 		}
 * 
 * 
 */

public class LocalInner { // 외부 클래스

	int a = 100;// 멤버변수

	public void innerTest(int k) { // 일반 메소드
			int b = 200;// 지역변수 
			final int c = k; // 상수  
			
			class Inner {// 지역 내부 클래스
				//지역 내부 클래스는 외부클래스의 멤버변수와 상수들만 접근이 가능하다.
				public void getData() {
					System.out.println("int a : "+a);
					System.out.println("int b : "+b);
					System.out.println("final int c : "+c);
				}
			}
			Inner i = new Inner();
			// 메소드 내에서 로컬 내부클래스의 객체를 생성
			
			i.getData();
			// 객체를 통해서 메소들를 호출
	}

	public static void main(String[] args) {
		LocalInner local = new LocalInner();
		local.innerTest(100);
		 
		
	}
}
