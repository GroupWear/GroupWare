package com.card;

public class Deck {
	
	
	final int CARD_NUM = 52;
	Card cardArr[] = new Card[CARD_NUM]; // Card 객체배열 
	
	public Deck() { // deck의 카드 초기화
		int i = 0;
		for(int k = Card.KIND_MAX; k > 0; k--) {
			for(int n = 0; n < Card.NUM_MAX; n++) {
				cardArr[i++] = new Card(k,n+1);
			}
		}
	}
	Card pick(int index) {//index에 있는 카드 하나를 반환
		return cardArr[index];
	}
	
	Card pick() {// deck에서 카드하나를 랜덤으로 선택
		int index = (int)(Math.random() * CARD_NUM);
		return pick(index);
	}
	
	void shuffle() {// 카드 섞기
		for(int i = 0; i < cardArr.length; i++) {
			int r = (int)(Math.random() * CARD_NUM);
			Card temp = cardArr[i];
			cardArr[i] =  cardArr[r];
			cardArr[r] = temp;
		}
	}
	
	
	
}
