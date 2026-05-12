package com.classex;

/* 	문]
 * 			이름, 전화번호 필드와 생성자 등을 가진 Phone 클래스를 작성하고,
 * 			실행클래스인 PhoneBook 클래스를 구현하시오.
 * 			PhoneBook -> read():입력저장, search(): 검색, run():실행
 * 
 * 			실행결과 
 * 			인원 수 : 3
 * 			이름과 전화번호 입력 : 가길동 010-1111-1111
 * 			이름과 전화번호 입력 : 가길동 010-1111-1111
 * 			이름과 전화번호 입력 : 가길동 010-1111-1111
 * 			저장이 되었습니다.
 * 			검색 이름  : 홍길동 
 * 			홍길동이(가) 없습니다.
 * 			 	
 * 			검색 이름  : 길가동
 * 			가길동의 전화번호는 010-1111-1111입니다.
 * 			검색할 이름 : 그만 --> 프로그램을 종료합니다. 를 풀력하고 끝낸다.
 * 			
 * 			hit : 객체배열을 활용, 문자열을 비교할때 equals활용
 */
import java.util.*;
class Phone{
	
	private String name;
	private String tel;
	
	public Phone(String name, String tel) {
		this.name = name;
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public String getTel() {
		return tel;
	}
	
}



public class PhoneBook {
	private Scanner sc;// 전역 변수 (인스턴스 변수)
	
	private Phone[] pArray; // Phone 객체배열 선언
	
	public PhoneBook() {
		// 생성자를 활용해서 스캐너를 생성한다.
		sc = new Scanner(System.in);
	}
	
	
	public void read() {
		// 인원수 입력
		System.out.print("인원 수 :");
		int n = sc.nextInt();
		// 인수만큼 객체배열 크기를 설정
		pArray = new Phone[n];
		// 인원수만큼 이름과 전화번호를 입력받아 저장한다.
		for(int i = 0; i < pArray.length; i++) {
			System.out.print("이름과 전화번호 입력 : ");
			String name = sc.next();
			String tel = sc.next();
			pArray[i] = new Phone(name, tel);// 한배열에 두개의 값을 저장.
		}
		System.out.println("저장 되었습니다.");
			
		}
	// 이름을 검색하여 정보를 추출한다.
	public String search(String name) {
		// 이름과 전화번호가 저장되어 있는 Phone 클래스에서 검색
		for(int i = 0; i < pArray.length; i++) {
			
			if(pArray[i].getName().equals(name)) {
				// 입력한 이름이 phone배열에 존재한다면 
				return pArray[i].getTel();//전화번호를 반환한다.
			} 
		}
		return null; // 이름에 해당하는 데이터가 없으면 null을 반환
	}
	
	public void run() {
		//정보를 가져와서 출력
		read();
		
		while(true) {
			System.out.print("검색할 이름 : ");
			String name = sc.next();
			
			if(name.equals("그만")) {
				System.out.println("프로그램을 종료합니다.");
				return;
			}
			String tel = search(name);// 내가 검색하고자 하는 이름이 phone 클래스에 존재하면 
			// 전화번호를 리턴받는다.
			if(tel == null) {
				System.out.println(name+"이 없습니다.");
			}else {
				System.out.println(name+"의 전화번호는 "+tel+"입니다.");
			}
		}
	}
	
	public static void main(String[] args) {
		
		//PhoneBook p = new PhoneBook();
		//p.run();
		new PhoneBook().run();
		
	}

}
