package com.Ex;

public class OperEx05 {

	public static void main(String[] args) {
		
		char c1 = 'a';
		char c2 = c1;
		char c3 = ' '; // 공백으로 초기화함.
		
		int i = c1 + 1; // 'a' + 1 = 97 + 1= 98
		
		c3 = (char)(c1+1);//b
		
		c2++; // b
		c2++; // c
		
		System.out.println(" i ="+i);
		System.out.println(" c2 ="+c2);
		System.out.println(" c3 ="+c3);
		System.out.println("--------------------");
		
		char e1 = 'a';
		char e2 = (char)(e1 + 1);
		// 에러의 원인
		// 문자도 연산을 해서 정수형태로 바뀐다.
		// 하지만 대입하고자 변수는 문자형이므로 정수형을 문자형으로 형변환 해야만 문제를 해결할 수 있다.
		
		
	}

}
