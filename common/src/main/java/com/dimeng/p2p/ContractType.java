package com.dimeng.p2p;

import java.util.HashMap;
import java.util.Map;

public enum ContractType {

	/**
	 * 三方借款协议
	 */
	SANFJKXY(1001, "三方借款协议"),
	/**
	 * 四方借款协议
	 */
	SIFJKXY(1002, "四方借款协议"),
	/**
	 * 债权转让协议
	 */
	ZQZRXY(2001, "债权转让协议"),
	/**
	 * 优选理财协议
	 */
	XYLCXY(3001, "优选理财协议");

	int contractType;
	String chineseName;

	protected static final Map<Integer, String> maps = new HashMap<Integer, String>();

	static {
		for (ContractType contractType : values()) {
			maps.put(contractType.contractType, contractType.chineseName);
		}

	}

	private ContractType(int contractType, String chineseName) {
		this.contractType = contractType;
		this.chineseName = chineseName;
	}

	public int contractType() {
		return contractType;
	}

	public String getChineseName() {
		return chineseName;
	}

	public static String getTypeName(int typeId) {
		return maps.get(typeId);
	}
}
