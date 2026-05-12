package com.game;
import java.util.*;
public class WordGameApp {
	
	private Player[] player;
	private Scanner sc;
	private String startWord = "아버지";
	
	public WordGameApp() {
		sc = new Scanner(System.in);
	}
	
	public void Players() {
		System.out.println("게임 참가자 인원수 : ");
		int num = sc.nextInt();
		player = new Player[num];
		String name;
		for(int i =0; i < player.length; i++) {
			System.out.print("참가자 이름 : ");
			name =  sc.next();
			player[i] = new Player(name);
		}
			sc = new Scanner(System.in);
	}
	public void run() 	{
		System.out.println("끝말잇기를 시작합니다.");
		// 게임 메소드 참가자 호출
		Players();
		//시작 단어 출력
		String lastWord = startWord;
		System.out.println("시작단어는 "+lastWord+"입니다.");
		int num = 0; // 다음 참가자를 위해 증가시키는 변수 선언
		while(true) {
			//다음참가자가 입력한 새로운 단어
			String newWord = player[num].getWordFromUser();
				
			//참가자 입력한 단어가 맞는지를 판단하여 게임을 계속 진행할 것인지 아니면 그만 할 것인지를 결정
			if(!player[num].checkSuccess(lastWord)) {
				// 다음 참가자가 입력단어가 맞지 않을 경우 => 게임을 중지하고 게임참가자의 이름을 출력한다.
				System.out.print(player[num].getName()+"게임에서 지졌습니다.");
				break; //게임끝
			}
			//다음 참가자
			num++;
			
			num %= player.length;
			lastWord = newWord;
		}
	}
	public static void main(String[] args) {
		new WordGameApp().run();
	}
}


