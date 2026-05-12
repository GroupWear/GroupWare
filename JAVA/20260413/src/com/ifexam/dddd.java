package com.ifexam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
public class dddd {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		char str1;
		int n1, n2;
		
		System.out.print("첫번째 정수 입력 : ");
		n1 = sc.nextInt();
		//n1 = Integer.parseInt(br.readLine());
		
		System.out.print("두번째 정수 입력 : ");
		n2 = sc.nextInt();
		//n2 = Integer.parseInt(br.readLine());
		
		System.out.print("연산자 입력 [+,-,*,/] : ");
		str1 = sc.next().charAt(0);
		//op = (char)System.in.read();
		
		int n3, n4, n5, n6;
		n3 = (n1+n2);
		n4 = (n1-n2);
		n5 = (n1*n2);
		n6 = (n1/n2);
		
		if(str1 == '+') {// +
			System.out.printf(n1+str1+"+"+n2+" = %d",n3);
		}else 
			if(str1 == '-') {// -
				System.out.printf(n1+str1+"-"+n2+" = %d",n4);
		}else 
			if(str1 == '*') {// *
				System.out.printf(n1+str1+"*"+n2+" = %d",n5);
		}else 
			if(str1 == '/') {// /
				System.out.printf(n1+str1+"/"+n2+" = %d",n6);
		}else System.out.println("오류");
		sc.close();
	}
	
	
}
