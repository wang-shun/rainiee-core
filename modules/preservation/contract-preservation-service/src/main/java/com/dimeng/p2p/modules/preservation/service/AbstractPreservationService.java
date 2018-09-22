/*
 * 文 件 名:  AbstractPreservationService.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月17日
 */
package com.dimeng.p2p.modules.preservation.service;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;

/**
 * <一句话功能简述> abstract抽象类
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月17日]
 */
public abstract class AbstractPreservationService extends AbstractP2PService
{
    
    /** 
     * <默认构造函数>
     */
    public AbstractPreservationService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
}
