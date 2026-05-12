package com.Ex;

public class OperEx06 {

	public static void main(String[] args) {
		
		char c = 'a';
		for(int i = 0; i<26;i++) {
			System.out.printf("%c ",c++);
		}
		System.out.println();
		c='A';
		for(int i = 0; i<26;i++) {
			System.out.printf("%c ",c++);
		}
		
		System.out.println();
		c='0';
		for(int i = 0; i<10;i++) {
			System.out.printf("%c",c++);
		}
		
		System.out.printf("----------------------");
		
		//소문자를 대문자로 변환
		char lower ='c';
		char upper = (char)(lower - 32);
			System.out.println(upper);
		System.out.println("----------------------");
		int x = 10;
		int y = 8;
		
		System.out.printf(" %d를 %d로 나누면 %n", x, y);
		System.out.printf("몫은 %d이고, 나머지는 %d 입니다.",x/y, x%y);
	}

}
