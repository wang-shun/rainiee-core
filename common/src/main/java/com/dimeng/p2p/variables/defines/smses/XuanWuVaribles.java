package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEXW", name = "玄武短信发送信息")
public enum XuanWuVaribles implements VariableBean
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
     * 短信发送端口号
     */
    SMS_PORT("短信发送端口号")
    {
        @Override
        public String getValue()
        {
            return "10657******";
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
            return "http://61.145.229.29:9002/MWGate/wmgw.asmx/MongateCsSpSendSmsNew";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    XuanWuVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return XuanWuVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(YiLiXiongFengVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
