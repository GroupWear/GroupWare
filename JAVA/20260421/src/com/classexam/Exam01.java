package com.classexam;
import java.util.*;
/*		사각형의 클래스 설계
 * 
 * 		사각형의 넓이와 둘레를 계산하는 클래스를 설계함.
 * 		
 * 		클래스명		- Rect
 * 		클래스 속성	- 필드(가로, 세로, 넓이, 둘레, 무게, 색깔, 재질)
 * 		클래스 기능	- 넓이 계산, 둘레 계산, 가로/ 세로길이 입력, 결과 출력
 * 						   area(), legnth(), input(), print()	
 * 		
 * 		객체의 구성 => 데이터(속성, 필드) + 기능(행위) 
 * 		클래스 구성 => 변수(멤버필드) + 메소드(함수)
 */

class Rect{//클래스 설계
	
	// 변수선언(멤버필드) -> 가로, 세로 
	// 전역변수 -> 클래스 안에서 유효한 변수.
	// 멤버변수 -> 클래스에 소속되어 있는 하나의 구성요소를 의미.
	// 인스턴스 변수 -> 객체가 생성될 때 메모리 할당이 이루어지는 변수 
	
	int w, h;// 인스턴스 변수
	
	/* 메소드 정의 (가로/세로 입력, 넓이계산, 둘레계산, 출력)
	 *	
	 * 	반환값이 있으면 리턴형을 갖고 있을 것이고,
	 * 반환값이 없으면 void로 정의함.
	 */
	
	void input() {//입력기능 (가로, 세로)
		Scanner  sc = new Scanner(System.in);
		
		System.out.print("가로 : ");
		w = sc.nextInt();
		System.out.print("세로 : ");
		h = sc.nextInt();
	}
	// 넓이 계산 기능
	// 메소드를 실행 후 게산될 결과를 반환
	int area() {
		int result;
		result = w*h;
		return result;
		//메소드는 return문을 통해서만 결과를 돌려(반환)주게 된다.
	}
	//둘레 계산 기능
	int length() {
		int result;//지역변수 : 함수 안, 또는 블록({ }) 안
		result = (w+h)*2;
		return result;
	}
	//결과 출력 기능
	void print(int a, int l) {
		System.out.println();
		System.out.println("가로 : "+w);
		System.out.println("세로 : "+h);
		System.out.println("넓이 : "+a);
		System.out.println("둘레 : "+l);
	}
};

public class Exam01 {

	public static void main(String[] args) {
		Rect re = new Rect();
		// 입력 메소드 호출
		re.input();
		
		int a = re.area();// 넓이값
		int l = re.length();// 둘레 값
		
		re.print(a, l);
	}

}
