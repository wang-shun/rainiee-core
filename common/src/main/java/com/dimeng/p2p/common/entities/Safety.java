package com.dimeng.p2p.common.entities;


/**
 * 安全信息
 * @author Administrator
 *
 */
public class Safety
{
    
    /**
     * 昵称
     */
    public String username;
    
    /**
     * 是否实名认证
     */
    public String isIdCard;
    
    /**
     * 身份证号
     */
    public String idCard;
    
    /**
     * 登陆密码
     */
    public String password;
    
    /**
     * 绑定邮箱
     */
    public String emil;
    
    /**
     * 绑定手机号码
     */
    public String phoneNumber;
    
    /**
     * 交易密码
     */
    public String txpassword;
    
    /**
     * 	真实姓名
     */
    public String name;
    
    /**
     * 邮箱是否验证
     */
    public String isEmil;
    
    /**
     * 加密的身份证号
     */
    public String sfzh;
    
    /**
     * 是否视频认证, TG:通过;BTG:不通过;DSH:待审核
     */
    public String videoAuth;
    
    /**
     * 审核意见
     */
    public String videoAuthOpinion;
    
    /**
     * 手机是否验证
     */
    public String isPhone;
    
    /**
     * 企业名称
     */
    public String qyName;
    
    /**
     * 性别
     */
    public String sex;
    
    /**
     * 出生日期
     */
    public String birthday;
    
    /**
    * 是否密保验证
    */
    public String isPwdSafety;
    
    /**
     * 用户类型,ZRR:自然人;FZRR:非自然人
     */
    public String userType;
}
