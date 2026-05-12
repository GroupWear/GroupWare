package com.collection;

/*		자료 
 * 			- 현실 세계로부터 단순한 관측이나 측정을 통해 수집된 사실이나 개념의 값들 또는 그 값들의 집합
 * 		
 * 		자료 구조
 *			- 자료 처리를 위한 자료의 표현, 저장, 관계, 관리 및 이용에 관한 방법등의 개념을 이해하여 
 *			   프로그램에 사용하고 컴퓨터에 의해 처리될 때, 적절한 자료의 표현, 구성, 저장 및 처리를 위한 
 *			   알고리즘의 작성 및 선택과 이용방법을 의미함.	
 *			
 *		컬렉션 프레임워크
 *			   컬렉션의 표현과 조작을 위한 통일된 아키텍처로 컬렉션 프레임 워크를 구성 세가지요소 
 *			   
 * 			   1. 인터페이스(interface)
 * 					- 컬렉션에 필요한 데이터 관리 기능을 제공함
 * 					   list는 추가, 삭제, 검색 등의 기능을 제공
 * 					   각 컬렉션마다 고유의 인터페이스 정의
 * 
 * 			   2. 구현(implements)
 * 					- 인터페이스를 구현한 클래스고 각 컬렉션을 실제 어떤 자료구조를 이용하여 표현했느냐에 따라
 * 					   컬렉션의 종류가 달라짐
 * 	   	   
 * 		       3. 알고리즘(Algorithms)
 * 					- 각 컬렉션마다 유용하게 사용할 수 있는 메소드 (기능)을 의미함 
 * 						   
 * 				자바의 컬렉션은 견고한 컬렉션 프레임워크 기반위에 정의되어 있으므로 
 * 				각 컬렉션 종류마다 제공되는 메소드는 일관성을 가지고 있다.
 * 				한가지 컬렉션을 익혀두면 다른 컬렉션을 사용하는데 용이하다. 
 */
import java.util.*;

import javax.swing.Popup;

public class CollectEx01 {

	public static void main(String[] args) {
		//스택 객체 생성
		Stack<String> myStack = new Stack<>(); // <>안에 String을 넣어줘도되고 안넣어도됨.
		
		String str1 = "1-aaa";
		String str2 = "2-aaa";
		String str3 = "3-aaa";
		String str4 = "4-aaa";
		
		// 스택에 데이터 추가
		// add(), push()
		myStack.add(str1);
		myStack.add(str2);
		myStack.push(str3);
		myStack.push(str4);
		
		System.out.println(myStack);
		
		// peek() : 스택의 맨 위 요소를 제거하지 않고 그냥 확인만 함
		String result1 = myStack.peek();
		System.out.println(result1);
		
		// push() : 스택의 맨 위에 요소를 추가
		myStack.push("5-aaa");
		System.out.println(myStack);
		
		// search(Object o) : 찾는 객체가 스택의 몇 번째에 있는지 반환 (1부터 시작)
		System.out.println(myStack.search("3-aaa"));
		
		// pop() : 스택 맨 위 요소를 꺼내고 반환 (제거됨)
		System.out.println(myStack.pop());
		System.out.println(myStack);
		
		while(!myStack.isEmpty()) { // myStack이 비어있지않다면
			result1 = myStack.pop();
			System.out.println(myStack);
		}
		
		
	}
	
}
