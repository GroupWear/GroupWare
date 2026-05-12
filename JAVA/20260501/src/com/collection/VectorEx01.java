package com.collection;

/*		
 * 		벡터
 * 			자료구조의 기능을 제공하는 클래스
 * 			배열과 비슷하지만, 배열과 달리 다른 종류의 데이터형을 가진
 * 			데이터 요소를 가질수 있으며, 데이터가 가득 차면 자동으로 
 * 			저장공간을 늘려준다.
 * 			
 * 		
 * 
 */


import java.util.*;

public class VectorEx01 {

	// 문자열 배열 선언 및 초기화
	private static final String[] Colors = { "검정", "노랑", "초록", "파랑", "빨강", "연두" };
	
	
	
	
	
	public static void main(String[] args) {
		
		int i;
		String str;
		
		// 벡터 인스턴스생성
		Vector<Object> v = new Vector<>()	;
		
		for(i = 0; i < Colors.length;i++) {
			v.add(Colors[i]);
		}
		
		v.addElement("[추가]");
		System.out.println(v);
		
		str = (String)v.firstElement();
		System.out.println("첫번째 요소 : "+str);
		
		str = (String)v.get(1);
		System.out.println("두번째 요소 : "+str);
		
		str = (String)v.elementAt(2);
		System.out.println("세번째 요소 : "+str);
		
		str = (String)v.lastElement();
		System.out.println("마지막 요소 : "+str);
		
		/*
		 *	이터레이터(Iterator) : 반복자
		 *		hasNext() : 다음 요소가 있는지를 판단
		 * 		next() : 다음 데이터를 가져옴
		 */
		Iterator<Object> it = v.iterator();
		
		while(it.hasNext()) {
			str = (String)it.next();
			System.out.print(str+"  ");
		}
		
		
	}
	
}
