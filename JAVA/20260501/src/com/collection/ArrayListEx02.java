package com.collection;
import java.util.*;
public class ArrayListEx02 {

	public static void main(String[] args) {
		final int LIMIT = 10; //자르고자하는 글자의 개수 
		
		String str = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()ZZZ";
		
		int length = str.length();
		//System.out.println(length);
		
		List list = new ArrayList(length/LIMIT+10);
		
		for(int i = 0; i < length; i+=LIMIT) {
			if(i+LIMIT < length) {
				list.add(str.substring(i,i+LIMIT));
			}else {
				list.add(str.substring(i));
			}
		}
		
		for(int i = 0; i < list.size();i++) {
			System.out.println(list.get(i));
		}
	}
}
