/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.cond.TxdzCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.TxdzEntity;

*//**
 * 
 * 提现业务管理接口
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月30日]
 *//*
public interface WithDrawManage extends Service
{
    
    *//**
     * 查询用户第三方托管信息
     * 
     * @param F01
     * @return
     * @throws Throwable
     *//*
    public String selectT6119()
        throws Throwable;
    
    *//**
     * 生成提现订单
     * @param funds 现金
     * @param cardId 银行卡ID
     * @param f03 账号类型
     * @return
     * @throws Throwable
     *//*
    public String addOrderId(BigDecimal funds, int cardId, T6101_F03 f03)
        throws Throwable;
    
    *//**
     * 验签提现返回信息
     * @param params
     * @return
     * @throws Throwable
     *//*
    public boolean withdrawReturnDecoder(Map<String, String> params)
        throws Throwable;
    
    *//**
     * 更新提现订单
     * <功能详细描述>
     * @param params
     * @return
     * @throws Throwable
     *//*
    public int updateOrder(Map<String, String> params)
        throws Throwable;
    
    *//** 
     * 描述:构建提现实体参数
     * 作者:wangshaohua 
     * 创建时间：2015年4月28日
     * @param cond
     * @return
     * @throws Throwable
     *//*
    public abstract Map<String, String> createWithdrawUrI(BigDecimal amount, ChargeCond cond)
        throws Throwable;
    
    *//**
     * 查询提现订单详情
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     *//*
    public T6503 selectT6503(Connection connection, int F01)
        throws SQLException;
    
     @param query
    *            提现对账接口
    * @param page
    *            分页对象
    * @return PagingResult<LoanCheck> 提现对账分页集合
    * @throws Throwable
    
    public abstract PagingResult<TxdzEntity> search(TxdzCond txdzCond, Paging paging)
        throws Throwable;
    
    *//** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     *//*
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
*/