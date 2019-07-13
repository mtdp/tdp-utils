package com.github.mtdp.util;

import java.util.Random;

/**
 * 
 *
 * @Description 自动生成字符串
 * @author gqwang
 * @date 2016年1月5日上午9:02:44
 *
 */
public class GenerateCharacter {
	
	//23456789abcdefghkmnpqrstxyzABCDEFGHKMNPQRSTXYZ
	
	private static char[] chars = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h','i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't','u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H','I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T','U', 'V', 'W', 'X', 'Y', 'Z'};
	
	/**
	 * 生成长度是多少的字符串
	 * @param num
	 * @param isDigit 是否纯数字
	 * @return
	 */
	public static String  randomGenernateCharacter(int num,boolean isDigit){
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		int index = chars.length;
		if(isDigit){
			index = 9;
		}
		for(int i = 0; i < num; i++){
			char ch = chars[random.nextInt(index)];
			sb.append(ch);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(randomGenernateCharacter(6, true));
		System.out.println(randomGenernateCharacter(16, false));
	}

}
