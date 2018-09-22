package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "NCIICGAJ", name = "公安局实名认证")
public enum GajVariable implements VariableBean
{
    /**
     * 实名认证地址
     */
    URL("实名认证地址")
    {
        @Override
        public String getValue()
        {
            return "https://api.nciic.com.cn/nciic_ws/services/NciicServices";
        }
    },
    /**
     * 实名认证账户
     */
    ACCOUNT("实名认证账户")
    {
        @Override
        public String getValue()
        {
            return "account";
        }
    },
    /**
     * 实名认证许可证
     */
    LICENCE("实名认证许可证")
    {
        @Override
        public String getValue()
        {
            return "licence";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    GajVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return GajVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(GajVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
