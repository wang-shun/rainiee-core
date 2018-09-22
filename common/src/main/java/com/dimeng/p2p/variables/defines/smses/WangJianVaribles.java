package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEWJ", name = "网建短信发送信息")
public enum WangJianVaribles implements VariableBean
{
    /**
     * 短信发送用户名
     */
    SMS_USER_NAME("短信发送用户名")
    {
        @Override
        public String getValue()
        {
            return "xiaocaifa";
        }
    },
    /**
     * 接口key
     */
    SMS_KEY("接口key")
    {
        @Override
        public String getValue()
        {
            return "5169ED366B6275AEB43BA859DABCE042";
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
            return "http://utf8.sms.webchinese.cn";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    WangJianVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return WangJianVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(WangJianVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
