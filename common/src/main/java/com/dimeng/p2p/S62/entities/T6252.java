package com.dimeng.p2p.S62.entities;
import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6252_F12;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/** 
 * 标还款记录
 */
public class T6252 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 付款用户ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 收款用户ID,参考T6110.F01
     */
    public int F04;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F05;

    /** 
     * 期号
     */
    public int F06;

    /** 
     * 金额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 应还日期
     */
    public Date F08;
    
    /** 
     * 状态,WH:未还;YH:已还;
     */
    public T6252_F09 F09;

    /** 
     * 实际还款时间
     */
    public Timestamp F10;

    /** 
     * 债权ID,参考T6251.F01
     */
    public int F11;
    
    /**
     * 当前阶段状态,YFS:已发送到富友;YCX:已撤销了预授权
     */
    public T6252_F12 F12;

    public String accountName;

    public String bidTitle;

    public String phone;

    /**
     * 邮箱
     */
    public String email;
  

}
