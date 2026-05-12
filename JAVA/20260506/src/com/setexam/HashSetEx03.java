package com.setexam;
import java.util.*;
class Person2 {
	String name;
	int age;
	
	public Person2(String name, int age) {
		this.name	= name;
		this.age = age;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person2) {
			Person2 tmp = (Person2)obj;
			return name.equals(tmp.name) && age == tmp.age; 
		}
		return false;
	}
	/*
	Object
  ├── String
  ├── Integer
  ├── Person2
  ├── HashSet
  └── ... 모든 클래스
  
명시적으로 상속 안 해도 Java가 자동으로 상속시킵니다.

class Person2 { }
// 사실 이거랑 같음
class Person2 extends Object { }

equals()는 어떤 타입이든 비교할 수 있어야 
하기 때문에 매개변수를 Object로 받습니다. 
모든 클래스의 부모니까 뭐든 받을 수 있습니다.

	 */
	@Override
	public int hashCode() {
		return (name+age).hashCode();
	}
	
	@Override
	public String toString() {
		return name+" : "+age;
	}
}
public class HashSetEx03 {

	public static void main(String[] args) {
		
		HashSet set = new HashSet<>();
		set.add("abc");
		set.add("abc");
		set.add(new Person2("David", 10));
		set.add(new Person2("David", 10));
		System.out.println(set);
		
		
		
		
	}

}
