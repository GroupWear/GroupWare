package com.ifexam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/*
 * 		문]
 * 			사용자로부터 임의의 정수 세개를 입력받아,
 * 			작은 수부터 큰수를 순서대로 출력하는 프로그램을 작성하시오.
 * 			오름차순 정렬(작은수 -> 중간수 -> 큰수)
 * 
 * 			첫번째 정수 입력 : 16
 * 			두번째 정수 입력 : 8
 *			세번째 정수 입력 : 21
 *  	
 *  		정렬결과 : 8 16 21
 *  
 */
import java.util.*;
public class Ifex10three {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		
		int a, b, c, temp;
		
		System.out.print("첫번째 정수 입력 : ");
		a = Integer.parseInt(br.readLine());
		
		System.out.print("두번째 정수 입력 : ");
		b = Integer.parseInt(br.readLine());
		
		System.out.print("세번째 정수 입력 : ");
		c = Integer.parseInt(br.readLine());
		
		
		//1. 첫번째 정수가 두번째 정수보다 큰 경우 자리를 바꾼다.
		if(a > b) {// temp = a, a=b, b= temp니까 a가 b고 b가 a로 바뀜
			temp = a;
			a = b;
			b = temp;
		}
		//2. 첫번재 정수가 세번째 정수보다 크다면 자리를 바꾼다.
		if(a > c) {
			temp = a;
			a = c;
			c = temp;
		}
		//3. 두번째 정수가 세번째 정수보다 큰 경우 자리를 바꾼다.
		if(b > c) {
			temp = b;
			b = c;
			c = temp;
		}
		
		
		System.out.println("출력 결과 : "+a+" -> "+b+" -> "+c);
		sc.close();
		}}