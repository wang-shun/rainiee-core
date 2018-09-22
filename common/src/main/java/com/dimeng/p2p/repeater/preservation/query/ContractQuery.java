/*
 * 文 件 名:  ContractQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.preservation.query;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6271_F07;
import com.dimeng.p2p.S62.enums.T6271_F08;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月16日]
 */
public class ContractQuery extends AbstractEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6230401201604356696L;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 姓名/企业名称
     */
    public String realName;
    
    /**
     * 保全ID
     */
    public String preservationId;
    
    /**
     * 标的ID
     */
    public String bidId;
    
    /**
     * 借款标题
     */
    public String loanTitle;
    
    /**
     * 合同编号
     */
    public String contractNum;
    
    /**
     * 保全状态
     */
    public T6271_F07 preservationStatus;
    
    /**
     * 开始时间
     */
    public String startTime;
    
    /**
     * 结束时间
     */
    public String endTime;
    
    /**
     * 合同类型：JKHT-借款合同，ZQZRHT-债权转让合同，DFHT-垫付合同，BLZQZRHT-不良债权转让合同
     */
    public T6271_F08 contractType;
}
