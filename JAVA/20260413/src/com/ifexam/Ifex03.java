package com.ifexam;

/*
 *	블럭, 블레이스, 중괄호 => {} 
 *	-> 여러 문장을 하나의 단위로 묶을 수 있다.
 *		{로 시작해서 	}로 끝난다.
 * 		;을 붙이지않는다.
 * 
 * 		문]
 * 			사용자로 부터 임의의 정수를 입력받아
 * 			입력받은 정수가 0인지 0이 아닌지를 판정하는 
 * 			프로그램을 구현하시오.
 */
import java.util.*;
public class Ifex03 {
	public static void main(String[] args) {
		
		/*
		int score = 55;
		
		if(score > 60) { 
			System.out.println("합격입니다.");
		}
			System.out.println("축하합니다.");// -> if문에 관계없는 문장 그냥 실행되는것임.
	}
	*/
		int a;
		System.out.println("임의의 정수 입력 : ");
		Scanner sc = new Scanner(System.in);
		a = sc.nextInt();
		
		if(a == 0) {
			System.out.println("0입니다.");// 조건이 참일때 수행하는 문장
		}
		if(a != 0)
			System.out.println("0이 아닙니다.");// 조건이 거짓일때 수행하는 문장
			System.out.printf("입력하신 숫자는 %d입니다.", a);// 그냥 상관없이 실행되는 문장
		
			sc.close();
	}
}
