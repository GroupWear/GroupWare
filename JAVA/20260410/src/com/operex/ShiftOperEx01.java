package com.operex;

public class ShiftOperEx01 {
	
	static String toBinaryString(int x) {
		String zero="00000000000000000000000000000000";
		String temp = zero + Integer.toBinaryString(x);
		return temp.substring(temp.length()-32);
	}
	
	public static void main(String[] args) {
		
		int d = 8;
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 0, d >> 0, toBinaryString(d >> 0));
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 1, d >> 1, toBinaryString(d >> 1));
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 2, d >> 2, toBinaryString(d >> 2));
		System.out.println();
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 0, d << 0, toBinaryString(d << 0));
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 1, d << 1, toBinaryString(d << 1));
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 2, d << 2, toBinaryString(d << 2));
		
		d = -8;
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 0, d >> 0, toBinaryString(d >> 0));
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 1, d >> 1, toBinaryString(d >> 1));
		System.out.printf("%d >> %d = %4d \t%s%n", 
				d, 2, d >> 2, toBinaryString(d >> 2));
		System.out.println();
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 0, d << 0, toBinaryString(d << 0));
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 1, d << 1, toBinaryString(d << 1));
		System.out.printf("%d << %d = %4d \t%s%n",
				d, 2, d << 2, toBinaryString(d << 2));
		System.out.println();
		
		
	}

}
