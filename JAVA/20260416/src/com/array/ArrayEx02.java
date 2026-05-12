package com.array;

/*		문]
 * 			정수 5개를 입력받아 배열에 저장하고, 
 * 			그 중 제일 큰 값을 출력하는 프로그램을 작성하시오.
 * 		
 * 
 */
import java.util.*;
public class ArrayEx02 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int[] aa = new int[5]; 
		int max= 0; // 가장 큰 값을 저장할 변수
		
		for(int i = 0; i < aa.length; i++) {
			System.out.print(i+1+"번째 정수입력 : ");
			aa[i] = sc.nextInt();
			if(aa[i] > max) {//배열에 저장된 값하고 max에 저장된 값을 비교하여 
				//배열에 저장된 값이 크면 max에 저장한다.
				max = aa[i];
			}
		}
		System.out.println("배열에 저장된 값을 가장 큰 값은"+max+"입니다.");
	}

}
