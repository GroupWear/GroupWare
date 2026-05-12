package com.collection;
import java.util.*;
class Mydata{
	public String name;
	public int age;
	
	public Mydata()	{
		name="";
		age= 0;
	}
	public Mydata(String name, int age) {
		this.name=name;
		this.age = age;
	}
}

public class VectorEx04 {
	
	public static void main(String[] args) {
		Vector<Mydata> v = new Vector<Mydata>();
		v.add(new Mydata("가길동", 23));
		v.add(new Mydata("나길동", 17));
		v.add(new Mydata("다길동", 20));
		
		for(Mydata d : v)
			System.out.printf("이름 : %s, 나이 : %d%n",d.name,d.age);
		
	}
}
