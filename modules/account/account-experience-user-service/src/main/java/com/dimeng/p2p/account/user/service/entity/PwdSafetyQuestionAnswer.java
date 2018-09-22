package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.sql.Date;

public class PwdSafetyQuestionAnswer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Integer id; //id
	public Integer userId; //用户ID
	public Integer questionId;  //问题ID
	public String answer; //答案
	public Date lastupdate; //最后修改时间
	
}
