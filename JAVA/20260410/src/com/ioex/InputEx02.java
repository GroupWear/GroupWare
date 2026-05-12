package com.ioex;

import java.io.*;

public class InputEx02 {

	public static void main(String[] args) throws IOException {
		
		System.out.print("문자 입력 : ");
		char ch = (char)System.in.read();
		System.out.println("입력 받은 문자 : "+ch);
		System.out.println("입력 받은 아키스코드 : "+(int)ch);
		
	}

}
