package com.dimeng.p2p.service;

import com.dimeng.framework.service.Service;

public interface EmailSenderManageExt extends Service {
	public void send(int type, String subject, String content,String... addresses) throws Throwable;
}