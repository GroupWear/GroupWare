package com.forExam;

/*
 * 	1~10까지의 합계
 * 
 */
public class ForEx02 {

	public static void main(String[] args) {
		// 합계를 누적시킬 누적변수를 선언.
		int sum = 0;
		
		for(int i=1, j=10; i<=10; i++,j--) {
			// 반복문이 수행될때마다 변수값을 누적변수에 더하여 계산한다.
			sum += i;// sum = sum + i;
			System.out.printf("%d\t %d%n",i,j);
			
		}
		
		
	}

}
