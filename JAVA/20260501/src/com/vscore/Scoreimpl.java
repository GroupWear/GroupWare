package com.vscore;
import java.util.*;
public class Scoreimpl implements Score {

	
	private Vector<ScoreVO> list = new Vector<ScoreVO>();
	private Scanner sc = new Scanner(System.in);
	
	
	@Override
	public void insert() {
		System.out.println(".........자료 추가........");
		String hak;
		System.out.print("학번 : ");
		hak = sc.next();
		
		ScoreVO temp = readScore(hak); // 입력한 학번이 list안에 있는지 없는지 판정함. 
		if(temp != null) { // 학번이 이미 존재한다면
			System.out.println("이미 등록된 학번입니다.");
			return;
		}// 학번이 존재하지 않으면
		ScoreVO vo = new ScoreVO();
		vo.setHak(hak);
		System.out.print("이름 : ");
		vo.setName(sc.next());
		System.out.print("생년월일 : ");
		vo.setBirth(sc.next());
		System.out.print("국어 : ");
		vo.setKor(sc.nextInt());
		System.out.print("영어 : ");
		vo.setEng(sc.nextInt());
		System.out.print("수학 : ");
		vo.setMat(sc.nextInt());
		
		list.add(vo);
		System.out.println("데이터 등록이 완료 되었습니다. .....");
	}

	@Override
	public void update() {
			System.out.println(".........자료 수정........");
			String hak;
			System.out.print("수정할 학번 : ");
			hak = sc.next();
			
			ScoreVO vo = readScore(hak); // 입력한 학번이 list안에 있는지 없는지 판정함. 
			if(vo == null) { // 학번이 이미 존재한다면
				System.out.println("입력하신 학번이 존재하지 않습니다.");
				return;
			}
			//학번이 존재하지않으면
			System.out.print("이름 : ");
			vo.setName(sc.next());
			System.out.print("생년월일 : ");
			vo.setBirth(sc.next());
			System.out.print("국어 : ");
			vo.setKor(sc.nextInt());
			System.out.print("영어 : ");
			vo.setEng(sc.nextInt());
			System.out.print("수학 : ");
			vo.setMat(sc.nextInt());
			
			list.add(vo);
			System.out.println("데이터 수정이 완료 되었습니다. .....");
		}

	@Override
	public void delete() {
		System.out.println(".........자료 삭제........");
		String hak;
		System.out.print("삭제할 학번 : ");
		hak = sc.next();
		
		ScoreVO vo = readScore(hak); // 입력한 학번이 list안에 있는지 없는지 판정함. 
		if(vo == null) { // 학번이 이미 존재한다면
			System.out.println("입력하신 학번이 존재하지 않습니다.");
			return;
		}
		list.remove(vo);
		System.out.println("데이터 삭제가 완료 되었습니다. .....");
	}

	@Override
	public void listAll() {
		System.out.println("\n 전체 출력 .............");
		Iterator<ScoreVO> it = list.iterator();
		
		while(it.hasNext()) {
			ScoreVO vo = it.next();
			System.out.println(vo.toString());
		}
		System.out.println();
	}

	@Override
	public void searchHak() {
		System.out.println(".........학번 검색........");
		String hak;
		System.out.print("검색할 학번 : ");
		hak = sc.next();
		
		ScoreVO vo = readScore(hak); // 입력한 학번이 list안에 있는지 없는지 판정함. 
		if(vo != null) { 
			System.out.println(vo.toString());
		}
		else System.out.println("없는 학번입니다.");
		System.out.println();
		
		
	}

	@Override
	public void searchname() {
		System.out.println("........이름 검색........");
		String name;
		System.out.print("검색할 이름 : ");
		name = sc.next();
		
		for(ScoreVO vo : list) {
			if(vo.getName().startsWith(name)) {
				System.out.println(vo);
			}
		}
		System.out.println();
	}

	public ScoreVO readScore(String hak) {
		ScoreVO vo = null;
		
		for(ScoreVO temp : list) {
			if(temp.getHak().equals(hak)) {
				vo = temp;
				break;
			}
		}
		return vo;
	}
	
	
	
	
}
