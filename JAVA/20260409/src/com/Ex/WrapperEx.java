package com.Ex;

/* 	Wrapper Class : 기본 데이터 자료형을 객체(Class)화
 * 		기본자료형 ------> 객체화시킨 자료형
 * 		byte	 	------> Byte
 * 		short	    ------> Short
 * 		int        ------> Integer
 * 		long 		------> Long
 * 		float		------>	Float
 * 		double	------>	Double
 * 		boolean------> Boolen
 * 		char		------> Character
 */
public class WrapperEx {

	public static void main(String[] args) {
		
		byte a_min = Byte.MIN_VALUE;
		byte a_max = Byte.MAX_VALUE;

		char b_min = Character.MIN_VALUE;
		char b_max = Character.MAX_VALUE;
		
		float f_min = Float.MIN_VALUE;
		float f_max = Float.MAX_VALUE;
		
		System.out.println("byte +"+a_min+" ~ "+a_max);
		System.out.println("char +"+(int)b_min+" ~ "+(int)b_max);// (int)를 붙여줌으로서 정수값으로 바꾸고 값이 나오게 됨.
		System.out.println("float +"+f_min+" ~ "+f_max);
		
		
	}

}
