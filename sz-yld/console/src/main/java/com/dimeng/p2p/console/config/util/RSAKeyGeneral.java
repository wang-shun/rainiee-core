package com.dimeng.p2p.console.config.util;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.cycle.Startup;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.service.PtAccountManage;

public class RSAKeyGeneral implements Startup
{

	@Override
    public void onStartup(ResourceProvider resourceProviderParam)
    {
        ServiceProvider serviceProvider = resourceProviderParam.getResource(ServiceProvider.class);
        try(ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
            try {
                ptAccountManage.addRSAKey(resourceProviderParam);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
	}
}
