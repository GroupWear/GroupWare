package com.arraytwoex;

import java.io.*;

public class ArraytwoEx10two {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String [] subname = {"국어", "영어", "수학"};
		int[][] sub = new int[3][subname.length+2];// length란 배열안에 있는 개수를 정수값으로 나타내줌. 
		// 0: 국어, 1: 영어, 2: 수학, 3:합계, 4: 석차
		
		float[] avg = new float[3];
		
		for(int k = 0; k < sub.length; k++) { // 사람 : 행 -> 3
			for(int i = 0; i < sub[k].length-2; i++) {// 0: 국어, 1: 영어, 2: 수학, 3:합계, 4: 석차 
				do {
					System.out.print(subname[i]+" 점수 : ");
					sub[k][i] = Integer.parseInt(br.readLine());
				}while(sub[k][i] < 0 || sub[k][i] > 100);// 해당 범위 안에 들어가면 다시 do로 돌아가 sysout부터시작.
				//한 사람당 과목별 점수의 합계를 구함
				sub[k][sub[k].length-2] += sub[k][i];
			}
			//사람별 평균을 구함
			avg[k] = sub[k][sub[k].length-2] / (sub[k].length-1);
			//석차를 초기화함.
			sub[k][sub[k].length-1] = 1;
		}
		// 석차 구하기
		for(int k = 0; k < sub.length; k++) {
			for(int i = 0; i < sub.length; i++) {
				if(sub[k][sub[k].length-2] < sub[i][sub[i].length-2]) { // 
					sub[k][sub[k].length-1]++;
				}
			}
		}
		
		//결과
		for(int k = 0; k < sub.length; k++) {
			System.out.println();
			System.out.println("총점 : "+sub[k][sub[k].length-2]);
			System.out.println("평균 : "+avg[k]);
			System.out.println("석차 : "+sub[k][sub[k].length-1]);
		}
		

	}

}
