package com.polymorphism;

public class Tiger extends Animal {


		private String name;
		
		public Tiger() {
			name = getClass().getSimpleName();
		}
		// 왜 this.getClass()가 아니라 getClass()?
		// getClass()는 Object에서 상속받은 메서드라서 this. 
		// 없이도 바로 호출 가능해요. 완전히 같은 의미예요.
		// .getSimpleName() // 그 클래스의 "단순 이름"만 반환
		
		@Override
		public String scream() {
			return "어흥";
		}
		public String getName() {
			return name;
		}
	}


