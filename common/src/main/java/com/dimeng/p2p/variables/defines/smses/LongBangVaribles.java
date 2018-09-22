package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGELB", name = "龙邦短信发送信息")
public enum LongBangVaribles implements VariableBean
{
    /**
     * 龙邦快信短信接口任务命令
     * send:发送，overage：余额及已发送量查询，
     * checkkeyword：非法关键词查询，query，状态报告
     */
    ACTION("龙邦快信任务命令")
    {
        @Override
        public String getValue()
        {
            return "send";
        }
    },
    /**
     * 龙邦快信短信接口userid
     */
    LB_USER_ID("龙邦快信短信接口userid")
    {
        @Override
        public String getValue()
        {
            return "137";
        }
    },
    /**
     * 短信发送用户id
     */
    SMS_USER_ID("短信发送用户id")
    {
        @Override
        public String getValue()
        {
            return "JC2083";
        }
    },
    /**
     * 短信发送用户密码
     */
    SMS_USER_PASSWORD("短信发送用户密码")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    /**
     * 短信发送地址
     */
    SMS_URI("短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://www.Lbdx.net/sms.aspx";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    LongBangVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return LongBangVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return key;
    }
    
    @Override
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(LongBangVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
        {
            StringBuilder builder = new StringBuilder();
            char[] cbuf = new char[1024];
            int len = reader.read(cbuf);
            while (len > 0)
            {
                builder.append(cbuf, 0, len);
                len = reader.read(cbuf);
            }
            return builder.toString();
        }
        catch (Throwable t)
        {
        }
        return null;
    }
    
    @Override
    public boolean isInit()
    {
        return false;
    }
}
