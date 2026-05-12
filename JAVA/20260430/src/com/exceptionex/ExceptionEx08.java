package com.exceptionex;

import java.io.File;
import java.util.*;

public class ExceptionEx08 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("파일이름 입력 :");
		String ff = sc.next();
		try {
			File f = createFile(args[0]);
			System.out.println(f.getName()+"파일이 성공적으로 생성되었습니다.");
		} catch (Exception e) {
			System.out.println(e.getMessage()+"다시 입력해주세요.");
		}
		
		static File createFile(String fileName) throws Exception {

			if (fileName == null || fileName.equals(""))
				throw new Exception("파일이름이 유효하지않습니다.");

		//	fileName = "제목없음.txt";

			File f = new File(fileName);
			f.createNewFile();
			return f;
		}
	}

	private static File createFile(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
