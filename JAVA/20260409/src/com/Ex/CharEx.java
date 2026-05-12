package com.Ex;

/*	 char : 2byte
 * 		       char ch = '\u0041'; --> unicode 방식
 *				      ch = 65, ch= 'A', ch='/n'
 *
 *			아스키 코드 : 1byte 문자를 표현하는 방식, 0~255 까지의 문자 범위.
 * 			Unicode : 2byte 문자를 표현하는 방식, 0~65535까지의 문자 범위.
 * 
 * 			세계 여러 국가의 문자들(한자나 한글같은 비 영어권 문자는 2byte 까지 표현)
 * 			1byte로 표현하기 부족하기 때문에 1byte를 추가로 할당해서 표현하는 방식.
 * 			https://velog.io/@pearpearb/TIL-40-%EC%BB%B4%ED%93%A8%ED%84%B0-%EA%B3%B5%ED%95%99
 */

public class CharEx {
	
	public static void main(String[]args) {	
		
		char ch1='A';
		char ch2='\u0041';
		
		System.out.println(ch1);
		System.out.println(ch2);
		System.out.println("ch1 + ch2 = "+ ch1+ch2); 
		
		
		// 계산이 가능한 경우 숫자(아스키 코드 십진수)로 변경하여 계산한다.
		System.out.println("ch1 + ch2 = "+ (ch1+ch2)); 
		System.out.println("ch1 + ch2 = "+ (char)(ch1+ch2));
	}
}
