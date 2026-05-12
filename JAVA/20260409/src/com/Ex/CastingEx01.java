package com.Ex;

/*		형변환(캐스팅, casting)
 * 		- 변수나 리터럴 타입을 다른 타입으로 변환하는 것을 의미함.
 * 		형변환 -> 변수 또는 상수의 타입을 다른 타입으로 변환.
 * 		
 * 
 * 		형 변환 방법
 * 		
 * 		프로모션(자동형변환)
 * 			- 더 큰 자료형으로 변환(자동)
 * 			정보의 손실없이 자동으로 형변환 처리함
 * 		ex) short a, b;
 * 			  a = b = 10;
 * 			  int c = a + b;
 * 
 * 		디모션(명시적형변환-> 강제형변환)
 * 				- 더 작은 자료형으로 만들어서 처리하기 때문에
 * 				   작은 자료형을 명시해야함
 * 				   정보의 손실을 가져옴.
 * 		ex) int c = 0;
 * 			  short s = 10;
 * 			  c = (int)(10+3.5f);
 * 
 * 		boolen 형은 형변환 불가
 * 		byte -> char
 * 		long -> float 자동형변환됨
 * 					이유 : 실수형이 정수형보다 크기 때문.
 * 		
 */
public class CastingEx01 {

	public static void main(String[] args) {
		double d = 85.4;
		int score = (int)d; // double이 용량이 더 크기때문에 넣어지면 오류가나오지만 (int)를 넣어줌으로서 
		// d는 실수형이기때문에 정수형보다 크다.
		// 따라서 score에 저장하려면 먼저 정수형으로 형변환하고 
		// 저장하면 된다.
		System.out.println("score : "+score);
		System.out.println("d : "+d);

	}

}
