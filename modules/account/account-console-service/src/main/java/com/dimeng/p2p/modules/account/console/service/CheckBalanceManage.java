/*
 * 文 件 名:  CheckBalanceManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月20日
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceRecord;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceTotalAmount;
import com.dimeng.p2p.modules.account.console.service.query.CheckBalanceQuery;

/**
 * 平台调账管理
 * @author  xiaoqi
 * @version  [版本号, 2015年11月20日]
 */
public interface CheckBalanceManage extends Service {

	/**
	 * 平台充值
	 * <功能详细描述>
	 * @param amount
	 * @param remark
	 * @return
	 */
	public abstract int recharge(BigDecimal amount, String remark,boolean tg) throws Throwable;
	
	/**
	 * 平台提现
	 * <功能详细描述>
	 * @param amount
	 * @param remark
	 * @return
	 */
	public abstract int withdraw(BigDecimal amount, String remark,String bankCard,boolean tg)throws Throwable;
	
	/**
	 * 导出平台调账管理列表
	 * <功能详细描述>
	 * @param records
	 * @param outputStream
	 * @param charset
	 */
	public abstract void export(CheckBalanceRecord[] records,OutputStream outputStream, String charset,boolean tg)throws Throwable;
	
	/**
	 * 查询平台调账管理列表
	 * <功能详细描述>
	 * @param query
	 * @param page
	 * @return
	 */
	public abstract PagingResult<CheckBalanceRecord> search(CheckBalanceQuery query,Paging page,boolean tg)throws Throwable;
    
    /**
     * 查询平台调账管理列表收入总金额，只出总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract CheckBalanceRecord searchAmount(CheckBalanceQuery query, boolean tg)
        throws Throwable;

	/**
	 * 获取平台往来帐号余额
	 * <功能详细描述>
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal getPTZHBalance(boolean tg)throws Throwable;
	
	/**
	 * 统计累计收入和支出
	 * <功能详细描述>
	 * @return
	 * @throws Throwable
	 */
	public abstract CheckBalanceTotalAmount getTotal(boolean tg) throws Throwable;
	
	/**
	 * 根据条件统计收入和支出
	 * <功能详细描述>
	 * @param tg
	 * @return
	 * @throws Throwable
	 */
	public abstract CheckBalanceTotalAmount getSelectTotal(CheckBalanceQuery query,boolean tg) throws Throwable;
	
	/**
	 * 根据ID查询银行卡
	 * <功能详细描述>
	 * @param tg
	 * @return
	 * @throws Throwable
	 */
	public abstract String getBankCardById(Integer bankCardId) throws Throwable;
}
