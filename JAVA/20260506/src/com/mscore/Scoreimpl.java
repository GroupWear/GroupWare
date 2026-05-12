package com.mscore;
import java.util.*;
public class Scoreimpl implements Score {

	
	private Scanner sc = new Scanner(System.in);
	
	//HashMap : 키가 정렬 되지 않는다.
//	private Map<String, ScoreVO> map = new HashMap<>();
	//TreeMap : 키값으로 정렬됨
	private Map<String, ScoreVO> map = new TreeMap<>();
	@Override
	public void insert() {
		System.out.println(".........자료 추가........");
		String hak;
		System.out.print("학번 : ");
		hak = sc.next();
		
		if(map.containsKey(hak)) {
			System.out.println("이미 등록된 학번이 있습니다. \n");
			return;
		}
		
		// 학번이 존재하지 않으면
		ScoreVO vo = new ScoreVO();
		
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
		
		map.put(hak, vo);
		System.out.println("데이터 등록이 완료 되었습니다. .....");
	}

	@Override
	public void update() {
		System.out.println(".........자료 수정........");
		String hak;
		System.out.print("수정할 학번 : ");
		hak = sc.next();
		
		if(!map.containsKey(hak)) {
			System.out.println("등록된 학번이 없습니다. \n");
			return;
		}
		
		// 학번이 존재하면
		ScoreVO vo = map.get(hak);
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
		
		System.out.println("데이터 수정이 완료 되었습니다. .....");
		}

	@Override
	public void delete() {
		System.out.println(".........자료 삭제........");
		String hak;
		System.out.print("삭제할 학번 : ");
		hak = sc.next();
		
		if(!map.containsKey(hak)) {
			System.out.println("등록된 학번이 없습니다. \n");
			return;
		}
		map.remove(hak);
		
		System.out.println("데이터 삭제가 완료 되었습니다. .....");
	}

	@Override
	public void listAll() {
		System.out.println("\n 전체 출력 .............");
		Iterator<String> it = map.keySet().iterator();
		
		while(it.hasNext()) {
			String hak = it.next();
			ScoreVO vo = map.get(hak);
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
		
		if(!map.containsKey(hak)) {
			System.out.println("등록된 학번이 없습니다. \n");
			return;
		}
		
		ScoreVO vo = map.get(hak); // 입력한 학번이 list안에 있는지 없는지 판정함. 
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
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String hak = it.next();
			ScoreVO vo = map.get(hak);

				System.out.println(vo);
			
		}
		System.out.println();
	}
	
}
