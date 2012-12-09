package org.xikilze.utils;


public class StringUtils {
	public static String toPartialPrint(String str, int size) {
		if (str == null) return "<null>";
		if (str.length()<= size) return str;
		return str.substring(0, size-3) + "...";
	}
}	
