package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;

public interface UserThirdPartyLoginManage extends Service
{
    
    /**
     * 
      * 〈一句话功能简述〉
      * 第三方登陆，根据第三方唯一码查询用户信息
      * @author 王义
      * @see    UserThirdPartyLoginManage.java
      * @since  (登陆模块，版本)
      * @return ThirdUser
      * @data 2014年11月28日
     */
    public ThirdUser getUserInfoByThirdID(String openID)
        throws Throwable;
    
    /**
     * 
      * 〈一句话功能简述〉
      * 第三方登陆，根据第三方唯一码查询用户信息
      * @author 王义
      * @see    UserThirdPartyLoginManage.java
      * @since  (登陆模块，版本)
      * @return ThirdUser
      * @data 2014年11月28日
     */
    public ThirdUser getUserInfoByID(int openID)
        throws Throwable;
    
    /**
     * 
      * 〈一句话功能简述〉
      * 第三方登陆，根据第三方唯一码查询用户信息
      * @author 王义
      * @see    UserThirdPartyLoginManage.java
      * @since  (登陆模块，版本)
      * @return ThirdUser
      * @data 2014年11月28日
     */
    public ThirdUser getUserInfoByUsrID(String userId, String passWord)
        throws Throwable;
    
    /**
     * 
      * 〈一句话功能简述〉
      * 第三方登陆，更新当前用户的登陆时间
      * @author 王义
      * @see    UserThirdPartyLoginManage.java
      * @since  (登陆模块，版本)
      * @return ThirdUser
      * @data 2014年11月28日
     */
    public void updateUserLoginTime(String openID)
        throws Throwable;
    
    /**
     * 根据第三方登录类型和第三方用户唯一标识
     * <功能详细描述>
     * @param openID
     * @param type
     * @throws Throwable
     */
    public void updateUserLoginTime(String openID, String type)
        throws Throwable;
    
    /**
     *
     * 〈一句话功能简述〉
     * 第三方登陆，更新当前用户的AccessToken，AccessTokenTime 和 登陆时间
     * @author 刘文豪
     * @see    UserThirdPartyLoginManage.java
     * @since  (登陆模块，版本)
     * @return ThirdUser
     * @data 2014年11月28日
     */
    void updateUserAccessTokenAndLoginTime(String openId, String qqToken)
        throws Throwable;
}
