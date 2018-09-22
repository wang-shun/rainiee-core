package com.dimeng.p2p.modules.base.front.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5021_F07;

public class AdvertSpscRecord {
	/**
	 * 视频ID
	 */
	public int id;
	/**
	 * 置顶值
	 */
	public int sortIndex;
	/**
	 * 视频标题
	 */
	public String title;
	/**
	 * 文件名称
	 */
	public String fileName;
	/**
	 * 文件大小
	 */
	public String fileSize;
	/**
	 * 文件格式
	 */
	public String fileFormat;
	/**
	 *是否自动播放
	 */
	public int isAuto;
	/**
	 * 发布者ID
	 */
	public int publisherId;
	/**
	 * 发布者姓名
	 */
	public String publisherName;
	/**
	 * 上传时间
	 */
	public Timestamp showTime;
	/**
	 * 最后修改时间
	 */
	public Timestamp updateTime;
	
	/**
	 * 发布状态
	 */
	public T5021_F07 status;

}
