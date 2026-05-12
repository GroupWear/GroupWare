package com.arraythree;

/*		문]
 * 			배열을 이용하여 369게임에서 박수를 쳐야하는 경우 순서대로 화면에 출력하는 프로그램
 * 		
 * 			범위 1~99까지
 * 
 * 			결과 
 * 			3 박수짝
 * 			6 박수짝
 * 			9 박수짝
 * 			
 */

public class ArrayExam03 {

	public static void main(String[] args) {
		String[] str = {"박수짝", "박수짝짝"};
		
		int result =0, num, tsn = 0;
		
		for(int i = 1; i < 100; i++) {
			num = 1;
			for(result = num % 10; num > 0;result = num % 10) {
				if(result == 3 || result == 6 || result == 9) {
					tsn++;
				num = num/10;
				}
				if(tsn > 0) {//3 6 9중 하나의 수가 존재하므로 
					System.out.println(i+str[tsn-1]);
				}
				tsn=0;
			}
		}
	}
}

