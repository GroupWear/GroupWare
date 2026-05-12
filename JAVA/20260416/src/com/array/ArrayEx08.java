package com.array;

/*		최대값과 최소값 구하기.
 * 			79, 88, 91, 33, 100, 55, 95
 * 		
 * 			배열에 저장된 값중 최대값과 최소값을 구하는 프로그램을 구현하시오.
 */
public class ArrayEx08 {

	public static void main(String[] args) {
		
		int[] v = {79, 88, 91, 33, 100, 55, 95};
		int max = v[0];
		int min = v[0];
		
		for(int i =0; i < v.length;i++) {
			System.out.printf(v[i]+" ");
			if(v[i] > max) {
				max = v[i];
			}else 
			if(v[i] < min) {
				min = v[i];
			}
		}
		System.out.println();
		System.out.printf("최대 값 : %d%n",max);
		System.out.printf("최대 값 : %d%n",min);
		
	}

}
