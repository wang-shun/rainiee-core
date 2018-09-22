package com.dimeng.p2p.modules.systematic.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.modules.systematic.console.service.entity.MoneyStatisticalEntity;

/**
 * 
 * 资金统计总览接口
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2015年11月10日]
 */
public abstract interface MoneyStatisticManage extends Service
{
    
    /**
    * 查询账户余额总额
    * @param type
    * @return
    * @throws Throwable
    */
    public BigDecimal accountBalance()
        throws Throwable;
    
    /**
     * 查询账户余额
     * 根据T6101_F03枚举状态来查询用户可用余额总额、机构风险保证金、冻结金额总额
     * @return
     * @throws Throwable
     */
    public BigDecimal usableBalance(T6101_F03 f03)
        throws Throwable;
    
    /**
     * 用户提现手续费总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getTxsxf()
        throws Throwable;
    
    /**
     * 线下充值总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getXxcz()
        throws Throwable;
    
    /**
     * 线上充值总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getXscz()
        throws Throwable;
    
    /**
     * 用户充值手续费总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getCzsxf()
        throws Throwable;
    
    /**
     * 成交服务费总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getPtzhzjtj(int f03)
        throws Throwable;
    
    /**
     * 违约金手续费总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getWyj()
        throws Throwable;
    
    /**
     * 累计投资总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getLjtzje()
        throws Throwable;
    
    /**
     * 累计投资总收益
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getLjtzzsy()
        throws Throwable;
    
    /**
     * 散标投资总收益
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getSbtzzsy()
        throws Throwable;
    
    /**
     * 债权转让盈亏总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getZqzrykze()
        throws Throwable;
    
    /**
     * 借款已还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getJkyhzje()
        throws Throwable;
    
    /**
     * 借款正常还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getJkzchkze()
        throws Throwable;
    
    /**
     * 借款未还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getJkwhk()
        throws Throwable;
    
    /**
     * 借款逾期未还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getJkyqwh()
        throws Throwable;
    
    /**
     * 逾期机构垫付总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqjgdf()
        throws Throwable;
    
    /**
     * 逾期机构垫付未还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqjgdfwh()
        throws Throwable;
    
    /**
     * 逾期机构垫付已还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqjgdfyh()
        throws Throwable;
    
    /**
     * 逾期平台垫付总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqptdf()
        throws Throwable;
    
    /**
     * 逾期平台垫付已还款总额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public BigDecimal getYqptdfyh()
        throws Throwable;
    
    /**
     * 今日用户总充值
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getTodayCharge()
        throws Throwable;
    
    /**
     * 今日用户总提现
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getTodayWithdraw()
        throws Throwable;
    
    /**
     * 用户提现总额
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getYhtxze()
        throws Throwable;
    
    /**
     * 待转让债权价值总金额
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getDzrzqze()
        throws Throwable;
    
    /**
     * 转让中债权价值总金额
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getZrzzqze()
        throws Throwable;
    
    /**
     * 已转让债权价值总金额
     * @return BigDecimal
     * @throws Throwable
     */
    public BigDecimal getYzrzqze()
        throws Throwable;
    
    public void export(String platformTotalIncome, MoneyStatisticalEntity moneyStatisticalEntity,
        OutputStream outputStream, String charset)
        throws Throwable;
}
