package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.InvestType;

/**
 * 标查询接口
 */
public interface QyBidTypeQuery
{
    
    /**
     * 年化利率
     * 
     */
    public abstract int getRate();
    
    /**
     * 进度
     * 
     */
    public abstract int getJd();
    
    /**
     * 状态
     * 
     */
    public abstract T6230_F20 getStatus();
    
    /**
     * 0为默认排序，1为按年化利率排序，2为进度排序
     * 
     * @return {@link CreditStatus}
     */
    public abstract int getOrder();
    
    /**
     * 查询借款标类型.
     * 
     * @return {@link InvestType}
     */
    public abstract T6211[] getType();
}
