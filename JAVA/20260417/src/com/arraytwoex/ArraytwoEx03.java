package com.arraytwoex;

/*		문]
 * 			2차원 배열을 활용하여 아래와 같이 출력되도록 프로그래밍 하시오.
 * 
 * 				1	2	3	4	5
 * 				6	7	8	9	10
 * 				11 12 13 14 15
 * 				16 17 18 19 20
 * 				21 22 23 24 25
 * 
 */
public class ArraytwoEx03 {

	public static void main(String[] args) {
		
		int[][] arr = new int[5][5];
		
		int n = 1;
		// 배열을 초기화시킴.
		for(int i = 0; i <arr.length;i++) {
			
			for(int j=0; j < arr[i].length;j++) {
				arr[i][j]=n;
				n++;
				System.out.printf("%02d\t",arr[i][j]);
			}
			
			System.out.println();
		}
		
		
		
//		for(int i = 0; i <arr.length;i++) {
//			
//			for(int j=0; j < arr[i].length;j++) {
//				System.out.printf("%d\t",arr[i][j]);
//			}
//			System.out.println();
		}
	}


