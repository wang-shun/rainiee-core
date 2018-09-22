package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6230_Ext;
import com.dimeng.p2p.S62.enums.T6273_F07;

public class QmjkEntity
{
    
    private static final long serialVersionUID = 670947648821982831L;
    
    /**
     * 借款标题
     */
    public String title;
    
    /**
     * 借款金额
     */
    public BigDecimal amount;
    
    /**
     * 年利率
     */
    public String nll;
    
    /**
     * 是否为按天借款
     */
    public String F05;
    
    /**
     * 期限（天）
     */
    public String term;
    
    /**
     * 状态
     */
    public String state;
    
    /**
     * 状态code
     */
    public String stateCode;
    
    /**
     * 标的id
     */
    public int bidId;
    
    /**
     * 借款时间（月）
     */
    public String F08;
    
    /**
     * 查看签名文档地址
     */
    public String docUrl;
    
    /**
     * 是否旧数据
     */
    public boolean isOldData;
}
