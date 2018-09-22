package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 三三得九短信常量
 */
//@VariableTypeAnnotation(id = "339SMSSSJ", name = " 三三得九短信常量")
public enum ThreeThreeNineVariable implements VariableBean
{
    /**
     * 通联托管商户号
     */
    SMS_GATEWAY("三三得九短信接口地址")
    {
        @Override
        public String getValue()
        {
            return "http://ws.iems.net.cn/GeneralSMS/ws/SmsInterface?wsdl";
        }
        
    },
    SMS_USER_NAME("三三得九短信接口用户名")
    {
        @Override
        public String getValue()
        {
            return "68314:admin";
        }
        
    },
    SMS_USER_PASSWORD("三三得九短信接口用户密码")
    {
        @Override
        public String getValue()
        {
            return "11451169";
        }
        
    };
    
    protected final String key;
    
    protected final String description;
    
    ThreeThreeNineVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return ThreeThreeNineVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(ThreeThreeNineVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
