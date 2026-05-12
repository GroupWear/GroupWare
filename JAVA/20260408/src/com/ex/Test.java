package com.ex;

public class Test {

	public static void main(String[] args) {
		System.out.println("안녕하세요."); // br이랑 똑같음. ln이 Line Next의 약자.
		System.out.print("안녕하세요."); // 행을 띄지않고 유지.
		
		
		System.out.printf("100"); // 서식을 지정해서 출력. 1, 1, 0으로 받아들임.
		System.out.printf("%d",100); // 완전한　숫자 100을 의미.
		System.out.printf("%d",100 + 100); // 숫자를 계산해서 결과를 출력.
		
//		System.out.printf("%n";)// %n 줄 바꾸기
		
		System.out.printf("%d %d", 100, 100);// %d와 숫자의 개수를 맞춰줘야함. 아니면 에러
		
		
	}

}
