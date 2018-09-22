package com.dimeng.p2p.scheduler.core;

/**
 * 任务状态
 * 
 * @author heluzhu
 *
 */
public enum JobStatus {
	/**
	 * 待执行
	 */
	waiting
	, 
	/**
	 * 运行中
	 */
	runing
	, 
	/**
	 * 暂停
	 */
	pause
}
