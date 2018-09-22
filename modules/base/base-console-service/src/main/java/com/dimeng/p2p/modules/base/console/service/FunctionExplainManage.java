/*
 * 文 件 名:  FunctionExplainManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.modules.base.console.service.entity.ExplainInfo;

/**
 * <功能说明管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
public interface FunctionExplainManage extends Service
{
    
    /**
     * 添加充值说明
     * @param explainInfo 线上实体
     * @param explainInfoLine 线下实体
     * @throws Throwable
     */
    void addCharge(ExplainInfo explainInfo, ExplainInfo explainInfoLine)
        throws Throwable;
    
    /**
     * 添加提现说明
     * @param explainInfo 提现实体
     * @throws Throwable
     */
    void addWithDraw(ExplainInfo explainInfo)
        throws Throwable;
    
    /**
     * @param F01 功能说明ID
     * @param t5023 功能说明
     * @throws Throwable
     */
    void updateCharge(ExplainInfo explainInfo, ExplainInfo explainInfoLine)
        throws Throwable;
    
    /**
     * @param F01 功能说明ID
     * @param t5023 功能说明
     * @throws Throwable
     */
    void updateWithDraw(ExplainInfo explainInfo)
        throws Throwable;
    
    /**
     * @param F02 功能说明类型
     * @throws Throwable
     */
    T5023 get(T5023_F02 F02)
        throws Throwable;
    
    /**
     * 是否支持线下充值
     * @throws Throwable
     */
    boolean isXxCharge()
        throws Throwable;
}
