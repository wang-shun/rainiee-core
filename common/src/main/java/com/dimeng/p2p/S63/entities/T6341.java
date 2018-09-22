/*
 * 文 件 名:  T6341.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年9月30日
 */
package com.dimeng.p2p.S63.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * 活动指定用户 实体
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年9月30日]
 */
public class T6341 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1743639745698259614L;
    
    /**
     * 主键ID
     */
    public int F01;
    
    /**
     * 用户ID，参考 T6110.F01
     */
    public int F02;
    
    /**
     * 活动ID，参考 T6340.F01
     */
    public int F03;
    
}
