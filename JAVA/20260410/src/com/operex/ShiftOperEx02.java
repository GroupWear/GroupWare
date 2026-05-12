package com.operex;

public class ShiftOperEx02 {

	public static void main(String[] args) {
		
		int d = 1234;
		int h = 0xABCD;
		int m = 0xF;
		
		System.out.printf("hex=%X%n",h);
		System.out.printf("%X%n",h & m);
		// ABCD와 F를 10진수로 변환해서 &로 계산
		
		h = h >> 4;
		System.out.printf("%X%n",h & m);
		
		h = h >> 4;
		System.out.printf("%X%n",h & m);
		
		h = h >> 4;
		System.out.printf("%X%n",h & m);
		
	}

}
