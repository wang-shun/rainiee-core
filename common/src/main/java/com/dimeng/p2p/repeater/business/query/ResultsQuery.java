
package com.dimeng.p2p.repeater.business.query;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月9日]
 */
public abstract interface ResultsQuery extends PerformanceQuery {

	/**
	 * 所属一级客户用户名.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getNamelevel(); 
	/**
	 * 项目ID.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getProject();
	
	/**
	 *用户类型
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getUserType();
	
	/**
	 * 客户用户名
	 * @return
	 */
	public abstract String getCustomName();

	/**
	 * 一级客户姓名
	 * @return
	 */
	public  abstract String getUserNameLevel();

	/**
	 * 客户姓名
	 * @return
	 */
	public abstract String getCustomRealName();

	/**
	 * 交易类型
	 * @return
	 */
	public abstract String getTradeType();
}
