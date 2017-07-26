package com.demo.voice.process.utils;

public class Utils {
	public static String encode(String text) {
		try {
			text = new String(text.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}

