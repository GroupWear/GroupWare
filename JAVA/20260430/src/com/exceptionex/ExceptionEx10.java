package com.exceptionex;





public class ExceptionEx10 {
	// 프로그램 설치 시작
	static void startInstall() throws SpaceException, MemoryException {
		if (enoughSpace()) {
			throw new SpaceException("설치할 공간이 부족합니다.");
		}
		if (!enoughMemory()) {
			throw new MemoryException("메모리 공간이 부족합니다");
		}
	}

	static void copyFiles() {
	} // 파일복사

	static void deletedTempFiles() {
	} // 임시파일삭제

	static boolean enoughSpace() { // 설치하는 데 필요한 공간이 있는 지를 판단하는 코드
		return false;
	}

	static boolean enoughMemory() { // 설치하는데 필요한 메모리 공간이 있는지를 판단하는 코드
		return false;
	}

	public static void main(String[] args) {
		try {
			startInstall();
			copyFiles();
		} catch (SpaceException se) {
			System.out.println("에러 메시지 : " + se.getMessage());
			se.printStackTrace();
			System.out.println("공간을 확보하신 후 다시 설치하시기 바랍니다.");
		} catch (MemoryException me) {
			System.out.println("에러 메시지 : " + me.getMessage());
			me.printStackTrace();
			System.gc();
			System.out.println("재설치바람.");
		} finally {
			// 프로그램 설치에 사용된 임시파일들을 삭제처리한다.
			deletedTempFiles();
		}

	}

}
