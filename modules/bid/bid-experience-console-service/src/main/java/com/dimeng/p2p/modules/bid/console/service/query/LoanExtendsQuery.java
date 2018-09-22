package com.dimeng.p2p.modules.bid.console.service.query;

import com.dimeng.p2p.S62.enums.T6231_F35;

public abstract interface LoanExtendsQuery extends LoanQuery
{
    
    /**
     * 标编号，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getBidNo();
    
    /**
     *用户类型
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getUserType();
    
    public abstract int getProductId();
    
    /**
     * 标识类型
     * @return
     */
    public abstract String getBidFlag();
    
    /**
     * 标的属性
     * <功能详细描述>
     * @return
     */
    public abstract String getBidAttr();
    
    /**
     * 来源
     * <功能详细描述>
     * @return
     */
    public abstract T6231_F35 getSource();
    
}
