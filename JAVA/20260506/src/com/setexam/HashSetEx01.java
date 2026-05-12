package com.setexam;

/*		set -> 인터페이스 -> 구현클래스 : HashSet, TreeSet
 *		- 객체들은 특별한 기준에 맞추어 정렬되지는 않는다.
 * 		- 저장되는 객체들간의 중복된 데이터가 발생하게 되는 것을 내부적으로 이를 허용하지 않는다.
 * 		- 순서가 없다.
 * 		- 동일한 데이터는 허용하지않는다.(중복 불가)
 * 
 * 		HashSet
 * 			- 특정한 기준으로 정렬되지 않아서 저장 및 검출과 같은 동작에는 일정한 시간을 필요로한다.
 * 
 * 		TreeSet 
 * 			- 기본적으로 오름차순으로 정렬되어 있기 때문에, 검색시간이 빠르다.
 */
import java.util.*;
public class HashSetEx01 {

	public static void main(String[] args) {
		Object[] objArr	=	{"1", new Integer(1), "2", "2", "3", "3", "4","4","4"};
		
		Set se = new HashSet<>();
		for(int i = 0; i < objArr.length;i++) {
			se.add(objArr[i]);
		}
		System.out.println(se);
		
	}
}
