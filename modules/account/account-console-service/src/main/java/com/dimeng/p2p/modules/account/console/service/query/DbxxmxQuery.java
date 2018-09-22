/*
 * 文 件 名:  DbxxmxQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月8日
 */
package com.dimeng.p2p.modules.account.console.service.query;

/**
 * <担保信息明细>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月8日]
 */
public interface DbxxmxQuery
{
    
    /**
     * 机构用户id
     */
    Integer getUserId();
    
    /**
     * 借款编号
     */
    String getJkbh();
    
    /**
     * 借款标题
     */
    String getJkbt();
    
    /**
     * 开始时间
     */
    String getCreateTimeStart();
    
    /**
     * 结束时间
     */
    String getCreateTimeEnd();
    
    /**
     * 状态
     */
    String getZt();
}
