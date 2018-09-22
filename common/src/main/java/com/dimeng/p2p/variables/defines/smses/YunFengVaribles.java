package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEYF", name = "云峰短信发送信息")
public enum YunFengVaribles implements VariableBean
{
    /**
     * 接口key
     */
    SMS_KEY("接口key")
    {
        @Override
        public String getValue()
        {
            return "184fys***orh";
        }
    },
    /**
     * 接口secret
     */
    SMS_SECRET("接口secret")
    {
        @Override
        public String getValue()
        {
            return "c9f7470***41e";
        }
    },
    /**
     * 短信平台用户名
     */
    SMS_PT_NAME("短信平台用户名")
    {
        @Override
        public String getValue()
        {
            return "nuoxinsh";
        }
    },
    /**
     * 签名
     */
    SMS_SIGN("签名")
    {
        @Override
        public String getValue()
        {
            return "【新源宝】";
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
            return "http://api.cloudfeng.com/api/v1/manySend";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    YunFengVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return YunFengVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(YunFengVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
