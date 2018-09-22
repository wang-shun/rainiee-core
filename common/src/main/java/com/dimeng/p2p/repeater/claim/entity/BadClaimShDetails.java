/*
 * 文 件 名:  BadClaimShDetails.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月21日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.sql.Timestamp;

/**
 * <不良债权转让详情>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月21日]
 */
public class BadClaimShDetails extends BadClaimZrDetails
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7276907212544281206L;
    
    /**
     * 逾期天数（天）
     */
    public int lateDays;
    
    /**
     * 申请时间
     */
    public Timestamp applyTime;
}
