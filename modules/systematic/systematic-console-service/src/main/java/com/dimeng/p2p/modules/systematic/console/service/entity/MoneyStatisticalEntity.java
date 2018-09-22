package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.math.BigDecimal;

public class MoneyStatisticalEntity
{
    
    /**
    * 账户余额总额  账户余额=可用金额+冻结金额+风险保证金（机构）
    */
    public BigDecimal accountBalance = BigDecimal.ZERO;
    
    /**
     * 可用余额总额 包含个人、企业、机构
     */
    public BigDecimal usableBalance = BigDecimal.ZERO;
    
    /**
     * 机构风险保证金 统计所有机构账户风险保证金总额
     */
    public BigDecimal margin = BigDecimal.ZERO;
    
    /**
     * 线上充值总额
     */
    public BigDecimal onlinePay = BigDecimal.ZERO;
    
    /**
     * 线下充值总额
     */
    public BigDecimal offlinePay = BigDecimal.ZERO;
    
    /**
     * 用户提现手续费总额
     */
    public BigDecimal Txsxf = BigDecimal.ZERO;
    
    /**
     * 用户充值手续费总额
     */
    public BigDecimal Czsxf = BigDecimal.ZERO;
    
    /**
     * 冻结金额总额 包含个人、企业、机构
     */
    public BigDecimal amountFrozen = BigDecimal.ZERO;
    
    /**
     * 成交服务费总额
     */
    public BigDecimal Cjfwf = BigDecimal.ZERO;
    
    /**
     * 理财管理费总额
     */
    public BigDecimal Lcglf = BigDecimal.ZERO;
    
    /**
     * 债权转让手续费总额
     */
    public BigDecimal Zqzrsxf = BigDecimal.ZERO;
    
    /**
     * 违约金手续费总额
     */
    public BigDecimal Wyjsxf = BigDecimal.ZERO;
    
    /**
     * 累计投资总额
     */
    public BigDecimal Ljtzje = BigDecimal.ZERO;
    
    /**
     * 累计投资总收益
     */
    public BigDecimal Ljtzzsy = BigDecimal.ZERO;
    
    /**
     * 散标投资总收益
     */
    public BigDecimal Sbtzzsy = BigDecimal.ZERO;
    
    /**
     * 债权转让盈亏总额
     */
    public BigDecimal Zqzrykze = BigDecimal.ZERO;
    
    /**
     * 借款已还款总额
     */
    public BigDecimal Jkyhzje = BigDecimal.ZERO;
    
    /**
     * 借款正常还款总额
     */
    public BigDecimal Jkzchkz = BigDecimal.ZERO;
    
    /**
     * 借款未还款总额
     */
    public BigDecimal Jkwhk = BigDecimal.ZERO;
    
    /**
     * 借款逾期未还款总额
     */
    public BigDecimal Jkyqwh = BigDecimal.ZERO;
    
    /**
     * 逾期机构垫付总额
     */
    public BigDecimal Yqjgdf = BigDecimal.ZERO;
    
    /**
     * 逾期机构垫付已还款总额
     */
    public BigDecimal Yqjgdfyh = BigDecimal.ZERO;
    
    /**
     * 逾期机构垫付未还款总额
     */
    public BigDecimal Yqjgdfwh = BigDecimal.ZERO;
    
    /**
     * 逾期平台垫付总额
     */
    public BigDecimal Yqptdf = BigDecimal.ZERO;
    
    /**
     * 逾期平台垫付已还款总额
     */
    public BigDecimal Yqptdfyh = BigDecimal.ZERO;
    
    /**
     * 逾期平台垫付未还款总额
     */
    public BigDecimal Yqptdfwh = BigDecimal.ZERO;
    
    /**
     * 今日用户总充值
     */
    public BigDecimal todayCharge = BigDecimal.ZERO;
    
    /**
     * 今日用户总提现
     */
    public BigDecimal todayWithdraw = BigDecimal.ZERO;
    
    /**
     * 用户提现总额
     */
    public BigDecimal yhtxze = BigDecimal.ZERO;
    
    /**
     * 待转让债权价值总金额
     */
    public BigDecimal dzrzqze = BigDecimal.ZERO;
    
    /**
     * 转让中债权价值总金额
     */
    public BigDecimal zrzzqze = BigDecimal.ZERO;
    
    /**
     * 已转让债权价值总金额
     */
    public BigDecimal yzrzqze = BigDecimal.ZERO;
    
    public BigDecimal getAccountBalance()
    {
        return accountBalance;
    }
    
    public void setAccountBalance(BigDecimal accountBalance)
    {
        this.accountBalance = accountBalance;
    }
    
    public BigDecimal getUsableBalance()
    {
        return usableBalance;
    }
    
    public void setUsableBalance(BigDecimal usableBalance)
    {
        this.usableBalance = usableBalance;
    }
    
    public BigDecimal getMargin()
    {
        return margin;
    }
    
    public void setMargin(BigDecimal margin)
    {
        this.margin = margin;
    }
    
    public BigDecimal getAmountFrozen()
    {
        return amountFrozen;
    }
    
    public void setAmountFrozen(BigDecimal amountFrozen)
    {
        this.amountFrozen = amountFrozen;
    }
    
    public BigDecimal getOnlinePay()
    {
        return onlinePay;
    }
    
    public void setOnlinePay(BigDecimal onlinePay)
    {
        this.onlinePay = onlinePay;
    }
    
    public BigDecimal getOfflinePay()
    {
        return offlinePay;
    }
    
    public void setOfflinePay(BigDecimal offlinePay)
    {
        this.offlinePay = offlinePay;
    }
    
    public BigDecimal getTxsxf()
    {
        return Txsxf;
    }
    
    public void setTxsxf(BigDecimal txsxf)
    {
        Txsxf = txsxf;
    }
    
    public BigDecimal getCzsxf()
    {
        return Czsxf;
    }
    
    public void setCzsxf(BigDecimal czsxf)
    {
        Czsxf = czsxf;
    }
    
    public BigDecimal getCjfwf()
    {
        return Cjfwf;
    }
    
    public void setCjfwf(BigDecimal cjfwf)
    {
        Cjfwf = cjfwf;
    }
    
    public BigDecimal getLcglf()
    {
        return Lcglf;
    }
    
    public void setLcglf(BigDecimal lcglf)
    {
        Lcglf = lcglf;
    }
    
    public BigDecimal getZqzrsxf()
    {
        return Zqzrsxf;
    }
    
    public void setZqzrsxf(BigDecimal zqzrsxf)
    {
        Zqzrsxf = zqzrsxf;
    }
    
    public BigDecimal getWyjsxf()
    {
        return Wyjsxf;
    }
    
    public void setWyjsxf(BigDecimal wyjsxf)
    {
        Wyjsxf = wyjsxf;
    }
    
    public BigDecimal getLjtzje()
    {
        return Ljtzje;
    }
    
    public void setLjtzje(BigDecimal ljtzje)
    {
        Ljtzje = ljtzje;
    }
    
    public BigDecimal getLjtzzsy()
    {
        return Ljtzzsy;
    }
    
    public void setLjtzzsy(BigDecimal ljtzzsy)
    {
        Ljtzzsy = ljtzzsy;
    }
    
    public BigDecimal getSbtzzsy()
    {
        return Sbtzzsy;
    }
    
    public void setSbtzzsy(BigDecimal sbtzzsy)
    {
        Sbtzzsy = sbtzzsy;
    }
    
    public BigDecimal getZqzrykze()
    {
        return Zqzrykze;
    }
    
    public void setZqzrykze(BigDecimal zqzrykze)
    {
        Zqzrykze = zqzrykze;
    }
    
    public BigDecimal getJkyhzje()
    {
        return Jkyhzje;
    }
    
    public void setJkyhzje(BigDecimal jkyhzje)
    {
        Jkyhzje = jkyhzje;
    }
    
    public BigDecimal getJkwhk()
    {
        return Jkwhk;
    }
    
    public void setJkwhk(BigDecimal jkwhk)
    {
        Jkwhk = jkwhk;
    }
    
    public BigDecimal getJkyqwh()
    {
        return Jkyqwh;
    }
    
    public void setJkyqwh(BigDecimal jkyqwh)
    {
        Jkyqwh = jkyqwh;
    }
    
    public BigDecimal getYqjgdf()
    {
        return Yqjgdf;
    }
    
    public void setYqjgdf(BigDecimal yqjgdf)
    {
        Yqjgdf = yqjgdf;
    }
    
    public BigDecimal getYqjgdfyh()
    {
        return Yqjgdfyh;
    }
    
    public void setYqjgdfyh(BigDecimal yqjgdfyh)
    {
        Yqjgdfyh = yqjgdfyh;
    }
    
    public BigDecimal getYqptdf()
    {
        return Yqptdf;
    }
    
    public void setYqptdf(BigDecimal yqptdf)
    {
        Yqptdf = yqptdf;
    }
    
    public BigDecimal getYqptdfyh()
    {
        return Yqptdfyh;
    }
    
    public void setYqptdfyh(BigDecimal yqptdfyh)
    {
        Yqptdfyh = yqptdfyh;
    }
    
    public BigDecimal getYqjgdfwh()
    {
        return Yqjgdfwh;
    }
    
    public void setYqjgdfwh(BigDecimal yqjgdfwh)
    {
        Yqjgdfwh = yqjgdfwh;
    }
    
    public BigDecimal getYqptdfwh()
    {
        return Yqptdfwh;
    }
    
    public void setYqptdfwh(BigDecimal yqptdfwh)
    {
        Yqptdfwh = yqptdfwh;
    }
    
    public BigDecimal getJkzchkz()
    {
        return Jkzchkz;
    }
    
    public void setJkzchkz(BigDecimal jkzchkz)
    {
        Jkzchkz = jkzchkz;
    }
    
    public BigDecimal getTodayCharge()
    {
        return todayCharge;
    }
    
    public void setTodayCharge(BigDecimal todayCharge)
    {
        this.todayCharge = todayCharge;
    }
    
    public BigDecimal getTodayWithdraw()
    {
        return todayWithdraw;
    }
    
    public void setTodayWithdraw(BigDecimal todayWithdraw)
    {
        this.todayWithdraw = todayWithdraw;
    }
    
    public BigDecimal getYhtxze()
    {
        return yhtxze;
    }
    
    public void setYhtxze(BigDecimal yhtxze)
    {
        this.yhtxze = yhtxze;
    }
    
    public BigDecimal getDzrzqze()
    {
        return dzrzqze;
    }
    
    public void setDzrzqze(BigDecimal dzrzqze)
    {
        this.dzrzqze = dzrzqze;
    }
    
    public BigDecimal getZrzzqze()
    {
        return zrzzqze;
    }
    
    public void setZrzzqze(BigDecimal zrzzqze)
    {
        this.zrzzqze = zrzzqze;
    }
    
    public BigDecimal getYzrzqze()
    {
        return yzrzqze;
    }
    
    public void setYzrzqze(BigDecimal yzrzqze)
    {
        this.yzrzqze = yzrzqze;
    }
    
}
