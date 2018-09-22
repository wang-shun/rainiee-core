/*
 * 文 件 名:  Donation.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月10日
 */
package com.dimeng.p2p.repeater.donation.entity;

import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6246;

/**
 * 捐助信息,扩展了T6246
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月10日]
 */
public class Donation extends T6246
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 捐助人的姓名
     */
    public String userName;
    
    /**
     * 捐助对象信息
     */
    public T6242 t6242;
    
}
