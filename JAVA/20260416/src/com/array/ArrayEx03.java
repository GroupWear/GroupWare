package com.array;

/*		문]
 * 			length를 이용하여 배열의 크기만큼 정수를 입력받고 평균을 구하는 
 * 			프로그램을 구현하시오.
 * 
 * 			배열의 길이 : 배열명.length
 */

import java.util.*;

public class ArrayEx03 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		//배열 생성
		
		int[] arr = new int[5];
		int sum = 0;
		
		System.out.print(arr.length+"개의 정수를 입력하세요. >>");
		
		for(int i = 0; i <arr.length;i++)
			arr[i] = sc.nextInt();
			
		for(int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		System.out.print("평균은 "+(double)sum/arr.length+"입니다.");
		
		
	}
}