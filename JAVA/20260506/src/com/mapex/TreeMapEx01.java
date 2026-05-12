package com.mapex;
import java.util.*;

/*		TreeMap
 * 		- 정렬된 상태로 데이터를 추가함
 * 		- SortedMap<K,V> subMap (K fromKey, K toKey)
 * 		   부분데이터를 추출할 수 있다.
 * 		   fromKey : 키를 포함하고 
 * 		   toKey : 키를 포함하지 않는다.
 */

public class TreeMapEx01 {

	public static void main(String[] args) {
		
		TreeMap<String, String> map = new TreeMap<>();
		map.put("만화", "마녀배달부 키키");
		map.put("호러", "스크림");
		map.put("영화", "황혼에서 새벽까지");
		map.put("방화", "쉬리");
		map.put("TV", "X-Files");
		
		System.out.println(map);
		
		Map<String, String> subMap = map.subMap("방화", "호러");
		System.out.println(subMap);
		
		
		
		
	}
}
