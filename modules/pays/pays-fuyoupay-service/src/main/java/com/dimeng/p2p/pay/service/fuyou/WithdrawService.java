package com.dimeng.p2p.pay.service.fuyou;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;

public abstract interface WithdrawService extends Service {

	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public T6110 selectT6110(int userId) throws Throwable;
	
	/**
	 * 提现
	 * @param withdrawPsd
	 * @param funds
	 * @param cardId
	 * @param poundage
	 * @param txkcfs
	 * @return
	 * @throws Throwable
	 */
	public int withdraw(String withdrawPsd, BigDecimal funds, int cardId, BigDecimal poundage, boolean txkcfs) throws Throwable;
	
	/**
	 * 提现冻结用户金额
	 * @param orderId
	 * @throws Throwable
	 */
	public void freezeUserAccount(int orderId) throws Throwable;
	
	/**
	 * 组装代付请求参数
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	public List<NameValuePair> getRequestParams(int orderId) throws Throwable;
	
	/**
	 * 查询订单状态
	 * @param F01
	 * @return
	 * @throws Throwable
	 */
	public T6501 selectT6501(int F01) throws Throwable;
	
	/**
	 * 组装代付订单查询请求参数
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	public Map<String, String> queryRequestParams(int orderId) throws Throwable;
	
	/**
	 * 修改订单状态
	 * @param orderId
	 * @param F03
	 * @throws Throwable
	 */
	public void updateT6501(int orderId, T6501_F03 F03) throws Throwable;
	
	/**
	 * 提现失败返回
	 * @param orderId
	 * @throws Throwable
	 */
	public void trade(int orderId, String resson) throws Throwable;
	
}
