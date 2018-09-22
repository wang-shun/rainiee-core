package com.dimeng.p2p.common.enums;


/**
 * 信用等级.
 */
public enum CreditLevel {

	/**
	 * AA
	 */
	AA("AA"),
	/**
	 * A
	 */
	A("A"),
	/**
	 * B
	 */
	B("B"),
	/**
	 * C
	 */
	C("C"),
	/**
	 * D
	 */
	D("D"),
	/**
	 * E
	 */
	E("E"),
	/**
	 * HR
	 */
	HR("HR");

	protected final String name;

	private CreditLevel(String name) {
		this.name = name;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;

	}
}
