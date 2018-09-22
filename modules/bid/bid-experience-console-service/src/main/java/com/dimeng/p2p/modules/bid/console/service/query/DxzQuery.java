
package com.dimeng.p2p.modules.bid.console.service.query;

/**
 * 
 * 定向组列表查询条件实体
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public interface DxzQuery
{
    /**
     * 定向组id
     * @return
     */
    public abstract String getDxzId();
    
    /**
     * 定向组名称
     * @return
     */
    public abstract String getDxzTitle();
    
}
