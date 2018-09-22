package com.dimeng.p2p.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML脚本过滤
 * @author  XiaoLang 2014 © dimeng.net 
 * @version v3.0
 * @LastModified 
 * 		Created,by XiaoLang 2014年12月23日
 */
public class HTMLValidator {
	
	public static boolean forbidden(String orignalValue) {
		if(orignalValue == null || "".equals(orignalValue.trim())) {
			return false;
		}
		String regex = "<script[^>]*>[\\s\\S]*</script>";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(orignalValue);
		return matcher.find();
	}
}
