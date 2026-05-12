package com.collection;

import java.util.*;

public class CollectEx02 {

	// 문자열 배열 선언 및 초기화
	private static final String[] Colors = { "검정", "노랑", "초록", "파랑", "빨강", "연두" };

	public CollectEx02() {
		// 스택 인스턴스 생성
		Stack<String> st = new Stack<>();

		// 스택에 데이터 저장
		for (String color : Colors) {
			st.push(color);
		}
		popStack(st);
	}

	public void popStack(Stack<String> st) {
		System.out.print("pop : ");
		while (!st.isEmpty()) {
			System.out.print(st.pop()+" ");
		}
		System.out.println();
	}

	public static void main(String[] args) {

		CollectEx02 c = new CollectEx02();

		
	}

}
