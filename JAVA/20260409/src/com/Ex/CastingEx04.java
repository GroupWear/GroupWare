package com.Ex;
// 실수형을 정수형으로 변환
public class CastingEx04 {

	public static void main(String[] args) {
		int i = 91234567;
		float f = (float)i;// int를 float으로 변환
		int i2= (int)f;
		double d = (double)i2;
		int i3 = (int)d;
		
		float f2 = 1.666f;
		int i4 = (int)f2;
		
		System.out.printf("i = %d%n",i);
		System.out.printf("f = %f%n",f);
		System.out.printf("i2 = %d%n",i2);		
		System.out.printf("i3 = %d%n",i3);
		System.out.printf("d = %f%n",d);
		System.out.printf("d = %f, i3 = %d%n",d,i3);
		System.out.printf("f2 = %f, i4 = %d%n",f2,i4);
	}
	// %f : 실수값을 포맷팅 0.111111 (소수점)
	// %d, %x, %o : 정수값을 포맷팅 0, 2 , -1, 1235 (10진수, 16진수, 8진수)
	// %c : 'a', 'b' (문자, 반드시 한글자, ('')가 묶여있어야함.)
	// %s : "안녕" (한글자이상에 ("")묶여있어야함.)
}
