/*
 * 文 件 名:  Sponsors.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年7月6日
 */
package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;

/**
 * <担保方>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年7月6日]
 */
public class Sponsors implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3991815052749651324L;
    
    /**
     * 担保方id
     */
    public int sponsorsId;
    
    /**
     * 担保机构
     */
    public String sponsorsName;
}
