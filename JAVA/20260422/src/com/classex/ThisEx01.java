package com.classex;

/*		this와 this()의 의미
 * 			
 * 		- this란 특정 객체 내에서 자신이 생성되었을때의 주소 값 변수를 의미한다.
 * 		- 객체의 주소는 객체가 생성되기 전 까지는 아무도 모르기 때문에 
 * 		객체 생성 후에 자신의 주소로 대치됨.
 * 		
 * 
 * 		- this()는 현재 객체의 생성자를 의미함.
 * 		- 생성자 안에서 다른 생성자를 호출할 경우 this()라고 하여 호출함. 
 */

class ThisEx {
	
	private String name;
	private String jumin;
	private String tel;
	
	public ThisEx() {// 기본생성자
			this.name = "홍길동";
			this.jumin = "000000-0000000";
			this.tel = "010-1111-1111";
	}
	public ThisEx(String name) {// 생성자의 오버로딩
		// 생성자에서 다른 생성자를 호출할 경우
		// 첫번째 라인에 기입을 함
		this();
		this.name = name;
	}
	public ThisEx(String name, String jumin) {
		this(name);// 다른 생성자를 호출 -> 매개변수가 하나인 생성자를 호출
		this.jumin = jumin;
	}
	public ThisEx(String name ,String jumin, String tel) {
		this(name,jumin);
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public String getJumin() {
		return jumin;
	}
	public String getTel() {
		return tel;
	}
	
}


public class ThisEx01 {

	public static void main(String[] args) {
		
		ThisEx ex1 = new ThisEx();
		ThisEx ex2 = new ThisEx("가길동");
		ThisEx ex3 = new ThisEx("가길동","451313-1897944");
		ThisEx ex4 = new ThisEx("가길동","451313-1897944","010-5454-9797");
		
		
		System.out.println("이름 : "+ex1.getName()+" 주민번호 : "+ex1.getJumin()+" 전화번호 : "+ex1.getTel());
		System.out.println("이름 : "+ex2.getName()+" 주민번호 : "+ex2.getJumin()+" 전화번호 : "+ex2.getTel());
		System.out.println("이름 : "+ex3.getName()+" 주민번호 : "+ex3.getJumin()+" 전화번호 : "+ex3.getTel());
		System.out.println("이름 : "+ex4.getName()+" 주민번호 : "+ex4.getJumin()+" 전화번호 : "+ex4.getTel());
		
		
		
		
	}

}
