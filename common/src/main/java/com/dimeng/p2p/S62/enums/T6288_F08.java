/*
 * 文 件 名:  T6288_F08.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月14日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 加息券是否自动投资[S-是;F-否]
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月14日]
 */
public enum T6288_F08 {

	/**
	 * 否
	 */
	F("否"),
	/**
	 * 是
	 */
	S("是");

	protected final String chineseName;

	private T6288_F08(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * 获取中文名称 <功能详细描述>
	 * 
	 * @return ${@link String}
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * 解析字符串 <功能详细描述>
	 * 
	 * @param value
	 * @return
	 */
	public static T6288_F08 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6288_F08.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
