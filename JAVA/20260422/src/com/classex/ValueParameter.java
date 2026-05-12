package com.classex;

/*		call by value(값에 의한 호출)
 * 			자바의 기본 자료형(8가지)
 * 		
 * 		메소드 호출시 기본 자료형의 값을 인자로 전달하는 방식
 * 		
 */


public class ValueParameter {

	public int increase(int n) {
		n++;
		return n;
	}
	
	public static void main(String[] args) {
		ValueParameter v = new ValueParameter();
		int var1 = 100;
		int var2 = v.increase(var1);
		System.out.println(var2);
	}

}
