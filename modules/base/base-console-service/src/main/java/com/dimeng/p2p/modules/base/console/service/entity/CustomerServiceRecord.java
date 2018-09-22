package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;

public class CustomerServiceRecord {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 浏览次数
	 */
	public int viewTimes;
	/**
	 * 排序值
	 */
	public int sortIndex;
	/**
	 * 名称
	 */
	public String name;
	/**
	 * 客服号
	 */
	public String number;
	/**
	 * 图片编码
	 */
	public String imageCode;
	/**
	 * 客服类型
	 */
	public T5012_F03 type;
	/**
	 * 发布者ID
	 */
	public int publisherId;
	/**
	 * 发布者姓名
	 */
	public String publisherName;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 最后更新时间
	 */
	public Timestamp updateTime;
	/**
	 * 状态
	 */
	public T5012_F11 status;

}
