package com.operex;

/*		비트 연산자 (0,1)
 * 		- &, |, ^, ~, <<, >>
 * 
 * 		& : 입력이 모두 1이어야만 1을 결과로 얻는다. 그외 나머지는 0이다.
 *		|  : 입력이 하나만 1이어도 1을 결과로 얻는다. 그외 나머지는 0이다.
 *		^ : 입력 값이 서로 다르면 1을, 서로 같으면 0을 결과도 얻는다.
 * 
 */

public class BitOperEx01 {

	public static void main(String[] args) {
		
		int x=0xAB, y=0xF;
		
		System.out.printf("x = %#X \t\t\t\t\t%s%n", x, toBinaryString(x));
		System.out.printf("y = %#X \t\t\t\t\t%s%n", y, toBinaryString(y));
		
		
		System.out.printf("%#X | %#X = %#X \t\t\t%s%n",
				x,y,x|y,toBinaryString(x|y));
		
		System.out.printf("%#X & %#X = %#X \t\t\t%s%n",
				x,y,x&y,toBinaryString(x&y));
		
		System.out.printf("%#X ^ %#X = %#X \t\t\t%s%n",
				x,y,x^y,toBinaryString(x^y));
		System.out.printf("%#X ^ %#X ^ %#X = %#X \t%s%n",
				x,y,y,x^y^y,toBinaryString(x^y^y));
}

// 10진수를 2진수로 변환하는 메소드 정의
static String toBinaryString(int x) {
	String zero="00000000000000000000000000000000";
	String temp = zero + Integer.toBinaryString(x);
	return temp.substring(temp.length()-32);
}
}
/* length 와 lenght()의 차이점
 * 
 * length : 정수형 (배열의 길이)
 * length() : 문자열의 길이
 */

