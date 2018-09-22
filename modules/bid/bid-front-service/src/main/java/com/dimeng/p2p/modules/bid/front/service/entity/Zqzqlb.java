package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6216_F18;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6260_F07;

/**
 * 线上债权转让列表
 *
 */
public class Zqzqlb extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 待收本息
     */
    public BigDecimal dsbx = new BigDecimal(0);
    
    /** 
     * 债权ID,参考T6251.F01
     */
    public int F01;
    
    /** 
     * 转让价格
     */
    public BigDecimal F02 = new BigDecimal(0);
    
    /** 
     * 已转债权
     */
    public BigDecimal F03 = new BigDecimal(0);
    
    /** 
     * 创建时间
     */
    public Timestamp F04;
    
    /** 
     * 结束时间
     */
    public Timestamp F05;
    
    /** 
     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
     */
    public T6260_F07 F06;
    
    /** 
     * 转让手续费率
     */
    public BigDecimal F07 = new BigDecimal(0);
    
    /** 
     * 债权人ID,参考T6110.F01
     */
    public int F08;
    
    /** 
     * 购买价格
     */
    public BigDecimal F09 = new BigDecimal(0);
    
    /** 
     * 原始债权金额
     */
    public BigDecimal F10 = new BigDecimal(0);
    
    /** 
     * 持有债权金额
     */
    public BigDecimal F11 = new BigDecimal(0);
    
    /** 
     * 借款标题
     */
    public String F12;
    
    /** 
     * 借款标类型ID,参考T6211.F01
     */
    public int F13;
    
    /** 
     * 年化利率
     */
    public BigDecimal F14 = new BigDecimal(0);
    
    /** 
     * 借款周期,单位:月
     */
    public int F15;
    
    /** 
     * 信用等级,参考T5124.F01
     */
    public int F16;
    
    /**
     * 信用等级名称
     */
    public String F18;
    
    /** 
     * 是否有担保,S:是;F:否;
     */
    public T6230_F11 F19;
    
    /** 
     * 是否有抵押,S:是;F:否;
     */
    public T6230_F13 F20;
    
    /** 
     * 是否实地认证,S:是;F:否;
     */
    public T6230_F14 F21;
    
    /**
     * 还款总期数
     */
    public int F22;
    
    /**
     * 剩余期数
     */
    public int F23;
    
    /** 
     * 标ID
     */
    public int F24;
    
    /** 
     * 债权申请ID
     */
    public int F25;
    
    /**
     * 封面图片编码
     */
    public String F26;
    
    /**
     * 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;
     */
    public T6230_F10 F27;
    
    /**
     * 担保方案,BXQEDB:本息全额担保;BJQEDB:本金全额担保;
     */
    public T6230_F12 F28;
    
    /**
     * 标产品ID
     */
    public int F29;
    
    /**
     * 产品风险等级
     */
    public T6216_F18 F30;
    
    /**
     * 担保机构简称
     */
    public String jgjc;
    
    /**
     * 封面图片
     */
    public String image;
    
    /**
     * 债权人
     */
    public String zqzrz;
    
    /**
     * 受让人
     */
    public String srr;
    
    /** 
     * 下个还款日
     */
    public Timestamp nextRepaymentDay;
    
    /**
     * 预期收益金额
     */
    public BigDecimal yqsy = new BigDecimal(0);

    /**
     * 借款标id
     */
    public int bidId;
}
