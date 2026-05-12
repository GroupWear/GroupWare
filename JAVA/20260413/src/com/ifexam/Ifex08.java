package com.ifexam;

/*		문]
 * 			사용자로부터 임의의 알파벳 한문자를 입력받아 
 * 			대문자가 입력 받았을 경우, 소문자로 변환하고
 * 			소문자가 입력 받았을 경우, 대문자로 변환하는 프로그램을 구현하시오.
 * 			단, 알파벳이 아닌경우는 "입력 오류"를 출력하시오.
 */
import java.io.*;

public class Ifex08 {

	public static void main(String[] args) throws IOException {
		
		char ch;
		int n1;
		System.out.print("문자 입력 : ");
		ch = (char)System.in.read();
		
		
		if('A' <= ch && ch <= 'Z') {
			ch = (char)(ch + 32);
			System.out.println(ch);
		}else 
		if('a' <= ch && ch <= 'z') {
			ch = (char)(ch - 32);
			System.out.println(ch);
		}else
			System.out.println("입력 오류");
			
	}
		
	}
	
