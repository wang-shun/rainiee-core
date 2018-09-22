/*
 * 文 件 名:  Bdxq.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.common.entities;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;

/**
 * <标的详情>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月29日]
 */
public class Bdxq extends T6230
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1604579062099992147L;
    
    /** 
     * 等级名称
     */
    public String djmc;
    
    /** 
     * 进度
     */
    public double proess;
    
    /**
     * 筹款结束时间
     */
    public Timestamp jsTime;
}
