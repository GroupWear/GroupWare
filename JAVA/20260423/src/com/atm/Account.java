package com.atm;


public class Account {
	
	// 소유주(계좌 주)
	private String name;
	// 잔액(금액) 
	private long balance;
	
	public Account() {
	}
	public Account(String name) {
		this.name = name;
	}
	public Account(String name, long balance) {
		this.name = name;
		this.balance = balance;
	}
	

	// 현재 객체가 가지고 있는 멤버 필드에 접근하려면 
	// 메소드를 통해서만이 접근이 가능하다.
	public String getName() {
		return name;
	}
	public long getBalance() {
		return balance;
	}
	
	
	// 입금 처리 메소드 정의 
	public void deposit(long amount) {
		balance += amount;
	}
	// 출금 처리 메소드 정의
	public void withdraw(long amount) {
		if(balance < amount) {
			// 출금하고자하는 금액이 현재 잔고의 잔액보다 큰 경우.
			System.out.println("현재 계좌의 잔액이 부족하여 출금이 불가합니다.");
		}else {
			// 출금하고자하는 금액이 현재 잔고의 잔액보다 작은 경우.
			balance -= amount;
		}
	}
	
	
}
