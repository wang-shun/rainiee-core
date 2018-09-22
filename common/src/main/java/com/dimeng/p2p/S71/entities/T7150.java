package com.dimeng.p2p.S71.entities
;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7150_F03;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.S71.enums.T7150_F12;
import com.dimeng.p2p.common.enums.BusinessStatus;

/** 
 * 线下充值申请
 */
public class T7150 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;
     */
    public T7150_F03 F03;

    /** 
     * 充值金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 充值状态,DSH:待审核;YCZ:已充值;YQX:已取消;
     */
    public T7150_F05 F05;

    /** 
     * 创建人,参考T7110.F01
     */
    public int F06;

    /** 
     * 申请时间
     */
    public Timestamp F07;

    /** 
     * 备注
     */
    public String F08;

    /** 
     * 审核人ID,参考T7110.F01
     */
    public int F09;

    /** 
     * 审核时间
     */
    public Timestamp F10;

    /** 
     * 审核意见
     */
    public String F11;
    
    /** 
     *  充值方式,S:后台线下充值;F:用户中心线下充值
     */
    public T7150_F12 F12;
    
    /**
     * 用户中心线下充值电话号码
     */
    public String F13;
    
    /**
     * 用户中心线下充值操作人
     */
    public String F14;
    
    /**
     * 第三方交易流水号
     */
    public String F15;

    /**
     * 业务员工号
     */
    public String F16;

    /**
     * 业务员状态
     */
    public BusinessStatus F17;
}
