package com.collection;

import java.util.*;

class MyVector extends Vector {
	
	public MyVector()	{
		super(1, 1);
	}
	
	public void addInt(int i) {
		addElement(i);
	}
	
	public void addFloat(float f) {
		addElement(f);
	}
	
	public void addString(String s) {
		addElement(s);
	}
	
	public void addCharArray(char[] a) {
		addElement(a);
	}
	
	public void write()	{
		Object o;
		int length = size();
		System.out.println("벡터 요수 개수 : "+length+", 그리고 요소는 ...");
		for(int i = 0; i < length;i++) {
			o = elementAt(i);
			
			if(o instanceof char[]) {
				System.out.println("문자 배열 :"+String.copyValueOf((char[] )o));
			}else if(o instanceof String){
				System.out.println("문자열 : "+o.toString());
			}else if(o instanceof Integer) {
				System.out.println("정수형 : "+o);
			}else {
				System.out.println("실수형 : "+o);
			}
		}
	}
}

public class VectorEx02 {

	public static void main(String[] args) {
		
		MyVector v = new MyVector();
		int digit = 5;
		float real = 3.14f;
		char[] letters = {'J','A','V','A'};
		
		String s = new String("사랑합니다.");
		
		v.addInt(digit);
		v.addFloat(real);
		v.addString(s);
		v.addCharArray(letters);
		v.write();
	}
	
}
