package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "NCIICGZT", name = "国政通实名认证")
public enum GztVariable implements VariableBean
{
    /**
    * 实名认证-数据类型-身份信息认证-国政通
    */
    ID5DATATYPE("实名认证身份信息认证")
    {
        @Override
        public String getValue()
        {
            return "1A020201";
        }
    },
    /**
    * 实名认证-加密私钥，长度不能够小于8位-国政通
    */
    ID5DATAKEY("实名认证加密私钥")
    {
        @Override
        public String getValue()
        {
            return "12345678";
        }
    },
    /**
    * 实名认证-账号-国政通
    */
    ID5DATAACCOUNT("实名认证账号")
    {
        @Override
        public String getValue()
        {
            return "szdmwebservice";
        }
    },
    /**
    * 实名认证-密码-国政通
    */
    ID5DATAPASSWORD("实名认证密码")
    {
        @Override
        public String getValue()
        {
            return "szdmwebservice_0q$lHFhV";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    GztVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return GztVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(GztVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
        return true;
    }
}
