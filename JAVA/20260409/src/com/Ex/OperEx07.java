package com.Ex;

public class OperEx07 {

	public static void main(String[] args) {
		
		System.out.printf(" 10 == 10.0f\t %b%n", 10==10.0f);//\t는 탭역할해줌 스페이스바 4개한거 ㅇㅇ
		// 10 == 10.0f -> 현재 자료형은 int == float인데 두 수치를 비교하기 위해서 자료형을 일치 시킨다.
		// 10을 float으로 변환함. 10.0f==10.0f 이므로 두값이 같으므로 결과는 true가 나온다. 
		
		System.out.printf(" '0' == 0 \t %b%n", '0'==0);
		System.out.printf(" 'A' == 65 \t %b%n", 'A'==65); //유니코드값이 65가 A임
		System.out.printf(" 'A' > 'B' \t %b%n", 'A' > 'B');
		System.out.printf(" 'A'+1 != 'B' \t %b%n", 'A'+1 != 'B');
	}

}
