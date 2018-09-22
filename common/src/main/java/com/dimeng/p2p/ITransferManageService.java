/*
 * 文 件 名:  ITransferManageService.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huguangze
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S65.entities.T6517;

/**
 * 转账管理服务
 * 
 * @author  huguangze
 * @version  [版本号, 2015年11月23日]
 */
public interface ITransferManageService extends Service
{
    /**
     * 查询转账订单列表
     * @return
     * @throws Throwable
     */
    List<T6517> findTransferOrderList() throws Throwable;
}
