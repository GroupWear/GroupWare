package com.array;

/*		문]
 * 			char 자료형의 배열에 알파벳 대문자를 저장하고
 * 			저장된 배열의 전체 값을 출력하는 프로그램을 구현하시오.
 * 
 * 			
 * 
 */
public class ArrayEx05 {

	public static void main(String[] args) {
		
		/*		알파벳 대문자를 배열에 저장
		 * 		ABCDEFGHIJKLMNOPQRSTUVWXYZ
		 * 		1.
		 */
		char[] arr1 = new char[26];
		arr1[0]='A';
		arr1[1]='B';
		arr1[2]='C';
		arr1[3]='D';
		arr1[4]='E';
		arr1[5]='F';
		arr1[6]='G';
		arr1[7]='H';
		arr1[8]='I';
		arr1[9]='J';
		arr1[10]='K';
		arr1[11]='L';
		arr1[12]='M';
		arr1[13]='N';
		arr1[14]='O';
		arr1[15]='P';
		arr1[16]='Q';
		arr1[17]='R';
		arr1[18]='S';
		arr1[19]='T';
		arr1[20]='U';
		arr1[21]='V';
		arr1[22]='W';
		arr1[23]='X';
		arr1[24]='Y';
		arr1[25]='Z';
		for(int i = 0; i < arr1.length;i++) {
			System.out.print(arr1[i]+" ");
		}
		System.out.println();
		System.out.println();
		char[] arr2 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		for(int i =0; i < arr2.length;i++) {
			System.out.print(arr2[i]+" ");
		}
		System.out.println();
		System.out.println();
		char[] arr3 = new char[26];
		for(int i =0; i < arr3.length;i++) {
			arr3[i] =(char)('A'+i);// 배열에 저장
			System.out.print(arr3[i]+" ");// 배열에 저장된 값 출력
		}
		System.out.println();
		System.out.println();
		for(int i =0; i < arr3.length;i++) {
			System.out.print(arr3[i]+" ");
		}
	}

}
