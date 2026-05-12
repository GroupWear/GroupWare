package com.swtichex;

/*
 * 		문]
 * 			월을 입력받아 월에 해당하는 계절을 출력하는 프로그램을 구현하시오.
 * 			봄		:3,4,5
 * 			여름	:6,7,8
 * 			가을	:9,10,11
 * 			겨울	:12,1,2
 * 			
 * 			결과 
 * 			월 입력 : 4
 * 			현재의 계절은 봄입니다.
 */

import java.io.*;

public class SwitchEx04 {

	public static void main(String[] args)throws IOException {
		
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
		
		int n1 = 0;
		String result;
		System.out.printf("월 입력 : ");
		n1 = Integer.parseInt(sc.readLine());
		
		switch (n1) {
		case 3:
		case 4:
		case 5:
			result = "봄";
			break;
		case 6:
		case 7:
		case 8:
			result = "여름";
			break;
		case 9:
		case 10:
		case 11:
			result = "가을";
			break;
		case 12:
		case 1:
		case 2:
			result = "겨울";
			break;
			
		default:System.out.println("오류");
			return;
		}
		System.out.printf("현재의 계절은 %s입니다.", result);
		sc.close();
	}

}
