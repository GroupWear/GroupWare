package com.collection;

/*		큐(Queue)
 * 			큐는 FIFO(First Input, First Output)구조로
 * 			먼저 입력된 데이터가 먼저 출력된다.
 * 			큐는 인터페이스를 구현한 클래스는 큐와 관련된
 * 			다양한 메소드를 제공함
 * 
 * 			삽입 메서드
 * 			add(E e)		큐에 요소 추가 		IllegalStateException 예외 던짐
 * 			offer(E e)		큐에 요소 추가			false 반환 (안전)
 * 
 * 			조회 메서드 (제거 ❌)
 *			element()		맨 앞 요소 확인		💥 예외 던짐
 *			peek()			맨 앞 요소 확인		null 반환 (안전)
 * 
 * 			삭제 메서드 (제거 ✅)
 * 			remove()		맨 앞 요소 꺼냄		💥 예외 던짐
 * 			poll()			맨 앞 요소 꺼냄		null 반환 (안전)
 */
import java.util.*;
public class CollectEx03 {

	public static void main(String[] args) {

		// Queue 인스턴스 생성
		Queue<String> myQueue = new LinkedList<>();
		
		String str1 = "1-aaa";
		String str2 = "2-aaa";
		String str3 = "3-aaa";
		String str4 = "4-aaa";
		
		// Queue에 데이터 추가
		myQueue.offer(str1);
		myQueue.offer(str2);
		myQueue.offer(str3);
		myQueue.offer(str4);
		
		System.out.println("값 1: "+myQueue.element());
		
		System.out.println();
		
		String val;
		// 조회 메서드 (제거 ❌)
		// peek()			맨 앞 요소 확인		null 반환 (안전)
		// 삭제 메서드 (제거 ✅)
		// poll()			맨 앞 요소 꺼냄		null 반환 (안전)
		while(myQueue.peek() != null) {
			val = myQueue.poll(); // Queue의 head요소를 반환하고 제거한다.
			System.out.print(val+", ");
		}
		
		System.out.println();
		
		myQueue.offer(str1);
		myQueue.offer(str2);
		myQueue.offer(str3);
		myQueue.offer(str4);
		// 2. 
		while(true) {
			val = myQueue.poll();
			if(val == null)
				break;
			else System.out.print(val+", ");
		}
		
		myQueue.offer(str1);
		myQueue.offer(str2);
		myQueue.offer(str3);
		myQueue.offer(str4);
		
		System.out.println();
		// 3.
		while(!myQueue.isEmpty()) {
			val = myQueue.poll(); // Queue의 head요소를 반환하고 제거한다.
			System.out.print(val+", ");
		}
		
		
		
	}

}
