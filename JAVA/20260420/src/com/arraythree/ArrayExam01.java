package com.arraythree;

/* 문] 
 * 		양의 정수 10개를 입력받아 배열에 저장하고, 배열에 있는 정수중
 * 		3의 배수만 출력하시오.
 * 
 * 		양의 정수 10 : 1 5 99 22 34 154 2346 55 32 85
 * 		3의 배수 : 99 345 2346
 */

import java.util.*;

public class ArrayExam01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int[] arr = new int[10];
		
		for(int i = 0; i < arr.length; i++) {
			System.out.printf("%d번째 정수 입력 : ",i+1);
			arr[i] = sc.nextInt();
		}
		for(int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]+" ");
		}
		System.out.println();
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] % 3== 0) {
				System.out.println("3의배수는 : "+arr[i]+" ");
			}
		}
		
		
		
		
//		System.out.print("양의 정수 : ");
//		int num = sc.nextInt();
//		int[] n = new int[num];
//		
//		for(int i =0; i < n.length; i++) {
//			
//			if(n[i] % 3==0) {
//				
//			}
//			System.out.printf("3의 배수 : %d", n[i]);
//		}
//		
		
		
		
		
		
		
}
	
}
