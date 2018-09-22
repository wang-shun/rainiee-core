/**
 * 
 */
package com.dimeng.p2p.order;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceProvider;

/**
 * 合同保全方法
 * @author dengwenwu
 *
 */
public abstract class AbstractPreservationExecutor extends Resource {
protected final Logger logger = Logger.getLogger(getClass());
    
    /** 
     * <默认构造函数>
     */
    public AbstractPreservationExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    /**
     * 合同保全
     * @param preservationId 合同保全ID
     */
    public abstract void contractPreservation(int preservationId) throws Throwable;
    
    /**
     * 协议保全
     * @param preservationId 协议保全ID
     */
    public abstract void agreementPreservation(int preservationId) throws Throwable;
}
