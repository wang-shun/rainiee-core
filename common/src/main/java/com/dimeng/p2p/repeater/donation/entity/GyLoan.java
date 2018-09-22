/*
 * 文 件 名:  GyLoan.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.repeater.donation.entity;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.entities.T6244;
import com.dimeng.p2p.S62.entities.T6247;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class GyLoan extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 公益标信息
     */
    public T6242 t6242;
    
    /**
     * 公益标 扩展表
     */
    public T6243 t6243;
    
    /**
     * 公益标 协议
     */
    public T6244 t6244;
    
    /**
     * 审核记录
     */
    public List<T6247> t6247s;
    
    /**
     * 进展数量(一个标下面的进展条数)
     */
    public int progres;
    
    /**
     * 进度(标的投资百分数)
     */
    public double perCent;

    /**
     * 进度
     */
    public String perCentFormat;
    
    /**
     * 创建用户
     */
    public String sysName;
    
    /**
     * 借款用户
     */
    public String loanName;

    /**
     * 截止时间
     */
    public boolean isTimeEnd;
    
}
