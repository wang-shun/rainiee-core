package com.dimeng.p2p.repeater.score;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.repeater.AbstractRepeaterService;

import java.util.List;

/**
 * 用户收货信息service
 * @author heluzhu
 *
 */
public abstract class UserReceiveInfoManage extends AbstractRepeaterService {

	public UserReceiveInfoManage(ServiceResource serviceResource) {
		super(serviceResource);
	}

	/**
	 * 新增收货信息
	 * @param info
	 * @return
	 */
	public abstract  void addReceiveInfo(T6355 info);
	
	/**
	 * 获取用户收货信息列表
	 * @param userId
	 * @return
	 */
	public abstract  List<T6355> queryReceiveInfo(int userId);
	
	/**
	 * 修改用户收货信息
	 * @param info
	 * @return
	 */
	public abstract  void updateReceiveInfo(T6355 info);
	/**
	 * 根据ID查询收货信息
	 * @param id
	 * @return
	 */
	public abstract  T6355 queryById(int id);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public abstract  boolean deleteReceive(int id);
}
