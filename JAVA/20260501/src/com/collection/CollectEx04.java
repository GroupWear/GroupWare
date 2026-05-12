package com.collection;
import java.util.*;
public class CollectEx04 {

	private static final String[] Colors = { "검정", "노랑", "초록", "파랑", "빨강", "연두" };
	
	public static void main(String[] args) {
		
		Queue<String> qu = new LinkedList<>();
		
		
		//offer : 큐에 데이터를 추가로 저장
		for(String str : Colors) {
			qu.offer(str);
		}
		// poll : 큐에 해더의 요소를 리턴하고 삭제 / 꺼내쓰고 버린다는것. 큐의 헤더는 가장 마지막의 배열이라 생각하자 
		while (qu.peek() != null) {
			System.out.println(qu.poll()+" ");
		}
		System.out.println();
	}

}
