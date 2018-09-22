package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "XYSMSXY", name = "兴业短信平台")
public enum XingYeVaribles implements VariableBean
{
    
    /**
     * 短信发送
     */
    SMS_TOP("短信发送")
    {
        @Override
        public String getValue()
        {
            return "2449";
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
            return "95561";
        }
    },
    
    /**
     * 短信发送用户id
     */
    SMS_USERNAME("短信发送用户名")
    {
        @Override
        public String getValue()
        {
            return "xyxfjr";
        }
    },
    
    /**
     * 短信发送用户密码
     */
    SMS_PASSWORD("短信发送用户密码")
    {
        @Override
        public String getValue()
        {
            return "xyxfjr";
        }
    },
    /**
     * 签名
     */
    SMS_SERVICE_ID("服务ID")
    {
        @Override
        public String getValue()
        {
            return "2309";
        }
    },
    /**
     * 通道标识，0=梦网短信平台  3=卡中心106，4=总行95561，5=银联，允许为空
     */
    SMS_CHANNEL("通道标识")
    {
        @Override
        public String getValue()
        {
            return "4";
        }
    },
    /**
     * 平台服务器地址
     */
    SMS_SERVICE_IP("平台服务器地址")
    {
        @Override
        public String getValue()
        {
            return "172.16.8.32";
        }
    },
    
    /**
     * 平台服务器端口
     */
    SMS_SERVICE_PORT("平台服务器端口")
    {
        @Override
        public String getValue()
        {
            return "9002";
        }
    },
    
    /**
     * 是否长短信
     */
    SMS_ISLONG("是否长短信")
    {
        @Override
        public String getValue()
        {
            return "0";
        }
    },
    
    /**
     * 默认唯一标识
     */
    SMS_SERIOLNO("默认唯一标识")
    {
        @Override
        public String getValue()
        {
            return "cba3131313161223";
        }
    },
    
    ;
    
    protected final String key;
    
    protected final String description;
    
    XingYeVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return XingYeVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
