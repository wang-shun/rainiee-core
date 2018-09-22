package com.dimeng.p2p.escrow.fuyou.service;

import java.util.ArrayList;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;

/**
 * 
 * 转账订单
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月28日]
 */
public interface OrderManage extends Service
{
    
    /**
     * 获取10条转账订单
     * @param orderType
     * @return
     * @throws Throwable
     */
    public abstract ArrayList<T6501> getToSubmit(OrderType orderType)
        throws Throwable;
    
    /**
     * 对账
     * <功能详细描述>
     * @param serviceSession
     * @param t6501
     * @param params
     * @throws Throwable
     */
    public boolean selectFuyou(ServiceSession serviceSession, T6501 t6501, Map<String, String> params)
        throws Throwable;
}
