package com.dimeng.p2p.modules.bid.front.service;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6281;
import com.dimeng.p2p.S62.entities.T6282;
import com.dimeng.p2p.S62.entities.T6284;

/**
 * 融资意向
 * 
 */
public interface BidWillManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：新增企业融资意向.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param t6281
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(T6281 t6281) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：新增个人融资意向.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param t6282
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(T6282 t6282) throws Throwable;

	/**
	 * 得到所有可用标的类型
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract T6211[] getBidTypeAll() throws Throwable;

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 * @throws Throwable
	 */
	public abstract void addAnnex(int id,UploadFile file) throws Throwable;

	/**
	 * 添加我要找项目
	 * 
	 * @param t6284
	 * @return
	 * @throws Throwable
	 */
	public abstract int addWyzxm(T6284 t6284) throws Throwable;
}
