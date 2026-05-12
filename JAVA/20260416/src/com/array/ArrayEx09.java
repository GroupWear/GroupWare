package com.array;

/*		문]
 * 			배열의 값의 순서를 반복적으로 바꾸어서 값을 생성
 * 
 * 			배열의 길이가 10인 값을 랜덤으로 생성해서 
 * 			값의 순서를 바꿔보는 프로그램을 구현하시오.
 * 			100번을 반복해서 순서를 바꾼다.
 * 			
 * 			
 * 			
 */

public class ArrayEx09 {

	public static void main(String[] args) {
		
		int[] arr = new int[10];
		
		for(int i = 0; i < arr.length;i++) {
			arr[i] = i;
			System.out.print(arr[i]+" ");
		}
		System.out.println();
		
		for(int i =0; i < 100;i++) {
			
			int n = (int)(Math.random() * 10);//0~9랜덤.
			
			int temp = arr[0];
			arr[0] = arr[n];
			arr[n] = temp;
		}
		System.out.println("100번 섞은 후 배열:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
	}

}
