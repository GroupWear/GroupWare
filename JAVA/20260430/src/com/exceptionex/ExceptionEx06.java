package com.exceptionex;

/*		throws : 예외전가
 * 		 - 자신을 호출한 메소드로 익셉션 처리를 위임한다.(떠 넘긴다.)
 * 		구성
 * 			[접근제한자] 반환형 메소드명 (인자1, 인자2,....) throws 예외클래스1, 예외클래스2, .....{
 * 				
 * 			}
 */

public class ExceptionEx06 {

	private static void test() throws Exception {
		System.out.println(6 / 0);
	}

	public static void main(String[] args) {

		// test();

		try {
			test();
		} catch (Exception e) {
			System.out.println("예외발생");
		}

	}

}
