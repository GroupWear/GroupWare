package com.operex;


/* 	조건 삼항 연산자
 * 		조건식	?조건식이 참일때 : 조건식이 거짓일 때 
 * 		
 */

public class OperEx01 {

	public static void main(String[] args) {
		
		int x, y, z;
		int absX, absY, absZ;
		char singX, singY, singZ;
		
		x = 10;
		y = -5;
		z = 0;
		
		// x의 값이 0보다 작으면 음수, 0보다 크면 양수.		
		absX = x >= 0 ? x : -x;
		if(x >= 0) {
			absX = x;
		}else {
			absX = -x;
		}
		
		absY = y >= 0 ? y : -y;// "조건 ? 참 : 거짓"
		absZ = z >= 0 ? z : -z;// ":" 참과 거짓을 구분하는 기호. 
		// 콜론(:) 뒤는 조건이 거짓일 때 값
		
		// 조건삼항을 중첩으로 실행.
		singX = x > 0 ? '+' : (x == 0 ? ' ':'-');
		singY = y > 0 ? '+' : (y == 0 ? ' ':'-');
		singZ = z > 0 ? '+' : (z == 0 ? ' ':'-');
		
		System.out.printf("x = %c%d%n", singX, absX);
		System.out.printf("y = %c%d%n", singY, absY);
		System.out.printf("z = %c%d%n", singZ, absZ);
		
		
	}

}

//순서,코드 조각,의미 설명
// 1, absY,왼쪽 변수 이름. 여기다가 결과를 저장하겠다는 뜻입니다.
// 2, =,대입 연산자. 오른쪽에 계산한 결과를 absY에 넣으라는 의미
// 3, y,조건을 검사할 변수. 지금 y에는 -5가 들어있습니다.
// 4, >=,크거나 같다 비교 연산자
// 5, 0,숫자 0. y가 0보다 크거나 같은지 비교할 대상
// 6, ?,"삼항 연산자의 시작. 여기서부터 ""조건 ? 참 : 거짓"" 형태가 시작됩니다."
// 7, y,조건이 참일 때 사용할 값. y가 0 이상이면 이 y를 그대로 사용
// 8, :,참과 거짓을 구분하는 기호. 콜론(:) 뒤는 조건이 거짓일 때 값
// 9, -y,조건이 거짓일 때 사용할 값. y 앞에 마이너스(-)를 붙여 부호를 바꿈
// 10, ;,문장의 끝을 나타내는 세미콜론
