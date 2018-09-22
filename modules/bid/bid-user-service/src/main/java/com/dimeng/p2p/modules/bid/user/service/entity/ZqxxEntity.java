package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6251_F08;

public class ZqxxEntity extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 债权编码
     */
    public String F01;
    
    /**
     * 标ID,参考T6230.F01
     */
    public int F02;
    
    /**
     * 债权人ID,参考T6110.F01
     */
    public int F03;
    
    /**
     * 购买价格
     */
    public BigDecimal F04 = new BigDecimal(0);
    
    /**
     * 原始债权金额
     */
    public BigDecimal F05 = new BigDecimal(0);
    
    /**
     * 持有债权金额
     */
    public BigDecimal F06 = new BigDecimal(0);
    
    /**
     * 是否正在转让,S:是;F:否;
     */
    public T6251_F08 F07;
    
    /**
     * 创建日期
     */
    public Date F08;
    
    /**
     * 起息日期
     */
    public Date F09;
    
    /**
     * 借款用户ID,参考T6110.F01
     */
    public int F10;
    
    /**
     * 借款标题
     */
    public String F11;
    
    /**
     * 借款标类型ID,参考T6211.F01
     */
    public int F12;
    
    /**
     * 借款金额
     */
    public BigDecimal F13 = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    public BigDecimal F14 = new BigDecimal(0);
    
    /**
     * 可投金额
     */
    public BigDecimal F15 = new BigDecimal(0);
    
    /**
     * 筹款期限,单位:天
     */
    public int F16;
    
    /**
     * 借款周期,单位:月
     */
    public int F17;
    
    /**
     * 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;
     */
    public T6230_F10 F18;
    
    /**
     * 是否有担保,S:是;F:否;
     */
    public T6230_F11 F19;
    
    /**
     * 担保方案,BXQEDB:本息全额担保;BJQEDB:本金全额担保;
     */
    public T6230_F12 F20;
    
    /**
     * 是否有抵押,S:是;F:否;
     */
    public T6230_F13 F21;
    
    /**
     * 是否实地认证,S:是;F:否;
     */
    public T6230_F14 F22;
    
    /**
     * 是否自动放款,S:是;F:否;
     */
    public T6230_F15 F23;
    
    /**
     * 是否允许流标,S:是;F:否;
     */
    public T6230_F16 F24;
    
    /**
     * 付息方式,ZRY:自然月;GDR:固定日;
     */
    public T6230_F17 F25;
    
    /**
     * 付息日,自然月在满标后设置为满标日+起息日,固定日则必须小于等于28
     */
    public int F26;
    
    /**
     * 起息天数,T+N,默认为0
     */
    public int F27;
    
    /**
     * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:
     * 已流标;YDF:已垫付;
     */
    public T6230_F20 F28;
    
    /**
     * 封面图片编码
     */
    public String F29;
    
    /**
     * 发布时间,预发布状态有效
     */
    public Timestamp F30;
    
    /**
     * 信用等级,参考T5124.F01
     */
    public int F31;
    
    /**
     * 申请时间
     */
    public Timestamp F32;
    
    /**
     * 标编号
     */
    public String F33;
    
    /**
     * 计息金额
     */
    public BigDecimal F34 = new BigDecimal(0);
    
    /*************** 回收中的债权 **********************/
    /**
     * 待收本息
     */
    public BigDecimal dsbx = new BigDecimal(0);
    
    /**
     * 月收本息
     */
    public BigDecimal ysbx = new BigDecimal(0);
    
    /**
     * 还款期数
     */
    public int hkqs;
    
    /**
     * 剩余期数
     */
    public int syqs;
    
    /**
     * 下个还款日
     */
    public Date xghkr;
    
    /*************** 已结清的债权 **********************/
    /**
     * 回收金额
     */
    public BigDecimal hsje = new BigDecimal(0);
    
    /**
     * 已赚金额
     */
    public BigDecimal yzje = new BigDecimal(0);
    
    /**
     * 结清日期
     */
    public Timestamp jqsj;
    
    /**
     * 是否可转让
     */
    public boolean isTransfer;
    
    /**
     * 债权ID
     */
    public int zqid;
    
    /**
     * 债权转让订单ID
     */
    public int zqzrOrderId;
    
    /**
     * 加息率
     */
    public BigDecimal jxl = new BigDecimal(0);
    
    /**
     * 不良债权转让记录id
     */
    public int blzqzrId;
}
