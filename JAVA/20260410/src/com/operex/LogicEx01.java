package com.operex;

/*
 * 	&& (and 연산자 : 논리 곱) => 두 개의 입력 중 둘다 참이면 참.
 * ||	 (or 연산자 : 논리합) => 두 개의 입력 중 하나의 입력이 참이면 모두 참. 
 */

public class LogicEx01 {

	public static void main(String[] args) {
		
		int x = 0;
		char ch = ' ';
		
		x = 15;
		System.out.printf("x=%2d, 10 < x && x < 20 = %b%n", x, 10 < x && x < 20);
		 
		x = 6;
		System.out.printf("x=%2d, x %%2 == 0|| x %% 3 ==0 && x %% 6 == 0 =%b%n",
				x, x%2==0 || x %3==0 && x % 6 !=0);
		
		System.out.printf("x=%2d, (x %%2 == 0|| x %% 3 ==0) && x %% 6 == 0 =%b%n",
				x, (x%2==0 || x %3==0) && x % 6 !=0);
		
		ch = '1'; // 49
		System.out.printf("ch= '%c', '0' <= ch && ch <= '9' =%b%n", 
				ch, '0' <= ch && ch <= '9');
		
		ch = 'a';
		System.out.printf("ch= '%c', 'a' <= ch && ch <= 'z' =%b%n", 
				ch, 'a' <= ch && ch <= 'z');
		
		ch = 'A';
		System.out.printf("ch= '%c', 'A' <= ch && ch <= 'Z' =%b%n", 
				ch, 'A' <= ch && ch <= 'Z');
		
		ch = 'q';
		System.out.printf(" ch = '%c', ch == 'q' || ch == 'Q' = %b%n", 
				ch, ch == 'q' || ch == 'Q');
	}

}
//서식,의미,예시 (값=7),출력 예시
//%d,그냥 정수,%d,7
//%2d,"최소 2자리, 왼쪽 공백 채움",%2d,""" 7"""
//%02d,"최소 2자리, 왼쪽 0으로 채움",%02d,"""07"""
//%5d,"최소 5자리, 왼쪽 공백",%5d,"""    7"""
//%-5d,"최소 5자리, 왼쪽 정렬",%-5d,"""7    """