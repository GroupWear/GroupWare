package com.ioex;
// 문자열 입력 받기
// 		-문자 하나 이상(문자열) 입력 받기
//
// InputStreamReader newInputStreamReader(System.in);
// BufferedReader br = new BufferedReader(is);
//
// BufferedReader br = new InputStreamReader(System.in));
// 버퍼 객체 생성
//
// br.readLine(); 한줄에 입력한 문자열을 입력 받는다.
// 입력받은 문자열을 name변수에 저장한다.
// string name = br.readLine();


import java.io.*;
public class InputEx04 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = 
				new BufferedReader(new InputStreamReader(System.in));
		
		String name;
		String nai;
		String addr;
		String tel;
		
		System.out.print("이름 입력 : ");
		name = br.readLine();
		
		System.out.print("나이 입력 : ");
		nai = br.readLine();
		
		System.out.print("주소 입력 : ");
		addr = br.readLine();
		
		System.out.print("전화 번호 입력 : ");
		tel = br.readLine();
		
		System.out.println(name+"씨 ");
		System.out.println(nai+"나이 ");
		System.out.println(addr+"주소 ");
		System.out.println(tel+"전번 ");
	}

}
