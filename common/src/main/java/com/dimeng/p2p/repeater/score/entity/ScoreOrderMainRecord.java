/*
 * 文 件 名:  OperationLog.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  beiweiyuan
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2015年12月14日]
 */
public class ScoreOrderMainRecord extends AbstractEntity
{

    private static final long serialVersionUID = 1L;

    /**
     * 订单明细ID
     */
    public String id;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 真实姓名
     */
    public String realName;
    
    /**
     * 手机号码
     */
    public String phoneNum;

    /**
     * 成交开始时间
     */
    public Timestamp dealTime;

    /**
     * 发货时间
     */
    public Timestamp fhTime;

    /**
     * 审核时间
     */
    public Timestamp shTime;

    /**
     * 退货时间
     */
    public Timestamp thTime;

    /**
     * 商品名称
     */
    public String productName;
    
    /**
     * 价值
     */
    public BigDecimal worth;
    
    /**
     * 数量
     */
    public int quantity;
    
    /**
     * 支付方式
     */
    public T6352_F06 payType;

    /**
     * 商品类别名称T6350.F02
     */
    public String productTypeName;
    
    /**
     * 商品类别T6350.F07
     */
    public T6350_F07 productType;

    /**
     * 状态
     */
    public T6359_F08 status;
    
    /**
     * 商品编号
     */
    public String comEncoding;

}
