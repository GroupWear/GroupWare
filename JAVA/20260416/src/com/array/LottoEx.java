package com.array;

/*		문]
 * 			배열을 활용하여 로또번호 생성기를 구현하시오.
 * 			
 * 
 * 
 */
public class LottoEx {

	public static void main(String[] args) {
		
		// 45개의 정수값을 저장할 배열을 생성.
		int[] lotto = new int[45];
		
		// 생성한 배열에 1 ~ 45개의 값을 저장. 
		
		for(int i = 0; i < lotto.length;i++) {
			lotto[i] = i+1;
//			System.out.print(lotto[i]+" ");
		}
		// 두 값을 바꾸기 위해서 임시로 저장할 변수 선언
		int temp = 0;
		// 랜덤으로 생성한 값을 저장할 변수 선언
		int j = 0; // (0 ~ 44) +1 = 1 ~ 45
		
		for(int i = 0; i < 6; i++) {
			
			j = (int)(Math.random() * 45);
			
			temp = lotto[i];
			lotto[i] = j;
			j = temp;
					
		}
		for(int i = 0; i < 6; i++) {
			System.out.printf("%d\t",lotto[i]);
		}
	}

}
