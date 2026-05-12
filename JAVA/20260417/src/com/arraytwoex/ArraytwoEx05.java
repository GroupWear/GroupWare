package com.arraytwoex;

/*		문]	
 * 			아래와 같이 출력되도록 프로그래밍하시오.
 * 
 * 			결과 
 * 			1 2 3 4 5 
 * 			2 3 4 5 6
 * 			3 4 5 6 7
 * 			4 5 6 7 8 
 * 			5 6 7 8 9
 * 
 */
public class ArraytwoEx05 {

	public static void main(String[] args) {
		
		int[][] arr = new int[5][5];
		
		// 데이터를 반복문을 이용하여 초기화함.
		for(int i =0, n = 1; i < arr.length;i++) {
			
			for(int j = 0; j < arr[i].length; j++) {
				arr[i][j] = n+j;
				System.out.printf("%2d\t",arr[i][j]);
				}
			n++;
			System.out.println();
			}
		
		//배열에 저장된 데이터 출력
//		for(int i =0; i < arr.length;i++) {
//			
//			for(int j = 0; j < arr[i].length; j++) {
//				arr[i][j] = n;
//				n++;
//				System.out.printf("%2d\t",arr[i][j]);
//				}
//			}
//			System.out.println();
//		}
		
		
		
	}
}


