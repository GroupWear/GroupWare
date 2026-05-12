package com.castingex;

/*	업캐스팅(upcasting)
 * 		- 서브클래스의 객체에 참조변수를 슈퍼 클래스의 타입으로 변환하는것을 의미함 
 * 		   업 캐스팅은 부모클래스의 참조변수로 서브클래스의 객체를 가르킨다.
 */

public class UpcastingEx {
	
	public static void main(String[] args) {
		Person p; // 참조변수
//		person p = new Student("나길동");
		Student s = new Student("가길동");//객체
		
		p = s; // 업캐스팅
		
		System.out.println(p.name);
		
//		p.grade = "A";
//		p.department = "C";
//		p 객체를 통해서는 name, id만이 접근가능
		
	}

}
