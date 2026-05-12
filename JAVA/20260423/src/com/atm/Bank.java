package com.atm;
import java.io.*;
public class Bank {
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 계좌 객체 생성(이름을 매개변수로 가지고)
		Account na = new Account("나");
		
		String strWork;
		
		do {
			System.out.println("\n\n 어떤 업무를 하시겠습니까?");
			System.out.println("============================");
			System.out.println("  입				금		   ====> 1");
			System.out.println("  출				금		   ====> 2");
			System.out.println("  잔		액		조		회 ====> 3");
			System.out.println("  종				료		   ====> 0");
			System.out.println("============================");
			System.out.println("업	무	선	택 : ");
			strWork = br.readLine();
			
			int n = 0;
			if(strWork != null) {
				n = Integer.parseInt(strWork);
			}else {
				System.out.println("작업을 선택하지 않았습니다.다시 입력해주세요.");
				System.exit(0);
			}
			switch(n) {
			case 1:
				System.out.println("=================");
				System.out.print("입금 할 금액을 작성해주세요 : ");
				String amount = br.readLine();
				long desposit = Long.parseLong(amount);
				na.deposit(desposit);
				System.out.println("=================");
				System.out.println("입금 한 금액 : "+desposit+"총 금액 : "+na.getBalance());
				break;
			case 2:
				long withdrawout = 0;
				do {
				System.out.println("=================");
				System.out.print("출금 할 금액을 작성해주세요 : ");
				String withdraw = br.readLine();
				withdrawout = Long.parseLong(withdraw);
				na.withdraw(withdrawout);
				}while(withdrawout > na.getBalance());
				System.out.println("=================");
				System.out.println("입금 한 금액 : "+withdrawout+"총 금액 : "+na.getBalance());
				System.out.println("====================");
				break;
			case 3:
				System.out.println(na.getName()+"님의 현재 계좌의 잔액은 "+na.getBalance()+"원 입니다.");
				break;
			default : 
				System.out.println("업	무는 1~3까지의 숫자만 입력 가능합니다.");
				break;
			}
		}while(!strWork.equals("0"));
		
		
		
		
		
	}

}
