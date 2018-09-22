package com.dimeng.p2p.S63.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6351_F11;
import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 积分商品
 */
public class T6351 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01;
    
    /**
     * 商品编号
     */
    public String F02;
    
    /**
     * 商品名称
     */
    public String F03;
    
    /**
     * 商品类别,参考T6350.F01
     */
    public int F04;
    
    /**
     * 商品积分
     */
    public int F05;
    
    /**
     * 商品库存
     */
    public int F06;
    
    /**
     * 成交笔数
     */
    public int F07;
    
    /**
     * 商品图片
     */
    public String F08;
    
    /**
     * APP商品图片
     */
    public String F09;
    
    /**
     * 商品详情
     */
    public String F10;
    
    /**
     * 状态（sold:已上架、unsold:已下架、 forsold：待上架）
     */
    public T6351_F11 F11;
    
    /**
     * 创建人
     */
    public int F12;
    
    /**
     * 创建时间
     */
    public Timestamp F13;
    
    /**
     * 最后修改时间
     */
    public Timestamp F14;
    
    /**
     * 商品价格
     */
    public BigDecimal F15;
    
    /**
     * 是否支持积分购买，No：不支持；Yes：支持
     */
    public YesOrNo F16;
    
    /**
     * 是否支持余额购买，No：不支持；Yes：支持
     */
    public YesOrNo F17;
    
    /**
     * 单用户限购数量
     */
    public int F18;
    
    /**
     * 市场参考值
     */
    public BigDecimal F19 = BigDecimal.ZERO;
    
    /**
     * 商品类型
     */
    public T6350 t6350;

    /**
     * 活动规则ID（参考S63.T6344.F01）
     */
    public int F20;
    
}
