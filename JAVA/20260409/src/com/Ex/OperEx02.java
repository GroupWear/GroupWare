package com.Ex;

// 산술 연산자 : +, -, *, /, %

public class OperEx02 {

	public static void main(String[] args) {
		
		int a=10;
		int b=4;
		
		System.out.printf("%d + %d = %02d%n",a,b,a+b);
		System.out.printf("%d - %d = %02d%n",a,b,a-b);
		System.out.printf("%d * %d = %02d%n",a,b,a*b);
		System.out.printf("%d / %d = %02d%n",a,b,a/b);
		System.out.printf("%d %% %d = %02d%n",a,b,a%b); // 나머지를 넣고 싶으면 %%를 겹치게 써줘야한다.
		
	}

}
