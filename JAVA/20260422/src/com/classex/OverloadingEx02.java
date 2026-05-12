package com.classex;

public class OverloadingEx02 {
	// 메소드 오버로딩일때
	public void getLength(int n) {
		String s = String.valueOf(n);
		getLength(s);
	}
	public void getLength(int n, int m) {
		String s = String.valueOf(n);
		getLength(s);
	}
	public void getLength(float n, int m) {
		String s = String.valueOf(n);
		getLength(s);
	}
	private int getLength(String str) {
		System.out.println("입력한 길이 : "+str.length());
		return 0;
	}
	private int getLength(String str, String str2) {
		System.out.println("입력한 길이 : "+str.length());
		System.out.println("입력한 길이 : "+str.length()+", "+str2.length());
		return 0;
	}
	
	public static void main(String[] args) {
		
		OverloadingEx02 oe2 = new OverloadingEx02();
		
		oe2.getLength(1000);
		oe2.getLength(1000,1000);
		oe2.getLength(3.14f, 100);
		oe2.getLength("1000");
		oe2.getLength("1000","1000");
		
		
	}

}
