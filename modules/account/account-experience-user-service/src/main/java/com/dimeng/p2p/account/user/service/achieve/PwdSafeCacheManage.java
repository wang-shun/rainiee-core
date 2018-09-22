package com.dimeng.p2p.account.user.service.achieve;

import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion;

public interface PwdSafeCacheManage extends Service{

	public Map<Integer, PwdSafetyQuestion> getPasswordQuestionMap();
	
	public Map<String, List<PwdSafetyQuestion>> getPasswordQuestionTypeMap();
	
	public List<PwdSafetyQuestion> getPasswordQuestionByUser(int userId) throws Throwable ;
	
	public List<PwdSafetyQuestion> getQuestionList();
}
