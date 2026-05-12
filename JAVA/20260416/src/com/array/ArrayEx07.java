package com.array;

/*		문]
 * 			배열에 저장된 값에 총점과 평균을 구하는 프로그램을 구현하시오.
 * 	
 * 			100, 88, 100, 100, 90
 * 
 */
public class ArrayEx07 {

	public static void main(String[] args) {
		
		int[] score = {100, 88, 100, 100, 90};
		int sum = 0;
		float avg = 0.0f;
		
		
		for(int i = 0; i < score.length;i++){
			sum += score[i];
		}
		avg = (float)sum/score.length;
		System.out.printf("총 점수는 : %d%n",sum);
		System.out.printf("총 점수의 평균은 : %.1f",(float)avg);
	}

}
