package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 诺证通实名认证配置
 * @author dengwenwu
 *
 */
@VariableTypeAnnotation(id = "NCIICNZT", name = "诺证通--实名认证")
public enum NciicNuoZhengVariable implements VariableBean
{
    
    MALL_ID("商家id")
    {
        @Override
        public String getValue()
        {
            return "110286";
        }
    },
    /**
     * md5签名
     */
    APP_KEY("md5加密key")
    {
        @Override
        public String getValue()
        {
            return "xxxxxxxxxxxx";
        }
    },
    URL("诺证通实名认证地址")
    {
        @Override
        public String getValue()
        {
            return "http://121.41.42.121:8080/v2/id-server";
        }
    },
    BANKCARD_URL("诺证通银行卡认证地址")
    {
        @Override
        public String getValue()
        {
            return "http://121.41.42.121:8080/v3/card2-server";
        }
    },
    /**
    * 是否启用银行卡认证
    */
    CHECKCARD_ENABLE("是否启用银行卡认证：true启用；false不启用")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    NciicNuoZhengVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return NciicNuoZhengVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(NciicNuoZhengVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
