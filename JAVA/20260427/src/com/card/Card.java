package com.card;

public class Card {

	static final int KIND_MAX = 4; // 카드의 종류 
	static final int NUM_MAX = 13; //카드 한종류의 개수
	
	
	// 무늬별 순위
	static final int SPADE = 4;
	static final int DIAMOND = 3;
	static final int HEART = 2;
	static final int CLOVER = 1;
	
	int kind;
	int number;
	
	public Card() {
		
	}
	public Card(int kind, int number) {
		this.kind = kind;
		this.number = number;
	}
	
	@Override // Object ->  object 클래스가 가지고 있는 메소드
	//							    -> 클래스의 모든 문자열을 반환해주는 메소드 
	public String toString() {
		String[] kinds = {"", "CLOVER", "HEART","DIAMOND","SPADE"};
		String numbers = "0123456789XJQK"; //숫자 10은 X로 표현
		
		return "kind : "+kinds[this.kind]+", numbers : "+numbers.charAt(this.number);
	}
	
	
	
	
}
