package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 中诚信源实名认证配置
 * @author dengwenwu
 *
 */
@VariableTypeAnnotation(id = "NCIICZCXY", name = "中诚信源实名认证")
public enum NciicZhongChengXinYuanVariable implements VariableBean
{
    
    USER_NAME("中诚信源实名认证用户名")
    {
        @Override
        public String getValue()
        {
            return "dmtext";
        }
    },
    /**
     * md5签名
     */
    PASSWORD("中诚信源实名认证密码")
    {
        @Override
        public String getValue()
        {
            return "xxxxxxxxxxxx";
        }
    },
    URL("中诚信源实名认证地址")
    {
        @Override
        public String getValue()
        {
            return "http://211.147.7.47/zcxy/services/QueryValidatorServices?wsdl";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    NciicZhongChengXinYuanVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return NciicZhongChengXinYuanVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(NciicZhongChengXinYuanVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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