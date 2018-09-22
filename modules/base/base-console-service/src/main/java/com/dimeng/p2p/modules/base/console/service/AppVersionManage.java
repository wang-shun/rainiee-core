package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;

/**
 * App版本管理
 * 
 */
public interface AppVersionManage extends Service {
	/**
	 * 添加app版本
	 * @return 
	 * @throws Throwable
	 */
	public abstract int addAppVersion(AppVersion appVer,  ResourceProvider rp) throws Throwable;
	/**
	 * 修改app版本
	 * @throws Throwable
	 */
	public abstract void updateAppVersion(AppVersion appVer,int id , ResourceProvider rp) throws Throwable;
	    
    /**
     * 查询app版本
     * @return 版本数组
     * @throws Throwable
     */
	public abstract AppVersionInfo[] searchAppVersion(AppVersion appVer, int id) throws Throwable;
    
    /**
     * 查询app版本
     * @return 版本分页对象
     * @throws Throwable
     */
    public abstract PagingResult<AppVersionInfo> searchAppVersionPaging(AppVersion query, Paging paging)
        throws Throwable;
	/**
	 * 删除app版本
	 * @return 
	 * @throws Throwable
	 */
	public abstract void delAppVersion(int id) throws Throwable;
}
