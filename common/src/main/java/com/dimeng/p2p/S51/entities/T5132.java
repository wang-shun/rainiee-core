/*
 * 文 件 名:  T5132.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年5月19日
 */
package com.dimeng.p2p.S51.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5122_F03;

/**
 * <订单类型>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年5月19日]
 */
public class T5132 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7963432973909540070L;
    
    /** 
     * 交易类型编码,非自增
     */
    public int F01;
    
    /** 
     * 类型名称
     */
    public String F02;
    
    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T5122_F03 F03;
}
