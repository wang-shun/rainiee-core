package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S51.entities.T5127;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;

/**
 * 等级信息管理
 * 
 */
public abstract interface DjsmManage extends Service {
	
	/**
	 * 根据类型和等级查询等级信息
	 * @param type 类型
	 * @param dj 等级
	 * @return
	 * @throws Throwable
	 */
	public abstract T5127 get(T5127_F02 type,T5127_F03 dj) throws Throwable;

}
