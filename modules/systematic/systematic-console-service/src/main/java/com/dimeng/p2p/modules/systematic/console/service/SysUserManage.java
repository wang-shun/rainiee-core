package com.dimeng.p2p.modules.systematic.console.service;

import java.sql.SQLException;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.systematic.console.service.entity.IndexCount;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysUser;
import com.dimeng.p2p.modules.systematic.console.service.query.SysUserQuery;

public abstract interface SysUserManage extends Service
{
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：添加系统用户账号
     * </dl>
     * 
     * 
     * 
     * @param accountName
     *            账号
     * @param password
     *            账号密码
     * @param name
     *            真实姓名
     * @param status
     *            状态
     * @return {@code int } 自增ID
     * @throws Throwable
     */
    public abstract int add(String accountName, String password, String name, String status, String phone, String pos,
        String employeeNum, int roleId, String dept)
        throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：根据ID查询系统用户详细
     * </dl>
     * 
     * 
     * @param id
     *            主键ID
     * @return {@link SysUser} 系统用户信息
     * @throws Throwable
     */
    public abstract SysUser get(int id)
        throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：根据条件分页查询所有系统用户信息
     * </dl>
     * 
     * 
     * 
     * 
     * @param query
     *            查询条件
     * @param paging
     *            分页参数
     * @return {@link PagingResult}{@code <}{@link SysUser}{@code >} 查询结果
     * @throws Throwable
     */
    public abstract PagingResult<SysUser> serarch(SysUserQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：修改系统用户账号
     * </dl>
     * 
     * 
     * 
     * @param id
     *            账号ID
     * @param password
     *            账号密码
     * @param name
     *            真实姓名
     * @param status
     *            状态
     * @throws Throwable
     */
    public abstract void update(int id, String name, String status, String phone, String pos,
        int roleId, String empployNum, String dept)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述： 后台首页管理
     * </dl>
     * </dt>
     * 
     * @return IndexCount 首页统计对象
     * @throws Throwable
     *             数据库异常
     */
    public IndexCount getIndexCount()
        throws Throwable;
    
    /**
     * 后台管理首页统计数据
     * 
     * @return
     * @throws Throwable
     */
    public IndexCount getIndexCountExt()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：查询用户是不是第一次登陆平台
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
     * <li>返回{@code boolean} true为是第一次登陆</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return
     * @throws Throwable
     */
    public abstract boolean isOneLogin()
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：记录登录日志.
     * </dl>
     * </dt>
     * 
     * @param accountName
     *            账号名称
     * @param ip
     *            登录IP
     * @param success
     *            是否成功
     * @throws Throwable
     * 
     */
    public abstract void log(int accountId, String ip)
        throws Throwable;
    
    /**
     * 描述： 修改登录密码
     * 
     * @param oldPassWord
     *            原密码
     * @param newPassWord
     *            新密码
     * @throws Throwable
     * 
     */
    public void updatePassWord(String oldPassWord, String newPassWord)
        throws Throwable;
    
    /**
     * 查询出登录用户ID
     * 
     * @param accountName
     * @param password
     * @return
     * @throws Throwable
     */
    public abstract int readAccountId(String accountName, String password)
        throws AuthenticationException, SQLException;
    
    /**
     * 根据用户名重置后台用户登录错误次数
     * @param userName
     */
    public abstract void resetConsoleLoginErrorNum(String userName)
        throws Throwable;
    
    /**
     * 修改管理员密码
     * <功能详细描述>
     * @param id
     * @param password
     * @throws Throwable
     */
    public abstract void updateUserPwd(int id,String password) throws Throwable;
}
