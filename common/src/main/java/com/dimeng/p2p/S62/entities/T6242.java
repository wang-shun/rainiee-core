/*
 * 文 件 名:  T6242.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6242_F10;
import com.dimeng.p2p.S62.enums.T6242_F11;

/**
 * 公益标信息
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class T6242 extends AbstractEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int  F01 ;
    
    /**
     * 借款用户,平台商户号,参考T7110.F01
     */
    public int    F02 ;
    
    /**
     * 公益项目标题
     */
    public String    F03 ;
    
    /**
     * 标类型ID
     */
    public int    F04 ;
    
    /**
     * 项目金额
     */
    public BigDecimal F05 = BigDecimal.ZERO ;
    
    /**
     * '最低起捐金额
     */
    public BigDecimal F06 = BigDecimal.ZERO ;
    
    /**
     * 可投金额
     */
    public    BigDecimal F07 = BigDecimal.ZERO ;
    
    /**
     * 筹款期限,单位:天
     */
    public int   F08 ;
    
    /**
     * 借款周期,单位:月
     */
    public int   F09 ;
    
    /**
     * 是否允许流标,S:是;F:否;
     */
    public T6242_F10 F10 ;
    
    /**
     * 标状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:已发布;JKZ:捐款中;YJZ:已捐助;YZF:已作废;
     */
    public T6242_F11 F11 ;
    
    /**
     * 封面图片编码
     */
    public String   F12 ;
    
    /**
     * 发布时间,预发布状态有效
     */
    public Timestamp    F13 ;
    
    /**
     * 信用等级,参考T5124.F01
     */
    public int    F14 ;
    
    /**
     * 申请时间
     */
    public Timestamp    F15 ;
    
    /**
     * 审核时间
     */
    public Timestamp  F16 ;
    
    /**
     * 满标时间
     */
    public Timestamp   F17 ;
    
    /**
     * 放款时间
     */
    public Timestamp   F18 ;
    
    /**
     * 结束时间
     */
    public Timestamp  F19 ;
    
    /**
     * 流标时间
     */
    public Timestamp  F20 ;
    
    /**
     * 标编号
     */
    public String F21 ;
    
    /**
     * 公益方
     */
    public String F22 ;
    
    /**
     * 借款帐户,默认为平台帐号
     */
    public int  F23 ;
    
    /**
     * 项目简介
     */
    public String F24;
    
    /**
     * 第三方使用标号
     */
    public String F25;
}
