package com.classex;

/*		
 * 		생성자
 * 			- 메모리 내에 객체가 생성될 때 호출되어 객체의 구조를 인식하게 하고
 * 			   생성되는 멤버 변수들을 초기화 작업을 하는데 그 목적을 둔다.
 * 			- 생성자명은 클래스명과 같아야하고, 반환형을 정의 하지 않는다.
 * 			- 프로그래머가 어떠한 생성자도 정의하지 않았을 경우 컴파일러가 기본 생성자를 
 * 			   자동으로 만들어 줌
 * 				기본생성자 : 매개변수가 없는 생성자를 의미함
 * 
 * 		생성자의 접근제한 의미
 *		- 생성자의 접근을 제한 둘 경우 해당 객체를 생성 할 수 있는 접근 권한을 가짐.
 *		- 클래스의 접근제한이 public 으로 정의 되어도 private로 정의하면 
 *		클래스 내부에서만 접근이 가능하다.
 *		- 만약 protected로 정의되는 클래스는 상속관계의 생성할 수 있음
 *  		
 *		생성자의 구성 
 *			[접근제한자] [생성자명] (자료형 인자1, 자료형 인자2...){
 *			수행문; 
 *			수행문;
 *		} 
 * 		
 * 		생성자의 특징
 * 			1. 클래스명과 반드시 이름이 같아야한다.
 * 			2. 반환형을 명시할 수 없다.
 * 			3. 클래스 내부에 생성자가 없을 때는 컴파일러가 자동으로 기본생성자를 만든다.  		 
 * 			4. 하나의 클래스에는 인자가 서로 다른 생성자가 여러 개 있을 수 있다.(생성자 오버로딩)
 * 			5. 생성자의 첫번째 라인에서 this(인자) 생성자를 사용해서 다른 생성자를 호출하여
 * 				사용할 수 있다. 
 */

class MyClass {
	
	private String name;
	private int age;
	
	public MyClass() {//기본 생성자 - > 컴파일러가 자동으로 만들어줌. (매개변수가 없음)
		name = "무명";
		
	}
	public MyClass(String n) {// 생성자 오버로딩
		name = n; // 객체를 생성자 인자로 전달된 값을 가지고 클래스 변수를 초기화
	}
	public MyClass(int a, String n) {// 생성자 오버로딩
		age = a;
		name = n;
	}
	public MyClass(String n, int a) {// 생성자 오버로딩
		age = a;
		name = n;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
}

public class MyClassEx {

	public static void main(String[] args) {
		
		MyClass mc1 = new MyClass();
		MyClass mc2 = new MyClass("가길동");
		MyClass mc3 = new MyClass(50,"가길동");
		MyClass mc4 = new MyClass("가길동",50);
		
		
		System.out.println("이름 : "+mc1.getName()+", 나이 : "+mc1.getAge());
		System.out.println("이름 : "+mc2.getName()+", 나이 : "+mc2.getAge());
		System.out.println("이름 : "+mc3.getName()+", 나이 : "+mc3.getAge());
		System.out.println("이름 : "+mc4.getName()+", 나이 : "+mc4.getAge());
	}

}
