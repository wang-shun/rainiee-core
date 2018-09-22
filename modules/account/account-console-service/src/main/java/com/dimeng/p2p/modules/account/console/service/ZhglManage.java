package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.modules.account.console.service.entity.User;
import com.dimeng.p2p.modules.account.console.service.query.UserQuery;

/**
 * 账号管理
 * 
 */
public abstract interface ZhglManage extends Service
{
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：查询分页查询用户列表.
     * </dl>
     * 
     * @param userQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<User> search(UserQuery userQuery, Paging paging)
        throws Throwable;
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：查询用户列表.
     * </dl>
     * 
     * @param userQuery
     * @param paging
     * @return
     * @throws Throwable
     */
    PagingResult<User> searchUsers(UserQuery userQuery, Paging paging)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：新增个人用户
     * </dl>
     * </dt>
     * 
     * @param user
     *            用户信息
     * @throws Throwable
     */
    public abstract int addGr(String userName, String password, String employNum)
        throws Throwable;
    
    /**
     * 新增机构
     * @param userName
     * @param password
     * @return
     * @throws Throwable
     */
    public abstract int addJg(T6161 entity, String dbjgms, String jgjs, String lxName, String lxTel, String mobile,
        String email, T6110_F17 t6110_f17, T6110_F19 t6110_f19)
        throws Throwable;
    
    /**
     * 新增企业
     * @param userName
     * @param password
     * @return
     * @throws Throwable
     */
    public abstract int addQy(T6161 entity, String lxName, String lxTel, String mobile, String email,
        T6110_F17 t6110_f17)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：判断用户账号是否存在
     * </dl>
     * </dt>
     * 
     * @param account
     *            用户账号
     * @throws Throwable
     */
    public boolean isAccountExists(String account)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：导出账号信息
     * </dl>
     * 
     * @param users
     *            用户信息列表
     * @param outputStream
     *            输出流
     * @param charset
     *            字符编码
     * @throws Throwable
     */
    public abstract void export(User[] users, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * 新增机构(中小企业)
     * @param userName
     * @param password
     * @return
     * @throws Throwable
     */
    public abstract int addJgZxq(T6161 entity, String jgjs, String lxName, String lxTel)
        throws Throwable;
    
    /**
     * 新增企业(中小企业)
     * @param userName
     * @param password
     * @return
     * @throws Throwable
     */
    public abstract int addQyZxq(T6161 entity, String lxName, String lxTel)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：判断企业联系人电话是否存在
     * </dl>
     * </dt>
     * 
     * @param account
     *            企业联系人电话
     * @throws Throwable
     */
    public boolean isQylxrdhExist(String F07)
        throws Throwable;
    
    /**
     * 批量替换业务员
     * <功能详细描述>
     * @param employeeNumNew
     * @param employeeNumOld
     * @return
     * @throws Throwable
     */
    public abstract int replaceEmployNum(String employeeNumNew, String employeeNumOld)
        throws Throwable;
    
    /**
     * 检查业务工号是否存在
     * <功能详细描述>
     * @param connection
     * @param code
     * @return String
     * @throws Throwable
     */
    public String checkNumExist(String code)
        throws Throwable;
    /**
     * <一句话功能简述> 同步法大大客户编号
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public List<User> SynchronizeUserCode()
        throws Throwable;
    
    /**
     * <一句话功能简述> 查看用户的账号信息
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6110 getT6110(int userId)
        throws Throwable;
    
    /**
     * 查询法大大客户编号查询 用户的账号信息
     * @param fddCusNo
     * @return
     * @throws Throwable
     */
    public abstract T6110 getT6110ByCusNo(String fddCusNo)
        throws Throwable;
    
    /**
    * 更新用户法大大客户编号
    * 参数验证，调用者处理
    * @param userId
    * @param cusNo 法大大客户编号
    */
    public abstract void updateFddCusNo(int userId, String cusNo)
        throws Throwable;
    
}
