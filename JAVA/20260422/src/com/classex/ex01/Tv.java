package com.classex.ex01;

/*		Tv 클래스를 작성하시오.
 * 
 * 		필드 (멤버 변수) : 제조사, 년도, 인치
 * 		생성자를 활용하여 초기화 
 *		//메소드 : 출력하는 메소드 show(); 
 * 
 * 	public static void main(String[] args) {
		Tv는 myTv = new Tv("LG", 2026,32); //LG에서 만든 2026년형 32인치 Tv
		myTv.show();
		
		
	}
	
			결과 
			LG에서 만든 2026년형 32인치 Tv
			
 * 		
 * 
 * 
 */


public class Tv {
	// 필드 선언
	private String maker;
	private int year;
	private int inch;
	
	public Tv() {
		//생성자를 활용하여 필드를 초기화함.
		this.maker = "무명";
		this.year = 2026;
		this.inch = 32;
	}
	public Tv(String a) {
		this();
		this.maker = a;
	}
	public Tv(String a, int b) {
		this(a);
		this.year=b;
	}
	public Tv(String a, int b, int c) {
		this(a,b);
		this.inch = c;
	}
	
	public String getMaker() {
		return maker;
	}

	public int getYear() {
		return year;
	}

	public int getInch() {
		return inch;
	}

	public void show() {
		System.out.println(getMaker()+"에서 만든"+getYear()+"년형"+getInch()+"인치 Tv");
		System.out.println(maker+"에서 만든"+year+"년형"+inch+"인치 Tv");
	}
	
	public static void main(String[] args) {
	
		Tv myTv = new Tv("LG", 2026, 32); //LG에서 만든 2026년형 32인치 Tv
		myTv.show();
		
		
	}

}
