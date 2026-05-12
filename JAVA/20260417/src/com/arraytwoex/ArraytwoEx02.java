package com.arraytwoex;

/*		
 * 
 * 
 */
public class ArraytwoEx02 {

	public static void main(String[] args) {
		
		// 배열선언 및 초기화
		int[][] arr = { 
				{100, 200, 300}, // 첫번째 행을 의미하며 행을 구분할때는 콤마(,)로 한다.
				{400, 500, 600}  // 두번재 행
			//첫번째 열,두번째,세번째
		};
		
		for(int i = 0; i < arr.length;i++) {
			for(int j = 0; j < arr[i].length;j++) {
				System.out.printf("%d\t",arr[i][j]);
			}
			System.out.println();
		}
		System.out.println("===============");
		int sum = 0;
		for(int[] t :arr) { // arrd의 각 인덱스를 t에 저장함
			for(int i : t) {// t는 1차원 배열을 가르키는 참조변수
				sum += i;
				// System.out.print(i+"\t");
			}
		}
		System.out.println(sum);
	}

}
