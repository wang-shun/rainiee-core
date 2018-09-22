/*
 * 文 件 名:  RealNameStatisticsEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.modules.statistics.console.service.entity;

import com.dimeng.p2p.S61.entities.T6198;


/**
 * <实名认证查询结果>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
public class RealNameStatisticsEntity extends T6198
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8284303457292988716L;

    /** 
     * 用户名
     */
    public String userName;
    
    /** 
     * 真实姓名
     */
    public String realName;
}
