package com.dimeng.p2p.account.user.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.entities.T6131;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6131_F07;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.account.user.service.entity.Order;
/**
 * 用户资金模块
 */
public interface ZjlsManage extends Service {

	/**
	 * 分页查询资金流水
	 * @param query
	 * @param paging
	 * @return
	 */
	public PagingResult<CapitalLs> searchLs(int tradingType, Date startTime, Date endTime, T6101_F03 zhlx, Paging paging)throws Throwable;
	
	/**
	 *分页查询充值的资金流水 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public PagingResult<T6131> searchCz(T6131_F07 stauts , Timestamp startTime, Timestamp endTime, Paging paging) throws Throwable;
	
	/**
	 * 分页查询提现的资金流水
	 * @param stauts
	 * @param startTime
	 * @param endTime
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public PagingResult<T6130> searchTx(T6130_F09 stauts , Timestamp startTime, Timestamp endTime,Paging paging) throws Throwable;
	
	/**
	 * 根据账户类型查询资金
	 * @param f03
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getZhje(T6101_F03 f03)throws Throwable;
	
	/**
	 * 查询用户的充值金额
	 * @param f03
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getCz()throws Throwable;
	/**
	 * 查询用户的提现金额
	 * @param f03
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getTx()throws Throwable;
	
	/**
	 * 计算托管用户提现总额
	 * <功能详细描述>
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getTGTx() throws Throwable;
	
	/**
	 * 导出交易记录查询数据
	 * 
	 * @param entities
	 *            记录列表
	 * @param outputStream
	 *            输出流
	 * @param charset
	 *            编码方式
	 * @throws Throwable
	 */
	public abstract void export(CapitalLs[] entities,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
	 * 手机版的交易流水
	 * @param type 0全部，1支出，2收入
	 * @param tradingType
	 * @param startTime
	 * @param endTime
	 * @param zhlx
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
    public abstract PagingResult<CapitalLs> mobileSearchLs(int type,int tradingType, Date startTime,
            Date endTime, T6101_F03 zhlx, Paging paging) throws Throwable;
    
    /**
     * 用户充值订单查询
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Order> searchCZ(List<T6501_F03> stauts, String typeCode, Timestamp startTime, Timestamp endTime, Paging paging) throws Throwable;
	
}
