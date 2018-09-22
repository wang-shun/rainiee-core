package com.dimeng.p2p.common;

/**
 * 邮件类型
 *
 * @author zengzhihua
 */
public class EmailTypeUtil
{
    public static final int EMAIL_HKYJ = 107;                       //还款预警

    public static final String EMAIL_HKYJ_CODE = "hkyj";            //还款预警

    public static  Integer getEmailType(String type)
    {
        Integer returnType = null;
        if ("emailpwd".equals(type))
        {
            //找回密码功能：发送邮件验证码
            returnType = 101;
        }
        else if ("bind".equals(type))
        {
            //安全中心-绑定邮箱:发送验证码
            returnType = 102;
        }
        else if ("update".equals(type))
        {
            //安全中心-修改邮箱:验证原邮箱
            returnType = 103;
        }
        else if ("new".equals(type))
        {
            //安全中心-通过老邮箱修改邮箱：验证新邮箱
            returnType = 104;
        }
        else if ("pnew".equals(type))
        {
            //安全中心-通过手机号修改邮箱：验证新邮箱
            returnType = 105;
        }
        else if (EMAIL_HKYJ_CODE.equals(type)) {

            returnType = EMAIL_HKYJ;
        }
        return returnType;
    }
}
