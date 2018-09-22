package com.dimeng.p2p.account.user.service;

import java.sql.Connection;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestionAnswer;
import com.dimeng.p2p.account.user.service.entity.Safety;

/**
 * 安全信息管理
 *
 */
public interface SafetyManage extends Service
{
    
    /**
     * 获取用户安全信息
     * @param acount
     * @return
     * @throws Throwable
     */
    public abstract Safety get()
        throws Throwable;
    
    /**
     * {@link com.dimeng.p2p.account.user.service.SafetyManage.get()}
     * 获取用户安全信息(含视频认证状态信息)
     * @return
     * @throws Throwable
     */
    public abstract Safety getSafety()
        throws Throwable;
    
    /**
     * 修改实名认证
     * @param name
     * @throws Throwable
     */
    public abstract void updateName(String name, String idcard, String status)
        throws Throwable;
    
    /**
    * 修改实名认证--是否是自然人（自然人身份证和非自然人法人身份证可以相同）
    * @param name
    * @throws Throwable
    */
    public abstract void updateNameSFZRR(String name, String idcard, String status, T6110_F06 t6110_f06)
        throws Throwable;
    
    /** 
     * 修改实名认证--企业
     * @param name
     * @param idcard
     * @param status
     * @throws Throwable
     */
    public abstract void updateNameForFZRR(String name, String idcard, String status)
        throws Throwable;
    
    /**
     * 修改密码
     * @param password
     * @throws Throwable
     */
    public abstract void updatePassword(String password)
        throws Throwable;
    
    /**
     * 修改邮箱
     * @param emil
     * @throws Throwable
     */
    public abstract void updateEmil(String emil)
        throws Throwable;
    
    /**
     * 绑定邮箱
     * @param emil
     * @throws Throwable
     */
    public abstract void bindEmil(String emil, String status)
        throws Throwable;
    
    /**
     * 通过邮箱连接地址绑定邮箱
     * @param emil
     * @throws Throwable
     */
    public abstract void bindEmail(String email, String status, int userId)
        throws Throwable;
    
    /**
     * 根据用户id和邮箱地址查询记录是否存在
     * @param email
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract boolean getEmailByUserId(String email, int userId)
        throws Throwable;
    
    /**
     * 修改手机号
     * @param phoneNumber
     * @throws Throwable
     */
    public abstract void updatePhone(String phoneNumber)
        throws Throwable;
    
    /**
     * 修改交易密码
     * @param txPassword
     * @throws Throwable
     */
    public abstract void updateTxpassword(String txPassword)
        throws Throwable;
    
    /**
     * 判断身份证是否存在
     * @param idCard
     * @return
     * @throws Throwable
     */
    public abstract boolean isIdcard(String idCard)
        throws Throwable;
    
    /**
    * 判断指定类型用户的身份证是否存在
    * @param idCard
    * @return
    * @throws Throwable
    */
    public abstract boolean isIdcard(String idCard, T6110_F06 t6110_f06)
        throws Throwable;
    
    /**
     * 判断邮箱是否存在
     * @param idCard
     * @return
     * @throws Throwable
     */
    public abstract boolean isEmil(String idCard)
        throws Throwable;
    
    /**
     * 判断手机号是否存在
     * @param idCard
     * @return
     * @throws Throwable
     */
    public abstract boolean isPhone(String idCard)
        throws Throwable;
    /**
     * 判断手机号是否存在
     * @param idCard
     * @return
     * @throws Throwable
     */
    public abstract boolean isPhone(String phone, int userId)
        throws Throwable;
    /**
     * 修改当前用户发送认证次数，且判断
     * @param sendtype
     * @return
     * @throws Throwable
     */
    public abstract boolean udpateSendTotal(String sendtype)
        throws Throwable;
    
    /**
     * 修改当前用户修改体现密码次数，且判断
     * @param sendtype
     * @return
     * @throws Throwable
     */
    public abstract void udpatetxSize()
        throws Throwable;
    
    /**
     * 交易密码是否能修改
     * @throws Throwable
     */
    public abstract void isUpdateTxmm()
        throws Throwable;
    
    /**
     * 用户认证视频
     * @param fileName
     * @param fileType
     * @param fileSize
     * @param accountId
     * @throws Throwable
     */
    public abstract void updateVideoAuth(String fileName, String fileType, int fileSize, int accountId)
        throws Throwable;
    
    /**
     * 判断是否绑定手机号
     * @return boolean:true： 绑定，false：没绑定
     * @throws Throwable
     */
    public abstract boolean isBindPhone()
        throws Throwable;
    
    /**
     * 今天本号码(phone)发送手机短信次数
     * @param phone
     * @return
     * @throws Throwable
     */
    public abstract Integer bindPhoneCount(String phone, Integer pType)
        throws Throwable;
    
    /**
     * 今天本号码(phone)匹配验证码失败次数
     * @param phone
     * @return
     * @throws Throwable
     */
    public Integer phoneMatchVerifyCodeErrorCount(String phone, Integer pType)
        throws Throwable;
    
    /**
     * 插入手机验证码匹配错误记录
     * @param phone
     * @param pType
     * @throws Throwable
     */
    public abstract void insertPhoneMatchVerifyCodeError(String phone, Integer pType, String code)
        throws Throwable;
    
    /**
     * 今天本邮箱(email)发送邮件的次数
     * @param email
     * @return
     * @throws Throwable
     */
    public abstract Integer sendEmailCount(String email, Integer pType)
        throws Throwable;
    
    /**
     * 当前登录用户今天发送邮件的次数
     * @param pType
     * @return
     * @throws Throwable
     */
    public abstract Integer userSendEmailCount(Integer pType)
        throws Throwable;
    
    /**
     * 当前登录用户今天发送手机短信的次数
     * @param pType
     * @return
     * @throws Throwable
     */
    public abstract Integer userSendPhoneCount(Integer pType)
        throws Throwable;
    
    /**
     * 当前IP今天发送手机短信的次数
     * @param ip
     * @param pType
     * @return
     * @throws Throwable
     */
    public abstract Integer iPAddrSendSmsCount(String ip, Integer pType)
        throws Throwable;
    
    /**
     * 设置密保问题
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean updatePwdSafeInfo(List<PwdSafetyQuestionAnswer> params)
        throws Throwable;
    
    /**
     * 获取用户的密码答案
     * @param userId 用户ID
     * @param questionId 问题ID
     * @return
     * @throws Throwable
     */
    public String getUserAnswerByQuestionId(int userId, int questionId)
        throws Throwable;
    
    /**
     * 获取用户待绑定的邮箱
     * @return
     * @throws Throwable
     */
    public abstract String getUnBindEmail()
        throws Throwable;
    
    /**
     * 绑定邮箱发送验证码更新邮箱地址
     * @param email
     */
    public abstract void updateT6118Email(String email)
        throws Throwable;
    
    /**
     * 修改手机号
     * @param phoneNumber
     * @param userId
     * @throws Throwable
     */
    public abstract void updatePhoneById(String phoneNumber, int acount)
        throws Throwable;
    
    /**
     * 根据手机号码查询交易密码
     * @param phoneNumber
     * @throws Throwable
     */
    public abstract String getTxPwdByPhone(String phoneNumber)
        throws Throwable;
    
    /**
     * 判断修改实名认证信息时该身份信息是否正在被认证
     * @return
     * @throws Throwable
     */
    public abstract boolean isAuthingUpdate()
        throws Throwable;
    
    /**
     * 更新错误认证次数
     * @return
     * @throws Throwable
     */
    public abstract void updateT6198F03()
        throws Throwable;
    
    /**
     * 更新认证通过时间
     * @return
     * @throws Throwable
     */
    public abstract void updateT6198F06(int accountId)
        throws Throwable;
    
    /**
     * 更新错误认证次数
     * @return
     * @throws Throwable
     */
    public abstract void udpateT6198F03(Connection connection, int userId)
        throws Throwable;
    
    /**
     * 更新认证通过时间
     * @return
     * @throws Throwable
     */
    public abstract void udpateT6198F06(Connection connection, int userId)
        throws Throwable;
    
    /**
     * 查询当日交易密码输入错误次数
     * 
     * @return int 
     * @throws Throwable 异常信息
     */
    public abstract int psdInputCount()
        throws Throwable;
    
    /**
     * 增加当日交易密码输入错误次数
     * 
     * @return int 
     * @throws Throwable 异常信息
     */
    public abstract void addInputCount()
        throws Throwable;
    
    /**
     * 判断该用户当天是否超过实名认证错误次数
     * @param userId 登录用户ID
     * @param number 限制的次数
     * @return
     * @throws Throwable
     */
    public abstract boolean isMoreThanErrorCount(int userId, int number)
        throws Throwable;
    
    
    
}
