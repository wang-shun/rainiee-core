package com.dimeng.p2p.repeater.score;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.repeater.AbstractRepeaterService;

/**
 * 用户积分账户service
 * @author heluzhu
 *
 */
public abstract class UserScoreAccountManage extends AbstractRepeaterService {


	public UserScoreAccountManage(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 获取用户积分信息
	 * @param userId
	 * @return
	 */
	public abstract T6105 getUserScoreAccount(int userId);
}
