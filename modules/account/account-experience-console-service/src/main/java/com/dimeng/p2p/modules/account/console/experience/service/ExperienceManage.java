package com.dimeng.p2p.modules.account.console.experience.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.modules.account.console.experience.service.entity.Experience;
import com.dimeng.p2p.modules.account.console.experience.service.entity.User;
import com.dimeng.p2p.modules.account.console.experience.service.query.AddExperienceUserQuery;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceQuery;

/**
 * 体验金管理
 * 
 * @author guopeng
 * 
 */
public abstract interface ExperienceManage extends Service {

	/**
	 * 体验金查看
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Experience get(int id) throws Throwable;

	/**
	 * 体验金导出
	 * 
	 * @param outputStream
	 * @throws Throwable
	 */
	public abstract void export(Experience[] experiences,
			OutputStream outputStream) throws Throwable;

	/**
	 * 根据用户账号名查询用户ID
	 * 
	 * @param userName
	 * @return
	 * @throws Throwable
	 */
    public abstract T6110 getIdByName(String userName)
        throws Throwable;

	/**
	 * 有效体验金共计
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal getTotalExperience() throws Throwable;

    /**
     * 体验金充值-新增选择充值人员
     * @param addExperienceUserQuery 查询条件
     * @param paging 分页
     * @return PagingResult
     * @throws Throwable
     */
    public abstract PagingResult<User> search(AddExperienceUserQuery addExperienceUserQuery, Paging paging)
            throws Throwable;

}
