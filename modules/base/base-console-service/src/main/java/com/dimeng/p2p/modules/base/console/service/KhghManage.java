/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7166;
import com.dimeng.p2p.S71.enums.T7166_F07;
import com.dimeng.p2p.modules.base.console.service.entity.KhghEntity;

/**
 * 客户关怀底层服务接口
 * @author xiaolang
 * @date 2014年7月12日 上午11:04:21
 */
public interface KhghManage extends Service {
	/**
	 * 分页查询关怀列表
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	PagingResult<KhghEntity> search(String title, String beginTime, String endTime,Paging paging) throws Throwable;
	
	/**
	 * 根据ID检索关怀项
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	T7166 get(int id) throws Throwable;
	
	/**
	 * 新增客户关怀
	 * @param t7166
	 * @throws Throwable
	 */
	int add(T7166  t7166) throws Throwable;
	
	/**
	 * 根据ID更新关怀项
	 * @param t7166
	 * @throws Throwable
	 */
	void update(T7166  t7166) throws Throwable;
	
	/**
	 * 根据ID删除关怀项
	 * @param id
	 * @throws Throwable
	 */
	void delete(int id) throws Throwable;
	/**
	 * 根据ID设置记录状态
	 * @param id
	 * @param t7166_F07
	 * @throws Throwable
	 */
	void changeStatus(int id, T7166_F07 t7166_F07) throws Throwable;
}
