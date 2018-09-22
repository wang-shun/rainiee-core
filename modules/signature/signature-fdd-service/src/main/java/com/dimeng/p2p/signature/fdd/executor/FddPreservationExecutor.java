package com.dimeng.p2p.signature.fdd.executor;

import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.order.PreservationExecutor;

/**
 * 法大大电子签执行类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月24日]
 */
@AchieveVersion(version = 20171024)
@ResourceAnnotation
public class FddPreservationExecutor extends PreservationExecutor 
{

    public FddPreservationExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public void contractPreservation(int preservationId) throws Throwable {
        
    }
    
}
