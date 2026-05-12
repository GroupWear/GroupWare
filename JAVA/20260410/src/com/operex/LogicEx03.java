package com.operex;

public class LogicEx03 {

	public static void main(String[] args) {
		
		boolean b = true;
		
		char ch = 'C';
		
		System.out.println("b = "+b);
		System.out.println("!b = "+!b);
		System.out.println("!!b = "+!!b);
		System.out.println("!!!b = "+!!!b);
		System.out.println();
		System.out.println("ch = "+ch);
		System.out.printf("ch < 'a' || ch > 'z' = %b%n",ch < 'a' || ch > 'z');
		System.out.printf("!('a' <= ch && ch <= 'z') = %b%n",!('a' <= ch && ch <= 'z'));
		System.out.printf("('a' <= ch && ch <= 'z') = %b%n",('a' <= ch && ch <= 'z'));
	}

}
