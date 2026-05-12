package com.example;
/*
 * 	문]
 * 		숫자 하나를 입력받아 그 수가 1이면'남성', 2이면 '여성'이라는 글자를 출력하는 프로그램을 구현하시오.
 * 		단, 입력은 IO를 활용하시오.
 * 			남성과 여성 판별시 조건삼항 연산자를 활용하시오.
 */
import java.io.*;
public class Exam03 {
	public static void main(String[] args) throws IOException {
		
		
		
		
		System.out.print("남성이면 '1', 여성이면 '2' :");
		int n1 = System.in.read() - 48;
		
//		if(n1 == 1) {
//			System.out.println("남성");
//		}else {
//			System.out.println("여성");
//		}
// 위 if문을 조건삼항으로 바꾸면 n1 == 1 ? "남성" : "여성"; 이 된다. 
// 이게 조건삼항이니까 조건삼항을 출력해야하니 sysout을 써주고 갖다붙이면 된당.
		String s = n1== 1 ? "남성" : "여성";
		System.out.println(s);
		
		 System.out.println(n1 == 1 ? "남성" : "여성"); 
		
	}
}
