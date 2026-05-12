package com.arraytwoex;

/*		문]	
 * 			2차원 배열을 학년별 1, 2학기 성적을 저장하고,
 * 			4년간의 전체 평점 평균을 구하는 프로그램을 구현하시오.
 * 
 * 
 */

public class ArraytwoEx08 {

	public static void main(String[] args) {	
		
		// 평점을 저장할 배열을 선언
		double sum = 0.0f;
		double jumsu[][] = {
				{3.3,3.4},
				{3.5,4.0},
				{3.8,3.7},
				{3.9,3.8}
		};
		for(int i = 0; i < jumsu.length;i++) {//행
			System.out.printf("%d학년 성적 : ",i+1);
			for(int j =0; j < jumsu[i].length;j++) {//열
				sum += jumsu[i][j];
				System.out.printf("%.1f\t",jumsu[i][j]);
			}
			System.out.println();
		}
		
		int n = jumsu.length;
		int m = jumsu[0].length;
		
		System.out.println("4년 전체 평점 평균 : "+sum /(n*m));
		
	}

}
