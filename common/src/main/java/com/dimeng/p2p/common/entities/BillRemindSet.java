/*
 * 文 件 名:  BillRemindSet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年8月18日
 */
package com.dimeng.p2p.common.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <账单提醒设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年8月18日]
 */
public class BillRemindSet extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5154957556564076323L;
    
    //还款天数
    public String[] hkDay;
    
    //逾期天数
    public String[] yqDay;
    
    //还款提醒方式
    public String[] hktxType;
    
    //还款状态
    public String hkStatus;
    
    //逾期提醒方式
    public String[] yqtxType;
    
    //逾期状态
    public String yqStatus;
}
