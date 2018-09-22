/*
 * 文 件 名:  QyUserInsert.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年8月25日
 */
package com.dimeng.p2p.modules.account.pay.service.entity;

import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6141_F03;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年8月25日]
 */
public abstract interface QyUserInsert
{
    /**
     * 账号名
     * 
     * @return
     */
    public abstract String getAccountName();
    
    /**
     * 密码
     * 
     * @return
     */
    public abstract String getPassword();
    
    /**
     * 兴趣类型（LC:理财;JK:借款）
     */
    public abstract T6141_F03 getType();
    
    /**
    * 注册来源
    */
    public abstract T6110_F08 getRegisterType();
    
    /**
     * 企业名称
     * 
     */
    public abstract String getEnterpriseName();
}
