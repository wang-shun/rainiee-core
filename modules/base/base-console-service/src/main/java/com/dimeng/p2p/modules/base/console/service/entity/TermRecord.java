package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5017_F05;
import com.dimeng.p2p.common.enums.TermType;

public class TermRecord {

	/**
	 * 条款类型
	 */
	public TermType type;

	/**
	 * 浏览次数
	 */
	public int viewTimes;

	/**
	 * 协议内容
	 */
	public String content;

	/**
	 * 发布者ID
	 */
	public int publisherId;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 更新时间
	 */
	public Timestamp updateTime;
	/**
	 * 修改人姓名
	 */
	public String updaterName;

	/**
	 * 状态
	 */
	public T5017_F05 status;
}
