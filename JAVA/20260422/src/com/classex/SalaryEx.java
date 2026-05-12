package com.classex;

/*		접근 제한자 (접근 제어 변경자)
 * 			-	멤버(필드, 메소드)들은 객체 자신들만의 속성이자 특징이므로 대외적으로 공개되는 것이
 * 				결코 좋은 것은 아니다. 그런이유로 프로그래머가 객체의 멤버들에게 접근 제한을 
 * 				걸 수 있는 데 이를 접근 제한자라고 함.
 * 
 * 			접근 제한자 종류
 * 			public			: 모든 접근을 허용함 
 * 			protected		: 같은 패키지(폴더)에 있는 객체와 상속관계의 객체들만 접근을 허용
 * 			default			: 같은 패키지 있는 객체들만 허용
 * 			private			: 현재 객체 내에서만 허용
 * 
 */

class Salary {
	
	private int pay;
	//	public int pay;

	public int getPay() {
		return pay;
	}

	public void setPay(int pay, String pass) {
		if(pass.equals("비밀번호"));
		this.pay = pay;
	}
}


public class SalaryEx {

	public static void main(String[] args) {
		
		Salary sa = new Salary();
		sa.setPay(10000,"비밀번호");
		System.out.println(sa.getPay());
		
		
	}

}
