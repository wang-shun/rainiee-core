package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 双乾实名认证配置
 * @author xiaoyaocai
 *
 */
@VariableTypeAnnotation(id = "NCIICSQ", name = "双乾--实名认证")
public enum NciicShuangQianVariable implements VariableBean
{
    CHARSET("双乾编码")
    {
        @Override
        public String getValue()
        {
            return "UTF-8";
        }
    },
    /**
     * 双乾,开通乾多多账号为平台账号时生成
     */
    SQ_PLATFORMMONEYMOREMORE("双乾，平台乾多多标识")
    {
        @Override
        public String getValue()
        {
            return "p185";
        }
    },
    /**
    * 双乾，公钥
    */
    SQ_PUBLIC_KEY("双乾，商户公钥 ")
    {
        @Override
        public String getValue()
        {
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYfEpdX5mTc+57J/51Asv9SDk/OF8/tRRSSAYXQg5OZZhBa0/WciVliAG07garWzIgWqCGK3+7jpnDwwM3uaAYFxReNN5Nsy4wEzOBZPcmc5avPZLmIJSeTlkecPpuBg3Sy0MI/HH5tlTrcZ203fyWScR0HMPMq5xpuCJ4Qr266QIDAQAB";
        }
    },
    /**
    * 双乾，商户私钥
    */
    SQ_PRIVATEPKCS8_KEY("双乾，商户私钥")
    {
        @Override
        public String getValue()
        {
            return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJh8Sl1fmZNz7nsn/nUCy/1IOT84Xz+1FFJIBhdCDk5lmEFrT9ZyJWWIAbTuBqtbMiBaoIYrf7uOmcPDAze5oBgXFF403k2zLjATM4Fk9yZzlq89kuYglJ5OWR5w+m4GDdLLQwj8cfm2VOtxnbTd/JZJxHQcw8yrnGm4InhCvbrpAgMBAAECgYBt3phigQCSKyU5Xc7Nlq9Ol1yQPdj7eUjkJHsnBPRz7mXvNRg4htSFPKMmL59klngesc4Z/nuxs4T9daT64OgFdUKIW8f+0kun5HZJ+fkBTSAGDMq+gqWQfweJ2Bys27qxDHCTyUGydRyAj9oQDAk0z7Jrtup4EePMIQwp/jb9VQJBANuiPsOtfOhIsOwK5vxI0BTdHrfMs5heY7BNKTymML8D7AbcLgkt53mEgIHQu8QBBSC7qS3s65+Rl9EpuoP4mdsCQQCxu8osiePWCOFvdNBLng+kj30e47socaErULChz0rmKYUO3vrGN7IoFrIuhwArnIbZduRaCLkGYhTS5Rl7j+OLAkB5ODvh7f+xiGU1cfL4rQtDaKNKmE1LPFVS+dNXqPXghz6erqkt4csO84WloFnxnQqCfXCra0bEpCuhgqFxsyTfAkBi2G24F3f+sTGvKugtJdrNSn/rjfuooolf7aBOXVrqZmz5uEj/tDoA0Z6HAc22c3cLunOFHxTH2AR8xa1Gat/BAkEAiCBTYHmw0zICBLrwloAlV4S2XRr9YIvAGlZrO2Zf+/SD8bQyU/Ad99qQzPVas29qVkXrgtIWNk8YZJNZ2Zn0fg==";
        }
    },
    /**
     * 双乾实名认证服务地址
     */
    SERVICE_VALID_URL("双乾验证服务地址")
    {
        @Override
        public String getValue()
        {
            return "http://218.4.234.150:88/main/authentication/identityMatching.action";
        }
    },
    /**
    * 双乾实名认证回调地址
    */
    ID_VALID_CALLBACK_URL("双乾实名认证回调地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/ret/nciicCallBack.htm";
        }
    },
    /**
     * 双乾，是否启用防抵赖,为0，不启用，1为启用
     */
    SQ_ANTISTATE("双乾，是否启用防抵赖功能")
    {
        @Override
        public String getValue()
        {
            return "0";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    NciicShuangQianVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return NciicShuangQianVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(NciicShuangQianVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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