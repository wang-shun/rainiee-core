package com.dimeng.p2p.modules.nciic.varibles;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;

/**
 * 
 * 通联代收代付配置
 * 
 * @author  wangshoahua
 * @version  [版本号, 2015年8月19日]
 */
@VariableTypeAnnotation(id = "NCIICALLINRZAIPG", name = "通联实名认证")
public enum AllinrzVaribles implements VariableBean
{
    /**
     * 通联代付商户号
     */
    ALLINRZ_AIPG_MERCHANTID("实名认证使用的通联代付商户号")
    {
        @Override
        public String getValue()
        {
            return "200604000000445";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    
    /**
     * 通联代付用户名
     */
    ALLINRZ_AIPG_USERNAME("实名认证使用的通联代付用户名")
    {
        @Override
        public String getValue()
        {
            return "20060400000044502";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    /**
     * 通联代付用户密码
     */
    ALLINRZ_AIPG_USERPASSWORD("实名认证使用的通联代付用户密码")
    {
        @Override
        public String getValue()
        {
            return "111111";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    
    /**
     * 通联代付私钥地址
     */
    ALLINRZ_PFXFILE_PATH("实名认证使用的通联代付私钥地址")
    {
        @Override
        public String getValue()
        {
            return "E:/allinpay/20060400000044502.p12";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    
    /**
     * 通联代付私钥密码
     */
    ALLINRZ_PFX_PASSWORD("实名认证使用的通联代付私钥密码")
    {
        @Override
        public String getValue()
        {
            return "111111";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    
    /**
     * 通联代付公钥地址
     */
    ALLINRZ_TLTCERPATH_PATH("实名认证使用的通联代付公钥地址")
    {
        @Override
        public String getValue()
        {
            return "E:/allinpay/allinpay-pds.cer";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    },
    
    /**
     * 通联代付接口地址
     */
    ALLINRZ_AIPG_URL("实名认证使用的通联代付接口地址")
    {
        @Override
        public String getValue()
        {
            return "https://113.108.182.3/aipg/ProcessServlet";
        }
        
        @Override
        public boolean isInit()
        {
            // TODO Auto-generated method stub
            return true;
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    AllinrzVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return AllinrzVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(PayVariavle.class.getResourceAsStream(getKey()), "UTF-8"))
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
}
