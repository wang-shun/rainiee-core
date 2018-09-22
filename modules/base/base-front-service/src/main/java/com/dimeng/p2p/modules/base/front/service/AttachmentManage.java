package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6212;

public abstract interface AttachmentManage extends Service{
	/**
	 * 根据附件类型ID查询名称
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract String getName(int id) throws Throwable;
	/**
	 * 查询所有类型文件
	 * @return
	 * @throws Throwable
	 */
	public abstract T6212[] search() throws Throwable;
}
