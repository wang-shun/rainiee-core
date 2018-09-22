package com.dimeng.p2p.modules.account.console.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.entities.T6120;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.S71.entities.T7152;

/**
 * 用户管理
 */
public interface UserManage extends Service
{
    /**
     * <dt>
     * <dl>
     * 描述：修改用户状态锁定）
     * </dl>
     * 
     * @param userId
     *            用户ID
     * @param userState
     *            用户状态
     * @param lockDesc
     *            锁定原因
     * @throws Throwable
     */
    public abstract void lock(int userId, String lockDesc)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：修改用户状态解锁
     * </dl>
     * 
     * @param userId
     *            用户ID
     * @param userState
     *            用户状态
     * @param lockDesc
     *            锁定原因
     * @throws Throwable
     */
    public abstract void unLock(int userId, String lockDesc)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：拉黑
     * </dl>
     * 
     * @param id
     *            拉黑ID
     * @param blacklistDesc
     *            拉黑原因
     * @param state
     *            拉黑状态
     * @throws Throwable
     */
    public abstract void black(int id, String blacklistDesc)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：取消拉黑
     * </dl>
     * 
     * @param id
     *            拉黑ID
     * @param blacklistDesc
     *            拉黑原因
     * @param state
     *            拉黑状态
     * @throws Throwable
     */
    public abstract void unBlack(int id, String blacklistDesc)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据用户名查询用户是否存在
     * </dl>
     * 
     * @param userName
     *            用户名
     * @return {@code int} 存在就返回用户true，不存在就返回false
     * @throws Throwable
     */
    public abstract boolean isExists(String userName)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：根据用户名查询用户ID,若不存在返回0
     * </dl>
     * 
     * @param userName
     *            用户名
     * @return {@code int} 存在就返回用户ID，不存在就返回0
     * @throws Throwable
     */
    public abstract int getUserIdByName(String userName)
        throws Throwable;
    
    /**
     * 查询用户房产信息
     */
    public abstract T6112 getFc(int userId)
        throws Throwable;
    
    /**
     * 查询用户车产信息
     */
    public abstract T6113 getCc(int userId)
        throws Throwable;
    
    /**
     * 查询用户认证信息
     */
    public abstract T6120 getAuthent(int userId)
        throws Throwable;
    
    /**
     * 修改用户房产信息
     */
    public abstract void updateUserFc(T6112 t6112)
        throws Throwable;
    
    /**
     * 修改用户车产信息
     */
    public abstract void updateUserCc(T6113 t6113)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询催收记录
     * </dl>
     * 
     * @param userId
     *            用户ID
     * @return {@link T7152}{@code []} 催收列表 不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract T7152[] csjlSearch(int userId)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询用户类型
     * </dl>
     * 
     * @param userId
     *            用户ID
     * @return {@link T7152}{@code []} 催收列表 不存在则返回{@code null}
     * @throws Throwable
     */
    public abstract T6110_F06 getUserType(int userId)
        throws Throwable;
    
    /**
     * 根据用户Id判断该用户是否有投资权限
     */
    public abstract T6110_F17 getUserInvestorType(int userId)
        throws Throwable;
    
    /**
     * 根据用户账号ID查询用户名
     */
    public abstract String getUserNameById(int userId)
        throws Throwable;
    
    /**
     * 根据用户账号名查询前台用户信息
     */
    public abstract T6110 getFrontUserByName(String userName)
        throws Throwable;
    
    /**
     * 根据用户账号名查询后台用户信息
     */
    public abstract T7110 getConsoleUserByName(String userName)
        throws Throwable;
    
    /**
     * 调整用户信用额度
     */
    public abstract void updateUserCredit(int userId, BigDecimal creditAmount)
        throws Throwable;
    
    /**
     * 调整用户信用等级
     */
    public abstract void updateUserCreditLevel(int userId, String creditLevel)
        throws Throwable;
    
    /**
     * 查询用户信用额度
     */
    public abstract BigDecimal getUserCredit(int userId)
        throws Throwable;
    
    /**
     * 通过用户id查询后台用户名
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract String getAccountName(int userId)
        throws Throwable;
    
    /**
     * 删除用户
     * 
     * @param userId
     * @throws Throwable
     */
    public abstract void del(int userId)
        throws Throwable;
    
    /**
     * 
     * 是否有第三方托管账户
     */
    public abstract boolean isTg(int userId)
        throws Throwable;
    
    /**
     * 查询用户是否是机构
     */
    public abstract T6110_F10 getDb(int userId)
        throws Throwable;
    
    /**
    * 查询用户错误登录次数
    * @param userName
    * @param ip
    * @return 用户错误登录次数
    * @throws Throwable
    */
    public abstract int getUserLoginError(String userName, String ip)
        throws Throwable;
    
    /**
     * 清除登录错误次数
     * @param userName
     * @param ip
     */
    public abstract void clearErrorCount(String userName, String ip)
        throws Throwable;
    
    /**
     * 根据用户名重置登录错误次数
     * @param userName
     */
    public abstract void resetLoginErrorNum(String userName)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeLog(String type, String log)
        throws Throwable;
    
    public void clearSession(int accountId)
        throws Throwable;
    
    /**
     * 查询用户第三方开户信息
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6119 selectT6119(int userId)
        throws Throwable;
}
