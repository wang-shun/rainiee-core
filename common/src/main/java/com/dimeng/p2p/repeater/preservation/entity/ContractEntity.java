/*
 * 文 件 名:  ContractEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.preservation.entity;

import com.dimeng.p2p.S62.entities.T6271;

/**
 * <一句话功能简述> 列表展示实体
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月16日]
 */
public class ContractEntity extends T6271
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6963954350910184213L;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 用户名称/企业名称
     */
    public String realName;
    
    /**
     * 标的编号
     */
    public String bidNum;
    
    /**
     * 借款标题
     */
    public String loanTitle;
    
}
