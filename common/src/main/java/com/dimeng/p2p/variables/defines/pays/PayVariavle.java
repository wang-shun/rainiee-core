package com.dimeng.p2p.variables.defines.pays;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "PAY", name = "第三方支付")
public enum PayVariavle implements VariableBean
{
    
    /**
     * 充值是否需要实名认证
     */
    IS_PAY_TEST("是否是支付模块测试")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 充值是否需要实名认证
     */
    CHARGE_MUST_NCIIC("充值是否需要实名认证")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 充值是否需要手机认证
     */
    CHARGE_MUST_PHONE("充值是否需要手机认证")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 是否需要邮箱认证
     */
    CHARGE_MUST_EMAIL("是否需要邮箱认证")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
    * 充值最低金额(元)
    */
    CHARGE_MIN_AMOUNT("充值最低金额(元)")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 充值最高金额(元)
     */
    CHARGE_MAX_AMOUNT("充值最高金额(元)")
    {
        @Override
        public String getValue()
        {
            return "200000";
        }
    },
    /**
     * 充值最高手续费（元）
     */
    CHARGE_MAX_POUNDAGE("充值最高手续费（元）")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
    },
    /**
     * 用户充值手续费率
     */
    CHARGE_RATE("用户充值手续费率")
    {
        @Override
        public String getValue()
        {
            return "0.004";
        }
    },
    /**
     * 系统是否需要交易密码
     */
    CHARGE_MUST_WITHDRAWPSD("系统是否需要交易密码")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    PayVariavle(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return PayVariavle.class.getAnnotation(VariableTypeAnnotation.class).id();
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
    
    @Override
    public boolean isInit()
    {
        return true;
    }
    
}