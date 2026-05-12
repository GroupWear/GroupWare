package com.ifexam;
/*
 *		if문의 else블럭을 추가
 *		else - > 그 밖의 다른 (그 외) 
 *			조건의 결과가 참이 아닌경우 즉, 조건이 거짓일때.
 *			else 문장을 수행하라.
 */
import java.util.*;
public class Ifex04 {
	public static void main(String[] args) {
		
		System.out.print("임의의 정수 : ");
		
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		
		if(a == 0) {
		System.out.println("0입니다.");// 조건이 참일때 수행하는 문장
		}else 
			System.out.println("0이 아닙니다.");// 조건이 거짓일때 수행하는 문장 
		
		sc.close();
	}
}
