/*
 * 文 件 名:  BidInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;

/**
 * <标的信息>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月16日]
 */
public class BidInfo implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3822956838687682248L;
    
    /**
     * id
     */
    public int id;
    
    /**
     * 借款标题
     * 
     */
    public String loanTitle;
}
