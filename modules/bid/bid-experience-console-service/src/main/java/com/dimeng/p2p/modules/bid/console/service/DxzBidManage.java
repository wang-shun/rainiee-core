package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6217;
import com.dimeng.p2p.modules.bid.console.service.entity.DxzInfo;
import com.dimeng.p2p.modules.bid.console.service.query.DxzQuery;

/**
 * 
 * 定向组标的接口
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public abstract interface DxzBidManage extends Service
{
    /**
     * 获取定向组分页信息
     * @param dxzQuery 查询条件
     * @param paramPaging 分页参数
     * @return 
     * @throws Throwable
     */
    public abstract PagingResult<DxzInfo> search(DxzQuery dxzQuery, Paging paramPaging)
        throws Throwable;
    
    /**
     * 验证定向组ID或定向组名称 是否存在
     * @param checkType 验证类型：dxzId:定向组ID; dxzName:定向组名称
     * @param dxzIdOrName 验证值
     * @param F01 id （如果id>0,则表示不包括当前定向组）
     * @return true:表示存在；false:不存在
     * @throws Throwable
     */
    public boolean isExist(String checkType, String dxzIdOrName, int F01)
        throws Throwable;
    
    /**
     * 新增定向组信息
     * @param t6217 定向组信息
     * @throws Throwable
     */
    public void addT6217(T6217 t6217)
        throws Throwable;
    
    /**
     * 修改定向组信息
     * @param t6217
     * @throws Throwable
     */
    public void updateT6217(T6217 t6217)
        throws Throwable;
    
    /**
     * @param dxzId id
     * @param status 状态 
     * @throws Throwable
     */
    public void updateT6217Status(int dxzId, String status)
        throws Throwable;
    
    /**
     * 根据定向组id查询定向组信息
     * @param F01 id 
     * @return
     * @throws Throwable
     */
    public T6217 getT6217(int F01)
        throws Throwable;
}
