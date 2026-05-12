package com.arraytwoex;

/*		가변 배열, 레기드 배열, 비정형 배열
 * 		2차원이상의 배열을 생성할 때 행의 길이는 고정이지만 열의 길이는 다시 조절가능하다.
 * 		고정 형태가 아닌 보다 유동적인 가변 배열을 구상하게 된다.
 * 		
 *	
 * 
 * 
 */
public class ArraytwoEx11 {

	public static void main(String[] args) {	
		
//		int[][] arr = new int[5][3];//정형 배열, 고정배열 행과 열이 고정값.
		
		//가변 배열, 레기드 배열, 비정형 배열
//		int [][] arr = new int[3][];// 행은 지정하고, 열의 길이는 지정하지않는다. 
//		
//		arr[0] = new int[2]; // 첫번째 행의 열의길이는 2개
//		arr[1] = new int[4];
//		arr[2] = new int[3];
		
//		int[][] arr = {
//				{100,100},
//				{20,20,30,50},
//				{50,50,50}
//		};
		
		int[][] arr = new int [4][];
		
		arr[0] = new int[3];
		arr[1] = new int[2];
		arr[2] = new int[3];
		arr[3] = new int[2];
		
		for(int i = 0; i < arr.length; i++) {
//			arr[i] = new int[i+1];
			for(int j =0; j < arr[i].length;j++) {
				arr[i][j] = (i+1)*10+j;
				
			}
		}
		
			
			
		for(int i = 0 ; i < arr.length; i++) {
			for(int j = 0; j< arr[i].length;j++){
				System.out.printf("%3d\t", arr[i][j]);
			}
			System.out.println();
		}
	}

}
