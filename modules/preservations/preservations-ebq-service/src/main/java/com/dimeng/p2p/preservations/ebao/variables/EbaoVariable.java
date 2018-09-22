package com.dimeng.p2p.preservations.ebao.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 
 * 易保全
 * <常量>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月15日]
 */
@VariableTypeAnnotation(id = "EBAO_PRESERVATIONS", name = "易保全合同保全")
public enum EbaoVariable implements VariableBean
{
    /**
     * 易保全_请填入服务地址（根据环境的不同选择不同的服务地址），沙箱环境，正式环境
     */
    EBAO_SERVICE_URL("易保全，服务器地址")
    {
        @Override
        public String getValue()
        {
            return "http://sandbox.api.ebaoquan.org/services";
        }
    },
    
    /**
     * 易保全_服务KEY
     */
    EBAO_APP_KEY("易保全，服务KEY")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    /**
     * 易保全_服务密钥
     */
    EBAO_APP_SECRET("易保全，服务密钥")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    EbaoVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return EbaoVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(EbaoVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
