package com.exceptionex;

public class ExceptionEx11 {

	public static void main(String[] args) {
		
		try {
			install();
		} catch (InstallException ee) {
			ee.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void install() throws InstallException {
		
		try {
			startInstall();
			copyFiles();
		} catch (SpaceException se) {
			InstallException ie = new InstallException("설치 중 예외발생");
			ie.initCause(se); // 지정한 예외를 원인 예외로 등록시켜서 원인예외를 반환
			throw ie;
		} catch (MemoryException me) {
			InstallException ie = new InstallException("설치 중 예외발생");
			ie.initCause(me); // 지정한 예외를 원인 예외로 등록시켜서 원인예외를 반환
			throw ie;
		} finally {
			deletedTempFiles();
		}
		
		
	}
	
	
	
	static void startInstall() throws SpaceException, MemoryException {
		if (!enoughSpace()) {
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
		return true;
	}
}
