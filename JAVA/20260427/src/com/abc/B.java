package com.abc;

public class B extends A{
	public B() { // 오류 : B()생성자에 대한 A()기본생성자가 없기 때문. 
		// 생성자와 생성자는 짝을 이루는데 자식생성자에서 부모생성자의 기본생성자가 없는 경우에는 
		// 자식생성자에서 오류가 발생함. 이를 해결하기 위해서는 부모클래스의 기본생성자를 생성해주어야한다.
		System.out.println("생성자 B입니다.");
	}
	public B(int x) {
		// 부모 클래스 생성자를 명시적으로 선택 
		// super()는 부모 클래스 생성자를 호출하는 메소드임 
		
		super(x);
		System.out.println("매개변수를 가지는 생성자 B : "+x);
	}
	
}
