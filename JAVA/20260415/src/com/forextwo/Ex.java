package com.forextwo;

public class Ex {
    public static void main(String[] args) {
        
        for (int i = 5; i >= 1; i--) {           // 줄 수 (5줄)
            // 공백 출력
            for (int j = 0; j < 5 - i; j++) {    // 앞에 공백 개수
                System.out.print(" ");
            }
            
            // 별 출력
            for (int j = 0; j < i; j++) {        // 별 개수
                System.out.print("*");
            }
            
            System.out.println();   // 한 줄 끝나면 줄 바꿈
        }
    }
}