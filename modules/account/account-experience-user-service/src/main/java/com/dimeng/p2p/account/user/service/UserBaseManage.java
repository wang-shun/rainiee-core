package com.dimeng.p2p.account.user.service;

import java.sql.SQLException;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6142;
import com.dimeng.p2p.S61.entities.T6143;
import com.dimeng.p2p.account.user.service.entity.UserBase;

/**
 * 个人基本信息
 * 
 * @author Administrator
 * 
 */
public interface UserBaseManage extends Service {

	/**
	 * 查询其他信息
	 * 
	 * @param acount
	 *            用户ID
	 * @return
	 */
	public abstract UserBase getUserBase() throws Throwable;


	/**
	 * 上传图像
	 * 
	 * @param up
	 * @throws Throwable
	 */
	public abstract void upload(UploadFile file) throws Throwable;
	
	
	/**
	 * 新增学历信息
	 * @param entity 学历信息
	 * @throws Throwable
	 */
	public abstract int addXlxx(T6142 entity) throws Throwable;
	
	/**
	 * 通过ID得到学历信息
	 * @param id 学历信息ID
	 * @return
	 * @throws Throwable
	 */
	public abstract T6142 getXlxx(int id) throws Throwable;
	
	/**
	 * 修改学历信息
	 * @param entity
	 * @throws Throwable
	 */
	public abstract void updateXlxx(T6142 entity) throws Throwable;
	/**
	 * 得到学历分页信息
	 * @param page
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6142> seachXlxx(Paging paging) throws Throwable;
	
	/**
	 * 新增房产信息
	 * @param entity 房产信息
	 * @throws Throwable
	 */
	public abstract int addFcxx(T6112 entity) throws Throwable;
	
	/**
	 * 通过ID得到房产信息
	 * @param id 房产信息ID
	 * @return
	 * @throws Throwable
	 */
	public abstract T6112 getFcxx(int id) throws Throwable;
	
	/**
	 * 修改房产信息
	 * @param entity
	 * @throws Throwable
	 */
	public abstract void updateFcxx(T6112 entity) throws Throwable;
	/**
	 * 得到房产分页信息
	 * @param page
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6112> seachFcxx(Paging paging) throws Throwable;
	
	
	
	/**
	 * 新增车产信息
	 * @param entity 车产信息
	 * @throws Throwable
	 */
	public abstract int addCcxx(T6113 entity) throws Throwable;
	
	/**
	 * 通过ID得到车产信息
	 * @param id 车产信息ID
	 * @return
	 * @throws Throwable
	 */
	public abstract T6113 getCcxx(int id) throws Throwable;
	
	/**
	 * 修改车产信息
	 * @param entity 车产信息
	 * @throws Throwable
	 */
	public abstract void updateCcxx(T6113 entity) throws Throwable;
	/**
	 * 得到车产分页信息
	 * @param page
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6113> seachCcxx(Paging paging) throws Throwable;
	
	
	/**
	 * 新增工作信息
	 * @param entity 工作信息
	 * @throws Throwable
	 */
	public abstract int addGzxx(T6143 entity) throws Throwable;
	
	/**
	 * 通过ID得到工作信息
	 * @param id 工作信息ID
	 * @return
	 * @throws Throwable
	 */
	public abstract T6143 getGzxx(int id) throws Throwable;
	
	/**
	 * 修改工作信息
	 * @param entity 工作信息
	 * @throws Throwable
	 */
	public abstract void updateGzxx(T6143 entity) throws Throwable;
	/**
	 * 得到工作分页信息
	 * @param page
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6143> seachGzxx(Paging paging) throws Throwable;
	 /**
     * 查询区域名称
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public abstract String getRegion(int id) throws Throwable ;
}
