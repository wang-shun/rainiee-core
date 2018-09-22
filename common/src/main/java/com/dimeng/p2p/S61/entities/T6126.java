/*
 * 文 件 名:  T6126
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/15
 */
package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 担保方担保交易记录
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/15]
 */
public class T6126 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户账号ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F03;

    /** 
     * 发生时间
     */
    public Timestamp F04;

    /** 
     * 收入
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 支出
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 余额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 备注
     */
    public String F08;

}
