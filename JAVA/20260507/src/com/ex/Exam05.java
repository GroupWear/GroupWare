package com.ex;
import java.util.*;
public class Exam05 {

    public static void print(ArrayList<Student> a) {
        for (Student s : a) {
            System.out.println("-------------------------------------");
            System.out.println("이름 : " + s.getName());
            System.out.println("학과 : " + s.getGwa());
            System.out.println("학번 : " + s.getNum());
            System.out.println("학점평균 : " + s.getAvg());
        }
    }
   
        
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ArrayList<Student> a = new ArrayList<>();
		String name = "0";
		System.out.println("학생 이름, 학과 ,학번, 학점 평균을 입력하세요.");
		for(int i = 0; i < 4; i++) {
		    System.out.print("이름 학과 학번 학점 : ");
		    name = sc.next();
		    
		    if(name.equals("그만")) {
		    	System.out.println("종료합니다.");
		    	return;
		    }
		    String gwa = sc.next();
		    int num = sc.nextInt();
		    double avg = sc.nextDouble();
		    a.add(new Student(name, gwa, num, avg));
		}
		print(a);
		System.out.print("학생 이름 : ");
		name = sc.next();
		for (Student s : a) {
			if(!name.equals(s.getName())) {
			 	System.out.println("없는 학생입니다.");
			 	break;
			 }else 
			 if(name.equals(s.getName())) {
				System.out.println(s.getName()+" "+s.getGwa()+" "+s.getNum()+" "+s.getAvg());
			 	break;
			 	}
		   	
		}
		
	}
}
