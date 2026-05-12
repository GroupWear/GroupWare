package com.arraythree;

/*	
 * 		3차원 배열
 * 			int[][][] arr = new int[면][행][열];
 * 
 * 			int[][][] arr = new int[2][3][4];
 * 
 * 
 * 
 * 
 */
public class ArrayEx01 {

	public static void main(String[] args) {
		
//		int[][][] arr = new int[2][3][4];
		
		int[][][] arr = {
				{
					{1,2,3,4},
					{5,6,7,8},
					{9,10,11,12}
				},
				{
					{1,2,3,4},
					{5,6,7,8},
					{9,10,11,12}
				}
		};
		
//		int v = 1;
//		
//		for(int i = 0; i < arr.length; i++) {// 면
//			for(int j = 0; j < arr[i].length; j++) {// 행
//				for(int k = 0; k < arr[i][j].length; k++) { // 열
//					arr[i][j][k] = v;
//					v++;
//				}
//			}
//		}
		
		// 출력
		
		for(int i = 0; i < arr.length; i++) {// 면
			for(int j = 0; j < arr[i].length; j++) {// 행
				for(int k = 0; k < arr[i][j].length; k++) { // 열
					System.out.printf("%d\t", arr[i][j][k]);
				}
				System.out.println();
			}
			System.out.println();
		}
		
	}

}
