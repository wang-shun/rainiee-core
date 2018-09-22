/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.base.console.service.entity.Role;

/**
 * 用户组修改
 */
public interface YhzManage extends Service {
	/**
	 * 修改用户组管理
	 * @param id
	 * @param name
	 * @param desc
	 * @throws Throwable
	 */
	public abstract void update(int id, String name, String desc) throws Throwable;
	/**
	 * 获取角色信息
	 * @param roleId
	 * @return
	 * @throws Throwable
	 */
	public abstract Role get(int roleId)throws Throwable;
	
	/**
	 * 获取角色信息
	 * @param roleId
	 * @return
	 * @throws Throwable
	 */
	public abstract void del(int roleId)throws Throwable;
}
