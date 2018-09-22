package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 鹏元中兴实名认证
 * @author xiaoyaocai
 *
 */
@VariableTypeAnnotation(id = "NCIICPYZX", name = "鹏元中兴--实名认证")
public enum PyzxVariable implements VariableBean
{
    /**
    * 实名认证-账号
    */
    ID5DATAACCOUNT("实名认证账号")
    {
        @Override
        public String getValue()
        {
            return "wstzwsquery";
        }
    },
    /**
    * 实名认证-密码
    */
    ID5DATAPASSWORD("实名认证密码")
    {
        @Override
        public String getValue()
        {
            return "{MD5}6zAp7CvxugS3XjexLprBmQ==";
        }
    },
    /**
     * 证书密码
     */
    CERTIFICATE_PASSWORD("证书密码")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    
    /**
     * 证书路径
     */
    CERTIFICATE_PATH("证书路径")
    {
        @Override
        public String getValue()
        {
            return "D:\\_keys\\pyzx_test\\wstz.jks";
        }
    },
    
    IS_CERTIFICATE("测试不需要安全认证")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    WEBSERVICE("鹏元认证WebService服务地址")
    {
        @Override
        public String getValue()
        {
            return "http://www.pycredit.com:9001/services/WebServiceSingleQuery?wsdl";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    PyzxVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return PyzxVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(PyzxVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
