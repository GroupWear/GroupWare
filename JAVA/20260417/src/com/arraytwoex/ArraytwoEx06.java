package com.arraytwoex;

/*		문]
 * 			주민번호를 입력받아 올바른 주민번호인지를 검증하는 프로그램을 구현하시오.
 * 
 * 			1 2 3 4 5 6 - a b c d e f g
 * 
 * 			12 => 태어난 년도.
 * 			34 => 태어난 월
 * 			56 => 일.
 * 			a => 성별( 1901 -> 1,2 | 2001 -> 3,4) 남 : 1,3 여 : 2,4
 * 			b => 주민번호를 등록한 지역
 * 			c, d => (시,구), (도, 군)을 의미
 * 			e,f => 동, 읍, 면을 의미함.
 * 			g =>	주민번호가 올바른지를 판별하는 숫자를 의미함.
 * 
 * 			마지막 숫자 g를 판별하는 공식
 * 			hap = 0;
 * 			hap =+ 주민번호 첫번째 자리(1) *2
 * 			hap =+ 주민번호 두번째 자리(2) *3
 * 			hap =+ 주민번호 세번째 자리(3) *4
 * 			hap =+ 주민번호 네번째 자리(4) *5
 * 			hap =+ 주민번호 다섯번째 자리(5) *6
 *  		hap =+ 주민번호 여섯번째 자리(6) *7
 *  		 - 			: 계산에서 제외
 *   		hap =+ 주민번호 여덟번째 자리(a) *8
 *   		hap =+ 주민번호 아홉번째 자리(b) *9
 *   		hap =+ 주민번호 열번째 자리  (c) *2
 *   		hap =+ 주민번호 열한번째 자리(d) *3
 *   		hap =+ 주민번호 열둘째 자리  (e) *4
 *   		hap =+ 주민번호 열세번째 자리(f) *5
 *   		
 *   		temp = (int)(hap / 11.0f) * 11.0f +11.0f - hap;
 *  		temp1 = temp - (int)(temp/10.0f) * 10.0f;
 */

import java.io.*;
import java.util.Calendar;
public class ArraytwoEx06 {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// 변수와 배열 선언
		String name = "";
		int jumin[] = new int[14];
		boolean right = false;
		
		// 이름을 입력 받음
		System.out.print("이름 : ");
		name = br.readLine();
		
		do {
			right = false;
			System.out.print("주민번호 입력(111111-1111111) : ");
			
			for(int i = 0; i < jumin.length;i++) {
				jumin[i] = System.in.read()-48;
			}
			System.in.skip(2);
			
			// 월의 범위 체크l
			if(jumin[2] * 10 + jumin[3] > 12) {
				System.out.println("태어난 달은 12월 보다 클 수 없습니다.");
				continue;
			}else 
				if(jumin[4] *10 + jumin[5] > 31) {// 일의 범위 체크
				System.out.println("태어난 일은 31일 보다 클 수 없습니다.");
				continue;
			}else 
				if(jumin[7] != 9 && jumin[7] != 0 && jumin[7] != 1 && jumin[7] != 2 && jumin[7] != 3 && jumin[7] == 4) {// 성별 체크
				System.out.println("성별을 나타내는 숫자는 9,0,1,2,3,4 중 하나이어야합니다.");
				continue;
			}
			
			// 주민번호가 올바른지를 판별한다.
			int hap = 0;
			int cre = 2;
			float temp = 0.0f, temp1 = 0.0f;
			
			for(int i = 0; i < jumin.length-1 ;i++) {
				if(i == 6) {
					continue;
				}
				hap += jumin[i] * cre;
				cre++;
				if(cre == 10) {
					cre = 2;
				}
			}
			temp = (int)(hap / 11.0f) * 11.0f +11.0f - hap;
			temp1 = temp - (int)(temp/10.0f) * 10.0f;
			
			if(temp1 != jumin[13]) {
				System.out.println("주민번호 검증 숫자가 올바르지 않습니다.");
				continue;
			}
			right = true;
		}while(!right);
		
		System.out.println("이름 : "+name);
		System.out.print("주민번호 : ");
		for(int i =0; i < jumin.length;i++) {
			if(i==6) {
				System.out.print("-");
				continue;
			}
			// jumin 배열에 저장된 주민번호를 출력한다.
			System.out.print(jumin[i]);
		}
		System.out.println();
		
		// 생년월일
		System.out.print("생년월일 : ");
		int year = 0, month = 0, day= 0;
		
		//태어난 년대를 추출함
		switch (jumin[7]) {
		case 9:
		case 0:
			year = 1800;
			break;
		case 1:
		case 2:
			year = 1900;
			break;
		case 3:
		case 4:
			year = 2000;
			break;
			
		default:
			break;
		}
		//년
		year += jumin[0] * 10 + jumin[1];
		//월
		month += jumin[2] * 10 + jumin[3];
		//일
		day += jumin[4] *10 + jumin[5];
		
		System.out.printf("%d %d %d%n",year,month,day);
		System.out.print("성별 : ");
		System.out.println(jumin[7] % 2 == 0 ? "여성":"남성");
		System.out.print("태어난 시도 : ");
		String area ="";
		switch (jumin[8]) {
		case 0:
			area = "서울";
			break;
		case 1:
			area = "경기, 인천";
			break;
		case 2:
			area = "부산";
			break;
		case 3:
			area = "강원도";
			break;
		case 4:
			area = "충청도";
			break;
		case 5:
			area = "전라도";
			break;
		case 6:
			area = "대구, 광주";
			break;
		case 7:
			area = "경북,경남";
			break;
		case 8:
			area = "경남";
			break;
		case 9:
				area = "제주도";
				break;
		}
		
		
		System.out.println(area);
		System.out.print("나이 : ");
		Calendar ca = Calendar.getInstance();
		int age = ca.get(Calendar.YEAR)-year+1;
		System.out.print(age+"세");
		
	}// end main

}//end class
