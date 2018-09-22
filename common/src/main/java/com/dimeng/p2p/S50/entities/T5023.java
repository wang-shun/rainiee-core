/*
 * 文 件 名:  T5023.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5023_F02;

/**
 * <功能说明>
 * <功能详细描述>
 * 
 * @author  God
 * @version  [版本号, 2015年11月9日]
 */
public class T5023 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5303950277521236510L;
    
    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 功能说明类型
     */
    public T5023_F02 F02;

    /** 
     * 内容
     */
    public String F03;
    
    /** 
     * 创建时间
     */
    public Timestamp F04;

    /** 
     * 最后更新时间
     */
    public Timestamp F05;
}
