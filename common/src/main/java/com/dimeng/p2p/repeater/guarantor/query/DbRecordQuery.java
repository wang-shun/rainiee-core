/*
 * 文 件 名:  GuarantorQuery
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/15
 */
package com.dimeng.p2p.repeater.guarantor.query;

import java.sql.Timestamp;
/**
 * 担保交易记录查询条件实体
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/15]
 */
public interface DbRecordQuery {

	/**
	 * 交易类型
	 * @return
	 */
	public abstract int getType();

	/**
	 * 开始时间
	 * @return
	 */
	public abstract Timestamp getStartPayTime();

	/**
	 * 结束时间
	 * @return
	 */
	public abstract Timestamp getEndPayTime();

	/**
	 * 用户Id
	 * @return
	 */
	public abstract int getUserId();

}
