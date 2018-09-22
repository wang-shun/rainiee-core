package com.dimeng.p2p.common;

/**
 * 短信类型
 *
 * @author zengzhihua
 */
public class PhoneTypeUtil
{
    public static final int PHONE_HKYJ = 301; //还款预警
    
    public static final String PHONE_HKYJ_CODE = "hbdj"; //还款预警
    
    public static Integer getPhoneType(String type)
    {
        Integer returnType = null;
        if ("bind".equals(type))
        {
            //绑定手机号码
            returnType = 201;
        }
        else if ("phoneemil".equals(type))
        {
            //安全中心-修改邮箱：通过手机修改邮箱
            returnType = 202;
        }
        else if ("update".equals(type))
        {
            //安全中心-修改手机号：验证原手机
            returnType = 203;
        }
        else if ("getoldpas".equals(type))
        {
            //安全中心-找回交易密码：发送手机验证码
            returnType = 204;
        }
        else if ("new".equals(type))
        {
            //安全中心-通过老手机修改手机号：验证新手机号
            returnType = 205;
        }
        else if ("cnew".equals(type))
        {
            //安全中心-通过身份证修改手机号：验证新手机号
            returnType = 206;
        }
        else if ("phonepwd".equals(type))
        {
            //找回密码：发送手机验证码
            returnType = 207;
        }
        else if ("securitypwd".equals(type))
        {
            //找回密码：发送手机验证码
            returnType = 208;
        }
        else if (PHONE_HKYJ_CODE.equals(type))
        {
            
            returnType = PHONE_HKYJ;
        }
        return returnType;
    }
}
