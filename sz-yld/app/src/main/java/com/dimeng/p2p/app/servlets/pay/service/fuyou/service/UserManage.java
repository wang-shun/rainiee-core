/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.service;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.escrow.fuyou.cond.UserRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserRegisterEntity;

*//**
 * 
 * 用户管理接口类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 *//*
public interface UserManage extends Service
{
    *//**
     * 验证是否绑定手机号与邮箱地址
     * <功能详细描述>
     * @return
     * @throws Throwable
     *//*
    public boolean isBandPhoneAndEmail()
        throws Throwable;
    
    *//**
     * 查询用户信息
     * <功能详细描述>
     * @return
     * @throws Throwable
     *//*
    public UserRegisterEntity selectUserInfo()
        throws Throwable;
    
    *//**
     * 生成用户开户跳转地址
     * 
     * @param cond
     *            传入的参数
     * @return 返回跳转地址
     * @throws Throwable
     *//*
    public abstract Map<String, String> createRegisterUri(UserRegisterCond cond)
        throws Throwable;
    
    *//**
     * 解析用户开户返回数据, 成功解析的必要条件
     * <功能详细描述>
     * @param params
     * @throws Throwable
     *//*
    public boolean registerReturnDecoder(Map<String, String> params)
        throws Throwable;
    
    *//**
     * 是否为自然人 heshiping
     * 
     * @date 2015.5.11
     * 
     * @return boolean
     * @throws Throwable
     *//*
    public abstract boolean isZrr()
        throws Throwable;
    
    *//**
     * 更新用户信息
     * <功能详细描述>
     *//*
    public abstract void updateUserInfo(Map<String, String> retMap)
        throws Throwable;
    
    *//**
     * 用户信息查询接口-签名数据
     * 
     * @param userQueryCond
     * @return String
     * @throws Throwable
     *//*
    public abstract void queryUserInfo(UserQueryEntity userQueryCond)
        throws Throwable;
    
    *//**
     * 解析用户信息查询返回参数
     * 
     * @param request
     * @return UserQueryResponseEntity
     * @throws Throwable
     *//*
    public abstract UserQueryResponseEntity userQueryReturnDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable;
    
    *//**
     * 加载用户信息查询-富友
     * 
     * @param Mchnt_cd
     *            商户代号
     * @param user_ids
     *            用户账号
     * @return UserQueryEntity 返回用户信息查询实体类
     * @throws Throwable
     *//*
    public abstract UserQueryEntity userChargeQuery(String Mchnt_cd, String user_ids)
        throws Throwable;
    
    *//**
     * 查询用户基本信息
     * 
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     *//*
    public T6110 selectT6110()
        throws Throwable;
    
    *//**
     * 查询用户第三方托管信息
     * 
     * @param F01
     * @return
     * @throws Throwable
     *//*
    public String selectT6119()
        throws Throwable;
    
    *//**
     * 是否实名认证 heshiping
     * 
     * @return boolean
     * @throws Throwable
     *//*
    public abstract boolean isSmrz()
        throws Throwable;
    
    *//**
     * 查询用户第三方账号是否存在
     * 
     * @param F03 手机号（第三方账号）
     * @return
     * @throws Throwable
     *//*
    public boolean selectT6119(String F03)
        throws Throwable;
    
}
*/