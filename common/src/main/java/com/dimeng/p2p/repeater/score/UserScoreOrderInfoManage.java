package com.dimeng.p2p.repeater.score;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.repeater.AbstractRepeaterService;
import com.dimeng.p2p.repeater.score.entity.UserScoreOrderInfoVO;

import java.util.List;


/**
 * 用户积分兑换记录service
 * @author heluzhu
 *
 */
public abstract class UserScoreOrderInfoManage extends AbstractRepeaterService {

	public UserScoreOrderInfoManage(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 获取用户积分兑换记录
	 * @param userId
	 * @param page
	 * @return
	 */
	public abstract List<UserScoreOrderInfoVO> getUserScoreOrderInfos(int userId, Paging page);
	
	/**
	 * 积分兑换商品
	 * @param user
	 * @param order
	 * @return
	 */
	public abstract void userScoreExchange(T6110 user,T6355 order);
}
