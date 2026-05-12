package com.arraythree;

/*
 * 배열의 복사
 *		System.arraycopy() 메소드를 이용하여 복사
 * 
 * 		메소드 형식
 * 
 * 		public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
 * 
 * 				src : 원본배열(소스배열)
 * 				srcPos : 원본배열의 시작위치(인덱스)
 * 				dest : 복사될 배열(복사본)
 * 				destPost : 복사 시작 위치
 * 				length : 복사되는 배열의 인덱스 수 
 * 				
 */



public class ArrayCopyEx02 {

	public static void main(String[] args) {
		String[] src = {"Java", "Database", "Jsp", "Network"};
		
		
		String[] dest = new String[6];
		
		dest[0] = "Spring";
		dest[1] = "Python";
		
		System.arraycopy(src, 0, dest, 2, 4) ;
		
		
		for(String s : dest) {
			System.out.println(s);
		}
		
	}

}
