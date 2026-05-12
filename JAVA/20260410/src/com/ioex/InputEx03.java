package com.ioex;
// 숫자 하나 입력 : System.in.read() - 48
// 					   System.in.read() - "0"
import java.io.*;

public class InputEx03 {

	public static void main(String[] args) throws IOException {
		
		int n1;
		int n2;
		
		System.out.print("첫번째 숫자 입력 :");
		n1 = System.in.read() - 48; // 0의 아스키코드값이 48이기때문에 48을빼줘야함.
		//System.in.read();
		//System.in.read();
		System.in.skip(2);
		
		System.out.print("두번째 숫자 입력 :");
		n2 = System.in.read() - '0'; // 위와 동일 
		System.in.read();
		System.in.read();
		int n3 = n1 + n2;
		System.out.printf("첫번째 수와 두번째수의 합 = %d",n3);
	}

}
