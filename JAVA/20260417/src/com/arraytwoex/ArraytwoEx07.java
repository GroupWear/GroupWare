package com.arraytwoex;

/*		문]
 * 			학생수를 입력받고, 그 수만큼 점수를 입력받아
 * 			전체학생의 점수의 총점, 평균, 표준편차를 구하는
 * 			프로그램을 구현하시오.
 * 			단, 배열을 활용하여.
 * 
 *  		결과 
 *  		학생 수 : 5
 *  		1번 학생 점수 : 90
 *  		2번 학생 점수 : 82
 *  		3번 학생 점수 : 64
 *  		4번 학생 점수 : 36
 *  		5번 학생 점수 : 98
 *  
 *  		총점 : 	370
 *  		평균 : 74.0
 *  
 *  		표준 편차 : 
 *  		90 : -16
 *  		78 : -4
 */

import java.util.*;

public class ArraytwoEx07 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int n, sum = 0;
		float avg = 0.0f;
		
		
		System.out.print("학생 수 : ");
		n = sc.nextInt();
		int[] arr = new int[n];
		
		for(int i = 0; i < arr.length; i ++) {
			
			System.out.printf("%d번 학생 점수 : ",i+1);
			arr[i] = sc.nextInt();
			sum += arr[i];
			
		}
		avg = (float)sum/arr.length;
		System.out.printf("총점 : %d%n",sum);
		System.out.printf("평균 : %.1f%n",avg);
		System.out.println("표준 편차");
		
		for(int t : arr) {//arr 배열 안에 점수들이 들어있어요: [90, 82, 64, 36, 98]   ----- (int) t 에 arr의 배열을 나눠주는것
								//for(int t : arr)는 마치 배열에서 점수를 하나씩 꺼내서 t에게 주는 것과 같습니다.
								//첫 번째 반복: t = 90
								//두 번째 반복: t = 82
								//세 번째 반복: t = 64
			System.out.println(t +" : "+(avg-t));
		}
	}

}
