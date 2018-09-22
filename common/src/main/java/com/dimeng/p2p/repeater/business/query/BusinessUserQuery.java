package com.dimeng.p2p.repeater.business.query;
/**
 * 
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月9日]
 */
public abstract interface BusinessUserQuery extends SysUserQuery {
	public abstract String getEmployNum();

	/**
	 * 所属部门
	 * @return
	 */
	public abstract String getDept();

}
