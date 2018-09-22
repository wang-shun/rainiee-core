package com.dimeng.p2p.modules.bid.user.service.query;

import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.enums.CreditStatus;

/**
 * 标查询接口
 */
public interface QyBidQuery
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
}
