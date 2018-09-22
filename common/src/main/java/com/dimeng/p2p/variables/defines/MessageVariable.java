package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "MESSAGE", name = "发送信息账户")
public enum MessageVariable implements VariableBean
{
    /**
     * 邮件服务器主机名
     */
    MAIL_SMTP_HOST("邮件服务器主机名")
    {
        @Override
        public String getValue()
        {
            return "smtp.exmail.qq.com";
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
     * 邮件服务器主机端口，默认25
     */
    MAIL_SMTP_PORT("邮件服务器主机端口")
    {
        @Override
        public String getValue()
        {
            return "25";
        }
    },
    /**
     * 邮箱账号名
     */
    MAIL_USER_NAME("邮件账号名")
    {
        @Override
        public String getValue()
        {
            return "service@vipdai.cn";
        }
    },
    /**
     * 邮箱密码
     */
    MAIL_USER_PASSWORD("邮箱密码")
    {
        @Override
        public String getValue()
        {
            return "taorun2014";
        }
    },
    /**
     * 邮箱账户个人名
     */
    MAIL_USER_PERSONALNAME("邮箱账户个人名")
    {
        @Override
        public String getValue()
        {
            return "迪蒙网贷";
        }
    },
    /**
     * 邮件最大读取量
     */
    MAIL_MAX_COUNT("邮件最大读取量")
    {
        @Override
        public String getValue()
        {
            return "1000";
        }
    },
    /**
     * 邮件发送超时时间
     */
    MAIL_EXPIRES_MINUTES("邮件发送超时时间")
    {
        @Override
        public String getValue()
        {
            return "30";
        }
    },
    /**
     * 未登录时，当天同一功能，同一手机、邮箱获取验证码的最大数量
     */
    PHONE_MAX_COUNT("未登录时，当天同一功能，同一手机、邮箱获取验证码的最大数量")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     *手机、邮箱验证码匹配错误最大次数
     */
    PHONE_VERIFYCODE_MAX_ERROR_COUNT("手机、邮箱验证码匹配错误最大次数")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 登陆后，用户当天同一功能获取手机、邮箱验证码的最大数量
     */
    USER_SEND_MAX_COUNT("登陆后，用户当天同一功能获取手机、邮箱验证码的最大数量")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * IP地址当天同一功能获取手机验证码的最大数量
     */
    IP_SEND_MAX_COUNT("IP地址当天同一功能获取手机验证码的最大数量")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 是否使用SSL发送邮件,FALSE表示不通过SSL发送，TRUE表示通过SSL发送
     */
    IS_SSL("是否使用SSL发送邮件,FALSE表示不通过SSL发送，TRUE表示通过SSL发送")
    {
        @Override
        public String getValue()
        {
            return "FALSE";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    MessageVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return MessageVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return key;
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(MessageVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
    public String getDescription()
    {
        return description;
    }
	@Override
	public boolean isInit() {
		return true;
	}
    
}
