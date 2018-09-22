package com.dimeng.p2p.modules.base.front.service;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.base.front.service.entity.InvestInfo;

/**
 * 首页TA发布、TA投资功能
 */
public interface TaManage extends Service
{
    
    /**
     * 首页TA发布
     * @return InvestInfo
     * @throws Throwable
     */
    public abstract List<InvestInfo> getPublishBids() throws Throwable;
    
    /**
     * TA投资功能
     * @return InvestInfo
     * @throws Throwable
     */
    public abstract List<InvestInfo> getInvestments() throws Throwable;
    
}
