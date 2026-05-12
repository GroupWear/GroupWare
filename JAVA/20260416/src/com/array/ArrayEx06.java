package com.array;

/*		문]
 * 			인원수를 입력받아 인원수만큼 학생이름, 전화번호를 입력받고 
 * 			배열에 저장하여 출력하는 프로그램을 구현하시오.
 * 	
 * 			입력
 * 			학생 수 :3
 * 			이름 전번 입력(1) : 가길동 010-1111-1111 
 * 			이름 전번 입력(2) : 가길동 010-1111-1111
 * 			이름 전번 입력(3) : 가길동 010-1111-1111
 * 
 * 			출력 
 * 			------------------------------------
 * 			전체 학생 수 : 3 
 * 			이름 		전번
 * 			가길동		010-1111-1111
 * 			가길동		010-1111-1111
 * 			가길동		010-1111-1111
 */
import java.util.*;
public class ArrayEx06 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.print("학생 수 : ");
		int n1 = sc.nextInt();
		sc.nextLine();
		String[] name= new String[n1];
		String[] number = new String[n1];
		
		
		for(n1 = 0; n1 < name.length;n1++) {
			System.out.printf("이름 전번 입력(%d) : ", (n1+1));
			name[n1] = sc.next();
			number[n1] = sc.next();
			sc.nextLine();
		}
		System.out.println("전체 학생 수 : "+name.length);
		System.out.println("이름		전번:");
		for(int i = 0; i < name.length; i++) {
			System.out.println(name[i]+" "+number[i]);
		}
	}

}
