package com.dimeng.p2p.common;

import java.math.BigDecimal;

/**
 * 数字格式化
 * @author  XiaoLang 2014 © dimeng.net 
 * @version v3.0
 * @LastModified 
 * 		Created,by XiaoLang 2014年12月23日
 */
public class HighPrecisionFormater {
	
	/**
	 * @param number
	 * @return
	 */
	public static String toPlainString(BigDecimal number) {
		if(number == null 
				|| number.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		} else {
			return number.stripTrailingZeros().toPlainString();
		}
	}
}
