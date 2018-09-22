package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGESXT", name = "闪信通短信发送信息")
public enum ShanXinTongVaribles implements VariableBean
{
    /**
    * 短信发送用户id
    */
    SMS_USER_NAME("短信发送用户名")
    {
        @Override
        public String getValue()
        {
            return "900735";
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
            return "900735";
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
            return "【康达来利】";
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
            return "http://119.145.253.67:8080/edeeserver/sendSMS.do";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    ShanXinTongVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String getKey()
    {
        return key;
    }
    
    @Override
    public String getType()
    {
        return ShanXinTongVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(ShanXinTongVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
