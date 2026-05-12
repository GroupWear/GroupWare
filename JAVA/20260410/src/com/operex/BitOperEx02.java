package com.operex;
// 비트 부정 연산(~) : 0은 1로, 1은 0으로 바꾼다.
//					   논리 부정 연산의 !와 같다.

public class BitOperEx02 {

	// 10진수를 2진수로 변환하는 메소드 정의
	static String toBinaryString(int x) {
		String zero="00000000000000000000000000000000";
		String temp = zero + Integer.toBinaryString(x);
		return temp.substring(temp.length()-32);
	}
	
	public static void main(String[] args) {
		
		byte p = 10;
		byte n = -10;
		
		System.out.printf("p = %d \t\t%s%n", p, toBinaryString(p));
		System.out.printf("~p = %d \t\t%s%n", ~p, toBinaryString(~p));
		System.out.printf("~p+1 = %d \t%s%n", ~p+1, toBinaryString(~p+1));
		System.out.printf("~~p = %d \t\t%s%n", ~~p, toBinaryString(~~p));
		System.out.println();
		System.out.printf("n = %d%n", n);
		System.out.printf("~(n-1) = %d%n", ~(n-1));
		
	}

}

