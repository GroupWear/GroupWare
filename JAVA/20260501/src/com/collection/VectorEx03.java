package com.collection;
import java.util.*;
public class VectorEx03 {

	// 문자열 배열 선언 및 초기화
	private static final String[] Colors = { "검정", "노랑", "초록", "파랑", "빨강", "연두" };
	
	public static void main(String[] args) {
		
		Vector<String> v = new Vector<>()	;
		
		for(String str : Colors) {
			v.add(str);
		}
		System.out.println("첫번째 요소 : "+v.firstElement());
		System.out.println("두번째 요소 : "+v.get(1));
		System.out.println("마지막 요소 : "+v.lastElement());
		System.out.println();
		//첫번째 요소를 흰색으로 변경
		v.set(0, "흰색");
		System.out.println("첫번째 요소 : "+v.firstElement());
		System.out.println("요소 개수 : "+v.size());
		System.out.println();
		// 첫번째 요소에 빨강 추가
		v.insertElementAt("빨강", 0);
		System.out.println("첫번째 요소 : "+v.firstElement());
		System.out.println("요소 개수 : "+v.size());
		System.out.println();
		System.out.println("전체출력 : ");
		for(String str : v) {
			System.out.print(str+"  ");
		}
		System.out.println();
		System.out.println();
		
		// 오름차순 정렬
		System.out.println("오름차순정렬 후 : ");
		Collections.sort(v);
		for(String str : v) {
			System.out.print(str+"  ");
		}
		System.out.println();
		
		// 검색
		int idx = Collections.binarySearch(v, "파랑");
		System.out.println("\n 파랑 : "+idx+" index에 위치");// 찾고하는 요소가 없을 경우 음수를 출력
		
		System.out.println();
		
		// 내림차순 정렬
		System.out.println("내림차순정렬 후 : ");
		Collections.sort(v, Collections.reverseOrder()); // 오름차순을 반대로 정렬해줌.
		for(String str : v) {
			System.out.print(str+"  ");
		}
		
		// 내림 차순 정렬 후 자료 검색할 때는 
		idx = Collections.binarySearch(v, "파랑", Collections.reverseOrder());
		System.out.println("\n 파랑 : "+idx+" index에 위치");// 
		
		// 검색
		String s = "노랑";
		if(v.contains(s)) {
			int i = v.indexOf(s)+1;
			System.out.println(s+" --> 위치 : "+i);
		}
		
		System.out.println();
		
		// 삭제
		v.remove(0);
		v.remove("파랑");
		System.out.println("삭제 후 : ");
		for(String str : v) {
			System.out.print(str+"  ");
		}
		
		
		
		
		
		
	}
}
