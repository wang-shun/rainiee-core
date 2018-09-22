package com.dimeng.p2p.app.tool;

public class RegexUtils {
	
	/**
	 * 帐号正则表达式
	 */
	public static final String  USER_NAME_REG = "^[a-zA-Z]([\\w]{5,17})$";
	
	/**
	 * 密码正则表达式
	 */
	public static final String  PASSWORD_REG = ".{6,20}";
	
	/**
	 * 邀请码正则表达式
	 */
	public static final String  INVITATION_REG = "^[A-Za-z0-9]{0,20}$";
	
}

