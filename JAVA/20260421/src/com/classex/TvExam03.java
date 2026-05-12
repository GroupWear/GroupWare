package com.classex;

/*		데이터 저장 형태
 * 
 * 		1. 변수 : 하나의 데이터를 저장할 수 있는 공간.
 * 		2. 배열 : 같은 종류의 여러개의 데이터를 저장할 수 있는 공간.
 * 		3. 구조체 : 서로 관련된 여러 데이터를 종류에 상관없이 하나의 그룹으로 저장할 수 있다.
 * 		4. 클래스 : 데이터와 함수의 결합체(구조체 + 함수)
 */

public class TvExam03 {

	public static void main(String[] args) {
		// 객체 배열 : 참조변수들을 하나의 묶은 참조 변수 배열
		Tv t1, t2, t3; // Tv라는 객체 3개.
		Tv[] tvArr = new Tv[3];// 길이가 3인 Tv 참조 변수
		
		// 생성자를 이용하여 메모리를 할당해야한다.
		tvArr[0] = new Tv();
		tvArr[1] = new Tv();
		tvArr[2] = new Tv();
		
		Tv[] tvArr2 = { new Tv(), new Tv(), new Tv()};
		//배열을 이용하여 객체를 초기화한다.
		
		for(int i = 0 ; i  < tvArr.length; i++) {
			tvArr[i] = new Tv();
			tvArr[i].channel = i + 10;
			System.out.println(tvArr[i].channel); 
		}
		System.out.println();
		for(int i = 0 ; i  < tvArr.length; i++) {
			tvArr[i].channelUp();
			System.out.println(tvArr[i].channel); 
		}
	}

}
