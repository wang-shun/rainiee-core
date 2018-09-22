package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;

public class PwdSafetyQuestion implements Serializable {

	private static final long serialVersionUID = 1L;
	// fields
	public Integer id;
	public String type; //问题类型
	public String descr; //问题描述
	public Integer displayOrder; //排序
}
