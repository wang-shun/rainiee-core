package com.dimeng.p2p.modules.bid.front.service.query;

import java.util.Map;


/**
 * 债权查询接口
 */
public interface TransferChildQuery extends TransferQuery{
    /**
     * 查询条件
     */
    public abstract Map<String,Object> zqConditionQry();
}
