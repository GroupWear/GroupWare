package com.swtichex;

/*	문]
 * 		주민등록 번호를 입력받아 성별을 판별하는 프로그램을 구현하시오.
 * 
 * 		남 : 1,3
 * 		여 : 2,4
 */
import java.util.*;
public class SwitchEx06 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("주민번호를 입력해주세요 : ");
		String jumin = sc.nextLine();
		char gender = jumin.charAt(7);// 000101-0101011
		
		
		switch (gender) {
		case '1': case '3':
			switch (gender) {
			case '1':
				System.out.println("2000년 이전에 태어난 남성입니다.");
				return;
			}
			System.out.println("2001년 이후에 태어난 남성입니다.");
			break;
		case '2': case '4':
			switch (gender) {
			case 2:
				System.out.println("2000년 이전에 태어난 남성입니다.");
				break;
			case '4':
				System.out.println("2001년 이후에 태어난 여성입니다.");
				break;
			}
		default:System.out.println("오류");
			return;
			
		}
		
		
	}

}
