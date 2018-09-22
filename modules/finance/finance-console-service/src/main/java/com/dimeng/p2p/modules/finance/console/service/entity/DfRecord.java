/*
 * 文 件 名:  DfRecord.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  dingjinqing
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.modules.finance.console.service.entity;

import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.enums.T6230_F10;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class DfRecord
{
    
    /**
     * 标Id
     */
    public int bidId;
    
    /**
     * 标编号
     */
    public String bidNo;
    
    /**
     * 借款标题
     * 
     */
    public String loanRecordTitle;
    
    /**
     * 借款人
     */
    public String loanName;
    
    /**
     * 借款人ID
     */
    public int userID;
    
    /**
     * 借款金额
     */
    public BigDecimal loanAmount = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    public BigDecimal yearRate = new BigDecimal(0);
    
    /**
     * 下一还款日
     */
    public Date refundDay;
  
    /**
     * 第几期
     */
    public int period;
    
    /**
     * 总期数
     */
    public int periods;
    
    /**
     * 期数
     */
    public String loandeadline;
    
    /**
     * 逾期天数（天）
     */
    public int yuqi;
    
    /**
     * 本期应还金额（元）
     */
    public BigDecimal principalAmount = new BigDecimal(0);
    
    /**
     * 逾期费用（元）
     */
    public BigDecimal yuqiAmount = new BigDecimal(0);
    
    /**
     * 待垫付金额（元）
     */
    public BigDecimal dinfuAmount = new BigDecimal(0);
    
    
    /**
     * 待还本金（元）
     */
    public BigDecimal dhbj = new BigDecimal(0);
    
    /**
     * 待还利息（元）
     */
    public BigDecimal dhlx = new BigDecimal(0);
    
    /**
     * 待垫付金额（元）
     */
    public BigDecimal ddfAmount = new BigDecimal(0);
    
    /**
     * 垫付方式
     */
    public T5131_F02 dfmethod;
    
    
    /**
     * 借款期限
     */
    public String loandTime;
    
    /**
     * 操作人
     */
    public String operate;
    
    /**
     * 垫付时间
     */
    public Timestamp dfTime;
    
    /**
     * 结清时间
     */
    public Timestamp jqTime;
    
    /**
     * 偿还时间
     */
    public Timestamp chTime;
    
    /**
     * 追偿金额（元）
     */
    public BigDecimal zuicAmount = new BigDecimal(0);
    
    /**
     * 还款方式
     */
    public T6230_F10 hkfs;
    
    /**
     * 从哪一期开始垫付的
     */
    public int dfStartPeriond;
    
    /**
     * 已经垫付金额
     */
    public BigDecimal ydfAmount = new BigDecimal(0);
    
    /**
     * 垫付返还金额
     */
    public BigDecimal dffhAmount = new BigDecimal(0);


    /**
     * 待还本金总额（元）
     */
    public BigDecimal dhbjTotal = new BigDecimal(0);

    /**
     * 待还利息总额（元）
     */
    public BigDecimal dhlxTotal = new BigDecimal(0);
}
