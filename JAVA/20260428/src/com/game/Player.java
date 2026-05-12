package com.game;
import java.util.*;
public class Player {

	private Scanner sc;
	private String name;
	private String word;	
	
	public Player(String name) {
		this.name = name;
		sc = new Scanner(System.in);
	}
	public String getName() {
		return name;
	}
	public String getWord() {
		return word;
	}
	// 사용자로부터 단어를 입력받는 단어 입력
	public String getWordFromUser() {
		System.out.print(name+" : ");
		word = sc.next();
		return word;
	}
	
	// 마지막 단어와 참가자가 입력한 단어를 비교하여 끝말잇기가 됐는지를 
	// 판정하고 맞으면 ture, 틀리면 false를 반환하느 메소드
	public boolean checkSuccess(String lastword) {
		// 입력 받은 단어의 마지막 문자 추출
		int lastIndex = lastword.length()-1;
		
		// 마지막 단어와 단어의 첫문자가 같은지를 비교 판단함
		// 마지막 단어의 맨 마지막 문자와 다음 참가자가 입력한 단어의 첫 문자가 같은지를 비교판단
		if(lastword.charAt(lastIndex) == word.charAt(0)) { // 문자가 같으면 참
			return true;
		}else {
			return false;
		}
	}
	
}
