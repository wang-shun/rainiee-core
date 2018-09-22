package com.dimeng.p2p.pay.util;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.cycle.Startup;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.bid.queue.service.QueueManage;

public class SubscribeInitializer implements Startup {

	@Override
	public void onStartup(ResourceProvider resourceProvider) {
		ServiceProvider serviceProvider = resourceProvider
				.getResource(ServiceProvider.class);
		try (ServiceSession serviceSession = serviceProvider
				.createServiceSession()) {
			serviceSession.getService(QueueManage.class).subscribeRedisMsg();
		} catch (Throwable e) {
			resourceProvider.log(e);
		}
	}
}
