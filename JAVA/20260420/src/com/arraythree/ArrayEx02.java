package com.arraythree;

/*
 * 반 -> 2
 * 학생수 -> 3
 * 과목 -> 3
 * 
 * 
 */
import java.io.*;
public class ArrayEx02 {

	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] subname = {"국어","영어","수학"};
		int[][][] sub = new int [2][2][subname.length+3];
		// 0:국어, 1: 영어, 2: 영어, 3: 총점 4: 반석차, 5: 전교석차
		float[][] avg = new float[2][3];
		
		for(int i = 0; i < sub.length; i++) {// 반
			System.out.println((i+1)+"반");
			for(int j = 0; j < sub[i].length; j++) {// 학생
				System.out.println((j+1)+"째학생");
				for(int k = 0; k < sub[i][j].length-3; k++) {// 과목
					// 점수 입력받고, 총점구하고
					do {
						System.out.print(subname[k]+"점수 : ");
						sub[i][j][k] = Integer.parseInt(br.readLine());
					}while(sub[i][j][k] < 0 || sub[i][j][k] > 100);
					//총점 구하기
					sub[i][j][sub[i][j].length-3] += sub[i][j][k];
					
				}
				// 평균, 반석차 전체석차
				// 평균 
				avg[i][j] = sub[i][j][sub[i][j].length-3] / (float)sub[i][j].length-3;
				// 반석차 초기화
				sub[i][j][sub[i][j].length-2] = 1;
				// 전교석차 초기화
				sub[i][j][sub[i][j].length-1] = 1;
			}
			System.out.println();
		}
		// ===================== 반석차 계산 =====================
        for(int i = 0; i < sub.length; i++) {
            for(int j = 0; j < sub[i].length; j++) {
                for(int k = 0; k < sub[i].length; k++) {
                    if (sub[i][j][sub[i][j].length - 3] < sub[i][k][sub[i][k].length - 3]) {
                        sub[i][j][sub[i][j].length - 2]++;
                    }
                }
            }
        }

        // ===================== 전교석차 계산 =====================
        for(int i = 0; i < sub.length; i++) {
            for(int j = 0; j < sub[i].length; j++) {
                for(int ci = 0; ci < sub.length; ci++) {
                    for(int cj = 0; cj < sub[ci].length; cj++) {
                        if (sub[i][j][sub[i][j].length - 3] < sub[ci][cj][sub[ci][cj].length - 3]) {
                            sub[i][j][sub[i][j].length - 1]++;
                        }
                    }
                }
            }
        }
		
		//결과 출력
		for(int i = 0; i < sub.length; i++) {
			for(int j = 0; j < sub[i].length;j++) {
				System.out.println();
				System.out.println("총점 : "+sub[i][j][sub[i][j].length-3]);
				System.out.println("평균 : "+avg[i][j]);
				System.out.println("반석차 : "+sub[i][j][sub[i][j].length-2]+"등");
				System.out.println("전교 석차 : "+sub[i][j][sub[i][j].length-1]+"등");
				}
			}
		}
	}
