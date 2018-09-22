package com.dimeng.p2p.service;

import com.dimeng.framework.service.Service;

public interface SmsSenderManageExt extends Service {
	public void send(int type, String message,String ip, String... receivers) throws Throwable;
}
