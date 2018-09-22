package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.common.enums.BusinessStatus;

/**
 * 标扩展信息
 */
public class T6231 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 标ID,参考T6230.F01
     */
    public int F01;
    
    /**
     * 还款总期数
     */
    public int F02;
    
    /**
     * 剩余期数
     */
    public int F03;
    
    /**
     * 月化收益率
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /**
     * 日化收益率
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /**
     * 下次还款日期
     */
    public Date F06;
    
    /**
     * 项目区域位置ID,参考T5119.F01
     */
    public int F07;
    
    /**
     * 资金用途
     */
    public String F08;
    
    /**
     * 借款描述
     */
    public String F09;
    
    /**
     * 审核时间
     */
    public Timestamp F10;
    
    /**
     * 满标时间
     */
    public Timestamp F11;
    
    /**
     * 放款时间
     */
    public Timestamp F12;
    
    /**
     * 结清时间
     */
    public Timestamp F13;
    
    /**
     * 垫付时间
     */
    public Timestamp F14;
    
    /**
     * 流标时间
     */
    public Timestamp F15;
    
    /**
     * 还款来源
     */
    public String F16;
    
    /**
     * 起息日期
     */
    public Date F17;
    
    /**
     * 结束日期
     */
    public Date F18;
    
    /**
     * 是否逾期,S:是(逾期);F:否;YZYQ:严重逾期
     */
    public T6231_F19 F19;
    
    /**
     * 还款提前预警日
     */
    public int F20;
    
    /**
     * 是否为按天借款,S:是;F:否
     */
    public T6231_F21 F21;
    
    /**
     * 借款天数
     */
    public int F22;
    
    /**
     * 是否是新手标,S:是;F:否
     */
    public T6231_F23 F23;
    
    /**
     * 是否融资担保,S:是;F:否
     */
    public T6231_F24 F24;
    
    /**
     * 最小投资额
     */
    public BigDecimal F25 = BigDecimal.ZERO;
    
    /**
     * 最大投资额
     */
    public BigDecimal F26 = BigDecimal.ZERO;
    
    /**
     * 是否奖励标,S:是;F:否
     */
    public T6231_F27 F27;
    
    /**
     * 奖励利率
     */
    public BigDecimal F28 = new BigDecimal(0);
    
    /** 
     * 是否为推荐标,S:是;F:否;
     */
    public T6231_F29 F29;
    
    /**
     * 设置推荐标的时间
     */
    public Timestamp F30;
    
    /**
     * 业务员工号
     */
    public String F31;
    
    /**
     * 业务员状态
     */
    public BusinessStatus F32;
    
    /**
     * 允许投资的终端
     */
    public T6231_F33 F33;
    
    /**
     * 不良债权转让时间
     */
    public Timestamp F34;
    
    /**
     * 来源：信用贷、担保贷、后台新增
     */
    public T6231_F35 F35;

    /**
     * 活动使用方式(奖励：红包、加息券、体验金)
     */
    public T6231_F36 F36;
}
