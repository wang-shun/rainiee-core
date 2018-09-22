package com.dimeng.p2p.common;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.cycle.Startup;

public class ResourceProviderUtil implements Startup
{
    private static ResourceProvider resourceProvider;
    
    public static ResourceProvider getResourceProvider()
    {
        return resourceProvider;
    }
    
    public static void setResourceProvider(ResourceProvider context)
    {
        ResourceProviderUtil.resourceProvider = context;
    }
    
    @Override
    public void onStartup(ResourceProvider resourceProviderParam)
    {
        resourceProvider = resourceProviderParam;
    }
}
