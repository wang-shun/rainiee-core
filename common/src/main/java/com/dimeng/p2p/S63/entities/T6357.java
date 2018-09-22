/*
 * 文 件 名:  T6357.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.S63.entities;

import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <积分清零设置表>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
public class T6357 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3407527717352211413L;
    
    /**
     * 自增ID
     */
    public int F01;

    /**
     * 操作用户ID,参考T7110.F01
     */
    public int F02;

    /**
     * 开始时间
     */
    public Date F03;

    /**
     * 结束时间
     */
    public Date F04;

    /**
     * 操作时间
     */
    public Timestamp F05;
}
