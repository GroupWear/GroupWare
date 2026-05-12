package com.collection;
import java.util.*;
public class ArrayListEx01 {

	public static void main(String[] args) {
		ArrayList<Integer> list1 = new ArrayList<>(10);
		
		list1.add(5);//0
		list1.add(4);
		list1.add(2);
		list1.add(1);
		list1.add(0);
		list1.add(3);//5
		
		ArrayList list2 = new ArrayList (list1.subList(1, 4));
		System.out.println(list1);
		System.out.println(list2);
		print(list1,list2);
		
		Collections.sort(list1); // 오름차순정렬
		Collections.sort(list2);
		print(list1,list2);
		
		
		System.out.println("list1.containsAll(list2) : "+list1.containsAll(list2)); // list1 안에 list2가 포함되어있느냐. / true
		
		list2.add("B");
		list2.add("C");
		list2.add(3,"A"); //배열 3번째 인덱스에 A를 추가함.
		print(list1,list2);
		
		list2.set(3, "AA"); // set은 수정이 가능
		print(list1,list2);
		
		// retainAll -> list1에서 list2와 겹치는부분만 남기고 나머진삭제처리
		System.out.println("list1.retainAll : "+list1.retainAll(list2)); //교집합
		print(list1,list2);
		
		// list2에서 list1에 포함된 객체들을 삭제처리
		for(int i = list2.size()-1; i >= 0; i--) {
			if(list1.contains(list2.get(i))) {
				list2.remove(i);
			}
		}
		print(list1,list2);
	}
	static void print(ArrayList list1, ArrayList list2) {
		System.out.println("list1 : "+list1);
		System.out.println("list2 : "+list2);
		System.out.println();
	}
			
}
