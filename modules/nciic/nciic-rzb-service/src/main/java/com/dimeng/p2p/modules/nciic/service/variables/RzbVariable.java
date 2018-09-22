package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "NCIICRZB", name = "认证宝实名认证")
public enum RzbVariable implements VariableBean
{
    /**
     * 认证宝，账号
     */
    RZB_ACCOUNT("认证宝，账号")
    {
        @Override
        public String getValue()
        {
            return "yqd_admin";
        }
    },
    /**
     * 认证宝，密码
     */
    RZB_PASSWORD("认证宝，密码")
    {
        @Override
        public String getValue()
        {
            return "yqd888";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    RzbVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return RzbVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(RzbVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
