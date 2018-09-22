package com.dimeng.p2p.scheduler.core;

import java.util.Map;

/**
 * Job上下文
 * 
 * @author heluzhu
 *
 */
public class JobContext {
	
	private static final ThreadLocal<Map<String, String>> jobParamLocal = new ThreadLocal<Map<String, String>>();
	
	/**
	 * 设置任务参数
	 * 
	 * @param map
	 */
	public static void setParamMap(Map<String, String> map){
		jobParamLocal.set(map);
	}
	
	/**
	 * 获取任务参数
	 * 
	 * @param key
	 * @return String
	 */
	public static String getParam(String key){
		Map<String, String> map = jobParamLocal.get();
		if(map != null){
			return map.get(key);
		}
		return null;
	}

}
