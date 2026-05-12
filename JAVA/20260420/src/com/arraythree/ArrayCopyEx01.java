package com.arraythree;

// for문을 이용한 배열복사

public class ArrayCopyEx01 {

	public static void main(String[] args) {
		
		int[] arr = new int[5];
		
		for(int i = 0; i < arr.length;i++) {
			arr[i] = i+1;
		}
		System.out.println("배열의 변경 전");
		System.out.println("배열의 길이 : "+arr.length);
		for(int i = 0; i < arr.length;i++) {
			System.out.print(arr[i]+"\t");
		}
		System.out.println();
		System.out.println("배열의 변경 후");
		
		int tmp[] = new int[arr.length*2];
		System.out.println("배열의 길이 : "+tmp.length);
		// tmp에 arr배열에 저장된 값을 저장함.
		// 배열에서 배열로 값을 저장할때는 복사
		for(int i = 0; i < arr.length;i++) {
			tmp[i] = arr[i];
		}
		
		arr=tmp;
		
		// tmp에 저장된 값을 arr에 저장함
		// 배열의 길이도 값이 복사되어 배열에 길이를 변경함.
		
		
		for(int i = 0; i < arr.length;i++) {
			System.out.print(arr[i]+"\t");
		}
		System.out.println();
		for(int i = 0; i < arr.length;i++) {
			System.out.print(tmp[i]+"\t");
		}
		
	}

}
