package com.interfaceEx;

/*		추상클래스와 인터페이스 비교
 * 		
 * 		abstract class class명 {
 * 		
 * 		모든 멤버 변수들;
 * 		모든 멤버 메소드들;
 * 
 * 		}
 * 		- 단일 상속만 가능함
 * 
 * 		interface interface 명 {
 * 
 * 		(public static final) int x = 10;// 상수만 가능
 *		(public abstract) void disp(); // 추상메소드만 가능
 *		}
 *		 
 * 		- 다중 상속이 가능함.
 *	
 */
public class InterfaceEx02 implements Inter3 {
	int a = 100;

	@Override
	public int getA() {
		// TODO Auto-generated method stub
		return a;
	}

	@Override
	public int getData() {
		// TODO Auto-generated method stub
		return a + 10;
	}

	public static void main(String[] args) {
		InterfaceEx02 in = new InterfaceEx02();
		Inter1 in1 = in;
		Inter2 in2 = in;
		Inter3 in3 = in;

		System.out.println(in1.getA());
		System.out.println(in2.getA());
		System.out.println(in3.getData());

	}
}
