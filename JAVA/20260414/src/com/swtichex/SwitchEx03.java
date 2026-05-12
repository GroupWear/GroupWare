package com.swtichex;

/*
 * 		문]
 * 			Switch case문을 이용하여 글로벌 카페의 커피메뉴의 가격을 알려주는 프로그램을 작성하시오.
 * 
 * 			커피 종류 : 에스프레소, 카푸치노, 카페라떼. = 3500원
 * 						 아메리카노 = 2000원
 * 
 * 			무슨 커피를 드릴까요?  : 카페라떼
 * 			카페라떼는 3500원입니다.
 */

import java.io.*;

public class SwitchEx03 {

	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String str1;
		int result= 0;
		// int result = '0';  -->  문자 '0' → 숫자 48 저장됨 아키스코드로 출력이 되기 때문에, ' ' 없이 출력 숫자는
		// 추가로 String의 경우 문자열이기때문에 계산이 필요할 경우 숫자로 변환을 해줘야하기때문에 아래와 같다.
		//= 0; -> 숫자 (int 등)
		//= ' '; -> 문자 (char 등)
		
		System.out.print("무슨 커피를 드릴까요? : ");
		str1 = br.readLine();
		
		
		switch (str1) {
		case "에스프레소":
		case "카페라떼":
		case "카푸치노":
			result = 3500;
			break;
		case "아메리카노":
			result = 2500;
			break;
			
		default:
			System.out.println("오류입니다.");
			return;
			//break;
			
		}
		// if(price !=0)
		System.out.printf("%s은(는) %d원 입니다.",str1, result);
		br.close();
	}

}
