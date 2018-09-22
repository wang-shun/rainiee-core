package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

//@VariableTypeAnnotation(id = "MESSAGEJKT", name = "集客通短信发送信息")
public enum JiKeTongVariables implements VariableBean
{
    
    /**
     * 短信发送用户id
     */
    SMS_ORG_ID("机构代码")
    {
        @Override
        public String getValue()
        {
            return "1300";
        }
    },
    
    /**
     * 短信发送用户id
     */
    SMS_USER_ID("短信发送用户名")
    {
        @Override
        public String getValue()
        {
            return "dmwl";
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
     * 签名
     */
    SMS_SIGN("签名")
    {
        @Override
        public String getValue()
        {
            return "【迪蒙网贷】";
        }
    },
    
    /**
     * 状态报告: true需要 , false不需要
     */
    SMS_NEED_STATUS("是否需要状态报告")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    //    /**
    //     * 产品ID
    //     */
    //    SMS_PRODUCT_ID("产品id")
    //    {
    //        @Override
    //        public String getValue()
    //        {
    //            return "621215";
    //        }
    //    },
    
    /**
     * 短信发送地址
     */
    SMS_URI("短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://120.24.240.234:8080/service.asmx/SendMessage";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    JiKeTongVariables(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return DianDianKeVariables.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(JiKeTongVariables.class.getResourceAsStream(getKey()), "UTF-8"))
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
