/*
 * 文 件 名:  T6146.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月22日
 */
package com.dimeng.p2p.S61.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <逾期记录表>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月22日]
 */
public class T6146 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /** 
     * 账户ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 逾期天数
     */
    public int F03;
    
    /** 
     * 期号
     */
    public int F04;
    
    /** 
     * 债权ID,参考T6251.F01
     */
    public int F05;
}
