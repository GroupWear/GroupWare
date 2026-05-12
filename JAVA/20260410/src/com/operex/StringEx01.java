package com.operex;

public class StringEx01 {

	public static void main(String[] args) {
		
		String str1 = "abc";
		// 아래의 문장을 간단하게 표현함.
		
		String str2 = new String("abc");
		// String 클래스의 객체를 생성.
		// new 연산자를 이용해서 객체를 생성한다.
		
		// 문자열 비교
		System.out.printf("\"abc\"==\"abc\" ? %b%n","abc"=="abc");
		System.out.printf("str1==\"abc\" ? %b%n", str1=="abc");
		System.out.printf("str2==\"abc\" ? %b%n", str2=="abc");
		// str2는 객체이므로 "abc"는 다른 객체로 인식하기 떄문에 내용은 같지만, 
		// 다른 객체라서 서로 다르다.
		// \"  -> "
		
		System.out.printf("str1.equals(\"abc\")? %b%n",str1.equals("abc"));
		// equals() 메소드는 객체가 달라도 내용이 같으면 true, 같지않으면 false.
		// 문자열을 비교할때는 항상 equals() 메소드를 사용해야한다.
		// 내용이 대소문자가 같이 있을 경우 대소문자를 구분하지 않고 비교하고 싶다면,
		//equals() 메소드 대신 equalsIgnoreCase() 메소드 사용하면 올바른 답을 얻을 수 있다.
		System.out.printf("str2.equals(\"abc\")? %b%n",str2.equals("abc"));
		System.out.printf("str2.equals(\"ABC\")? %b%n",str2.equals("ABC"));
		System.out.printf("str2.equalsIgnoreCase(\"ABC\")? %b%n",str2.equalsIgnoreCase("ABC"));
	}

}
