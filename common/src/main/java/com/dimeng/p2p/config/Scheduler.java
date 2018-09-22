package com.dimeng.p2p.config;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.cycle.Startup;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.service.PtAccountManage;

public class Scheduler implements Startup {

	@Override
	public void onStartup(ResourceProvider resourceProvider) {
		ServiceProvider serviceProvider = resourceProvider
				.getResource(ServiceProvider.class);
		try (ServiceSession serviceSession = serviceProvider
				.createServiceSession()) {
            PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
			ptAccountManage.addPtAccount();
		} catch (Throwable e) {
			resourceProvider.log(e);
		}
	}
}
