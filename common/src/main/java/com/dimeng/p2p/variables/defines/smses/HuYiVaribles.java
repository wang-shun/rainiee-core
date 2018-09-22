package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEHY", name = "互易短信发送信息")
public enum HuYiVaribles implements VariableBean
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
     * 短信发送地址
     */
    SMS_URI("短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
        }
    },
    /**
     * 批量短信发送用户名
     */
    BATCH_SMS_USERNAME("批量短信发送用户名")
    {
        @Override
        public String getValue()
        {
            return "abc";
        }
    },
    /**
     * 批量短信发送密码
     */
    BATCH_SMS_PASSWORD("批量短信发送密码")
    {
        @Override
        public String getValue()
        {
            return "abc";
        }
    },
    /**
     * 互亿无线短信接口产品ID
     */
    HYWX_PID("互亿无线短信接口产品ID")
    {
        @Override
        public String getValue()
        {
            return "32";
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
            return "[迪蒙网贷]";
        }
    },
    /**
     * 批量短信发送地址
     */
    BATCH_SMS_URI("批量短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://10658.cc/webservice/api?method=SendSms";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    HuYiVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return HuYiVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(HuYiVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
