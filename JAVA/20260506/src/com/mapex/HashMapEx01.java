package com.mapex;

/*		Map 인터페이스 -> HashMap, TreeMap
 * 		- Key와 Value를 매핑하는 객체이다.
 * 		- Key는 절대 중복될 수 없으며, 각 키는 1개의 값만 가질수 있다.
 * 		- 정렬의 기준은 없으며 Key로 값을 참조할 수 있다.
 * 
 * 		- 사용자가 원하는 값의 키를 알고 싶다면 키를 가지고 와서 해당 키와 
 * 		   매핑되어 있는 값을 얻어오는 구조임.
 * 			=> 검색을 Key로 해야하므로 키를 모르면 원하는 값을 얻지 못한다.
 */
import java.util.*;
public class HashMapEx01 {

	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<>();
		map.put("myId", "1234");
		map.put("asdf", "1111");
		map.put("asdf", "1234");
		System.out.println(map);
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			
			System.out.print("ID : ");
			String id = sc.nextLine().trim();
			System.out.print("PW : ");
			String pw = sc.nextLine().trim();
			
			if(!map.containsKey(id)) {
				System.out.println("입력하신 id는 존재하지않습니다.");
				continue;
			}
			if(!map.get(id).equals(pw)) {
				System.out.println("비밀번호가 일치하지않습니다.");
			}else {
				System.out.println("아이디와 비밀번호가 일치합니다.");
				break;
			}
			
		}
	}
}
