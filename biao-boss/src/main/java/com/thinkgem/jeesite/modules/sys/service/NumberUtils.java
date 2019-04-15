package com.thinkgem.jeesite.modules.sys.service;

import java.util.Random;

public class NumberUtils {

	private static final int[] NUMBERS = {1,2,3,4,5,6,7,8,9,0} ;
	
	public static String getRandomNumber(int size) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int start = random.nextInt(10);
			if(start==9&&i==0) {
				start = 0 ;
			}
			sb.append(NUMBERS[start]);
		}
		return sb.toString();
	}
	
}
