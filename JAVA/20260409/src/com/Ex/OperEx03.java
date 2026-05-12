package com.Ex;

public class OperEx03 {

	public static void main(String[] args) {
		
		byte a=10;
		byte b=20;
		
		byte c=(byte)(a+b);
		// byte + byte = int
		// 대입할 공간의 자료형이 byte이므로 연산결과가 int 형이므로 저장이 불가능 함.
		// 이를 저장하기 위해서는 큰 자료형을 작은 자료형으로 변환하여, 저장하게 되면
		// 에러가 발생하지 않는다.
		
		System.out.printf("%d + %d = %d%n",a,b,c);
	}

}
