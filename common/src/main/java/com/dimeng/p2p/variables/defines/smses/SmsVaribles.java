package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "MESSAGE", name = "短信发送信息")
public enum SmsVaribles implements VariableBean
{
    /**
    * 短信最大读取量
    */
    SMS_MAX_COUNT("短信最大读取量")
    {
        @Override
        public String getValue()
        {
            return "1000";
        }
    },
    /**
     * 短信发送超时时间
     */
    SMS_EXPIRES_MINUTES("短信发送超时时间")
    {
        @Override
        public String getValue()
        {
            return "30";
        }
    },
    /**
     * 是否使用云通讯短信接口
     * 格式：0 不使用，1 使用
     */
    SMS_IS_USE_YTX("是否使用云通讯接口")
    {
        @Override
        public String getValue()
        {
            return "0";
        }
    },
    /**
     * 云通讯SMS模板键值对
     * 格式：类型=模板id
     */
    SMS_YTX_TEMPLE("云通讯SMS模板键值对")
    {
        @Override
        public String getValue()
        {
            return "交易密码修改成功=5079&公用验证码=5119&交易密码修改获取验证码=5078&提现申请=5074&提现失败=5073&提现成功=5072&放款成功=5070&发标审核成功=5067&找回密码=5058&逾期垫付=5177&风险保证金充值=5178&投资成功=5179&取消投资成功=5180&投资收款=5183";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    SmsVaribles(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return SmsVaribles.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(SmsVaribles.class.getResourceAsStream(getKey()), "UTF-8"))
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
