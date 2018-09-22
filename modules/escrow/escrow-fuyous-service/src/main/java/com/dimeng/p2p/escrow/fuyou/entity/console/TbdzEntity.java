/*
 * 文 件 名:  TbdzEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heshiping
 * 修改时间:  2015年12月24日
 */
package com.dimeng.p2p.escrow.fuyou.entity.console;

import java.math.BigDecimal;

import com.dimeng.p2p.S65.entities.T6501;

/**
 * 
 * 投资实体类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月24日]
 */
public class TbdzEntity extends T6501
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1483986643598054784L;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 投资金额
     */
    public BigDecimal amount;
}
