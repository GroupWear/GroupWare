package com.buyer;

/*		고객이 제품을 구매(buy 메소드를 이용하여 TV, Computer, Audio)하여 
 * 		고객의 잔고와 보너스 점수를 출력하는 프로그램
 */
import java.util.*;

class Product {
	int price; // 제품의 가격
	int bonusPoint; // 제품을 샀을때 주는 보너스포인트
	
	public Product(int price) {
		this.price = price;
		bonusPoint = (int)(price/10.0); // 제품의 10%가 보너스점수
	}
	public Product() {
//		price = 0;
//		bonusPoint 0;
	}
}
class TV extends Product{
	
	public TV() {
		// 부모 클래스의 생성자를 호출
		super(100); // 제품가격을 100만원으로 한다.
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TV";
	}
	
}
class Computer extends Product{
	public Computer() {
		super(200);
	}
	@Override
	public String toString() {
		return "Computer";
	}
}
class Audio extends Product{
	public Audio() {
		super(50);
	}
	@Override
	public String toString() {
		return "Audio";
	}
}
//제품을 사는 사람(구매자)
class Buyer {
	int money; // 고객의 현재 소유 금액
	int bonusPoint = 0; // 보너스 점수( 초기화)
//	Product[] item = new Product[10];// 구매한 제품을 저장하기 위한 배열
//	구입한 제품을 저장하기위한 Vector 객체 생성
	Vector item = new Vector();
	
	
	
	int i = 0;
	
	public Buyer(int x) {
		this.money = x;
		System.out.println("현재 소유중인 금액은 "+money+"만원 입니다.");
	}
	
	void buy(Product p) {
		if(money < p.price ) {
			System.out.println("돈이 부족합니다.");
			return;
		}
		money -= p.price; // 현재 소유한 금액에서 제품가격을 -
		bonusPoint += p.bonusPoint;// 제품의 보너스 점수를 추가한다.
		//item[i++] = p; // 제품을 Product 배열 item에 저장한다.
		item.add(p);
		
		System.out.println(p+"을/를 구입하셨습니다.");
	}
	//환불기능
	void refund(Product p) { // 구입한 제품을 환불처리
		if(item.remove(p)) {
			money += p.price;
			bonusPoint -= p.bonusPoint;
			System.out.println(p+"가 환불되었습니다.");
			System.out.println("현재 금액은 "+money+"만원 이고, 마일리지는 "+bonusPoint+"남았습니다.");
		}
	}
	
	//구입한 제품에 대한 정보를 요약해서 보여줄 기능
	void summary() {
		
		 int sum = 0;// 총 구매 가격
		 String itemList="";// 구입한 제품 목록
		 
		 // Vector가 비어 있는지 확인
		 if(item.isEmpty()) {
			 System.out.println("구입하신 제품이 없습니다.");
			 return;
		 }
		 
		 //반복문을 이용해서 구입한 제품의 총 가격과 목록을 만듬
		 for(int i = 0; i < item.size();i++) {
			 Product p = (Product)item.get(i); // 다운캐스팅
			 sum += p.price;
			 itemList += (i==0) ? ""+p:", "+p;
		 }
		 
		 //반복문을 이용해서 구입한 제품을 총합계와 목록을 만듬
//		 for(int i = 0; i < item.length; i++) {
//			 if(item[i] == null)break;
//			 sum += item[i].price;
//			//  itemList += item[i]+", ";
//			 itemList += (i==0) ? ""+item[i]:", "+item[i];
//		 }
		 System.out.println("구입하신 물품의 총금액은 "+sum+"만원 입니다.");
		 System.out.println("구입하신 제품은 "+itemList+"입니다.");
		 System.out.println("남은 돈은 "+money+"만원 입니다.");
	}
}

public class PolymorphismEx01 {

	public static void main(String[] args) {

		Buyer b = new Buyer(800);
		
		b.buy(new TV());
		b.buy(new Computer());
		
		System.out.println("현재 남은 돈은 "+b.money+"만원 입니다.");
		System.out.println("현재 보너스 점수는 : "+b.bonusPoint+"점 입니다.");
		
		
	}

}
