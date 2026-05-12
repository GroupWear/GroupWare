package com.Ex;

/*
 * 	byte : 1byte 자료형
 * 			 입출력 범위 : -128 ~ +127
 */
public class ByteEx {

		static int cc; // static 공용 다른사람도 쓸 수 있도록, 
							 // 클래스 변수 (공유변수) : 초기화를 하지 않아도 됨.
							 // 객체를 생성하지 않아도 접근이 가능한 변수.
		int aa; // 인스턴스 변수 --> 객체를 생성해야만 접근이 가능한 변수.
		
		public static void ccc() {//static을 붙이면 b.ccc();가아닌 ccc();만 써도 사용가능.
			System.out.println("나는 너늘ㄴㄻㅇㄴ");
		}
		
	public static void main(String[] args) {
		// 객체 생성 --> new 연산자와  생성자와 같이 겹합되어 객체를 생성한다.
		ByteEx b= new ByteEx();
		
		byte bb = 127; 
		// 129를 쓰면 에러 남. 범위가 -128~127까지
		
		bb++; 
		//bb = bb + 1과 같음. 
		
		System.out.println("byte bb : "+ bb);
		System.out.println(b.aa);
//		b.ccc();
		ccc();
	}

}

//종류,기호,의미,예시
//산술 연산자,"+, -, *, /, %","더하기, 빼기, 곱하기, 나누기, 나머지","bb + 1, 5 % 2"
//증감 연산자,"++, --","1 증가, 1 감소",bb++ (당신 코드에 있음)
//대입 연산자,"=, +=, -=",값을 넣거나 계산 후 넣기,"bb = 127, aa += 5"
//비교 연산자,"==, !=, >, <","같다, 다르다, 크다, 작다",if (bb > 0)
//논리 연산자,"&&, ||, !","그리고, 또는, 아니다",if (a > 0 && b < 10)