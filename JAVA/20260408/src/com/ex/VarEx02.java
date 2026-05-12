package com.ex;
/* 두 변수의 값을 교환(바꾼다)
 * 
 * int x, y;
 * 		y값과 x값을 바꾸고 싶을 때?
 * 		
 * 		Z = X
 * 		X = Y
 *  	Y = Z
 * 
 * 		변수의 이름(식별자)을 정하는 규칙
 * 			만들때는 규칙을 지켜야 함.
 * 
 * 		1. 대소문자가 구분되며, 길이에 제한이 없다.
 * 		2. 예약어를 사용해서는 안된다. p. 45
 * 		3. 숫자로 시작해서는 안됨.
 * 		4. 특수문자는 _, $만 허용.
 *		
 */
public class VarEx02 {

	public static void main(String[] args) {
		int x = 7;
		int y = 1;
		int z = 0;
		
		System.out.println("x :"+x+", y: "+y);
		z = x;
		x = y;
		y = z;
		
		
//		x = x ^ y;
//		y = y ^ x;
//		x = x ^ y;
		System.out.println("x:"+x+",y: "+y);
	}

}
