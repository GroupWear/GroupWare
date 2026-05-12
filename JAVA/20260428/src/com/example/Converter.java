package com.example;

import java.util.Scanner;

public abstract class Converter {
	
	abstract protected double convert(double src);
	abstract protected String getSrcString();
	abstract protected String getDestString();
	protected double ratio; // 비율
	
	//실행 메소드 
	public void run() {
		Scanner sc = new Scanner(System.in);
		System.out.println(getSrcString()+"을"+getDestString()+"로 바꿉니다.");
		System.out.print(getSrcString()+"을 입력 : ");
		double val = sc.nextDouble();
		double result = convert(val);
		System.out.println("변환 결과 : "+result+getDestString());
	}
	
}
