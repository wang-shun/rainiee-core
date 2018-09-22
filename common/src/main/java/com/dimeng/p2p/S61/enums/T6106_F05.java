package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * 积分类型
 */
public enum T6106_F05
{
    
    register("注册"), sign("签到"), invite("邀请"), invest("投资"), cellphone("手机认证"), mailbox("邮箱认证"), realname("实名认证"), trusteeship(
        "开通第三方托管账户"), charge("充值"), chargeScore("积分充值"), buygoods("现金购买商品积分"), nopassreturn("审核不通过返还积分");
    
    protected final String chineseName;
    
    private T6106_F05(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName()
    {
        return chineseName;
    }
    
    /**
     * 解析字符串.
     *
     * @return {@link T6106_F05}
     */
    public static final T6106_F05 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6106_F05.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
