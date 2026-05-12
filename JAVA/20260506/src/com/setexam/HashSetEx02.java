package com.setexam;

import java.util.*;

class Person {
	String name;
	int age;
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	@Override
	public String toString() {
		return name+" : "+age;
	}
	
	
}
public class HashSetEx02 {

	public static void main(String[] args) {

		HashSet set = new HashSet<>();
		set.add("abc");
		set.add("abc");// 같음
		set.add(new Person("David", 10));
		set.add(new Person("David", 10)); // new라는 연산자는 새로만드는 것이기때문에 메모리자체가 다름 그래서 다른객체로 분류됨
		//객체를 두개 생성했기 떄문에 다른 객체로 인식하여 중복처리안함
		System.out.println(set);
		
	}
}
