package com.ioex;

/* ASCII code 입력 받기.
 * 
 * int i = System.in.read();
 * 기본적으로 알아야할 아스키 코드값.
 * 
 * 1. 숫자 : 0~9 : 48~57
 * 2. 대문자 : A~Z : 65~90
 * 3. 소문자 : a~z : 97~122
 * 
 */

import java.io.*;
public class InputEx01 {

	public static void main(String[] args) throws IOException {
		
		System.out.print("문자 입력 : ");
		int i = System.in.read();
		System.out.println("ASCII code 값 : "+i);
		
	}

}
