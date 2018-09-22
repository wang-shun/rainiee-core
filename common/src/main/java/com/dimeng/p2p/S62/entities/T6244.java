/*
 * 文 件 名:  T6244.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 公益标的捐助协议
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class T6244 extends AbstractEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 公益标的ID,参考T6230.F01
     */
    public int F01; 
    /**
     * 协议类型ID,参考T5125.F01
     */
    public int F02 ;
    /**
     * 协议版本号
     */
    public int F03 ;
}
