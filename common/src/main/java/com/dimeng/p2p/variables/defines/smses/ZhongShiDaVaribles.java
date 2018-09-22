package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEZSD", name = "众视达短信发送信息")
public enum ZhongShiDaVaribles implements VariableBean
{
    
    /**
     * 短信接口任务命令
     * send:发送，overage：余额及已发送量查询，
     * checkkeyword：非法关键词查询，query，状态报告
     */
    SMS_ZSD_ACTION("任务类型")
    {
        @Override
        public String getValue()
        {
            return "send";
        }
    },
    SMS_ZSD_COMPANY_ID("企业ID")
    {
        @Override
        public String getValue()
        {
            return "340";
        }
    },
    /**
    * 用户账号
    */
    SMS_ZSD_ACCOUNT("用户账号")
    {
        @Override
        public String getValue()
        {
            return "httx";
        }
    },
    /**
     * 用户密码
     */
    SMS_ZSD_PASSWORD("短信发送用户密码")
    {
        @Override
        public String getValue()
        {
            return "httx123";
        }
    },
    /**
     * 签名
     */
    SMS_ZSD_SIGN("签名")
    {
        @Override
        public String getValue()
        {
            return "【红彤易融】";
        }
    },
    /**
     * 短信发送地址
     */
    SMS_ZSD_URI("短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://120.24.247.128:8888/sms.aspx";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    ZhongShiDaVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return ZhongShiDaVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(ZhongShiDaVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
