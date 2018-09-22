/*
 * 文 件 名:  ExplainInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.modules.base.console.service.entity;

/**
 * <说明信息>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
public interface ExplainInfo
{
    
    /**
     * 获取说明id.
     * 
     * @return {@link int} 说明id
     */
    int getId();
    
    /**
     * 获取说明类型.
     * 
     * @return {@link String} 说明类型
     */
    String getType();
    
    /**
     * 获取说明内容.
     * 
     * @return {@link String} 说明内容
     */
    String getContent();
    
}
