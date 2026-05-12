package com.classex;

/*		call by reference -> 참조에 의한 호출
 * 		 - 메소드 호출시 전달하려는 인자를 참조(객체, 배열) 자료형을 사용할 경우를 의미한다.
 * 		    기본 자료형이 아닌 일반 자바의 객체 또는 자바 배열이 여기에 속함
 */
public class ReferenceParameter {
		
		public void increase(int[] n) {
			for(int i = 0; i < n.length;i++){
				n[i]++;
			}
		}

	public static void main(String[] args) {
		int[] ref = {100, 800, 1000};
		
		ReferenceParameter rp = new ReferenceParameter();
		rp.increase(ref);
		
		for(int i = 0; i < ref.length; i++) {
			System.out.println("ref["+i+"] : "+ref[i]);
		}
		
		
	}

}
