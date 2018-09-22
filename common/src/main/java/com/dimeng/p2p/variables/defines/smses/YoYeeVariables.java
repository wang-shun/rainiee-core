package com.dimeng.p2p.variables.defines.smses;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/*
* 包    名:  com.dimeng.p2p.variables.defines.smses
* 版    权:  深圳市迪蒙网络科技有限公司
* 描    述:  <描述>
* 修 改 人:  王明
* 修改时间:  2015/4/23
*/
//@VariableTypeAnnotation(id = "YOYEE_MESSAGEYY", name = "有易互动短信常量")
public enum YoYeeVariables implements VariableBean
{
    YOYEE_USERNAME("登录账号")
    {
        @Override
        public String getValue()
        {
            return "jingsha";
        }
    },
    YOYEE_PASSWORD("登录密码")
    {
        @Override
        public String getValue()
        {
            return "108669";
        }
    },
    YOYEE_SEND_URL("短信发送地址")
    {
        @Override
        public String getValue()
        {
            return "http://service.yoyee.cn/index.php";
        }
    };
    
    private String key;
    
    private String description;
    
    private YoYeeVariables(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return YoYeeVariables.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return this.key;
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(YoYeeVariables.class.getResourceAsStream(getKey()), "UTF-8"))
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
        return this.description;
    }
    
    @Override
    public boolean isInit()
    {
        return false;
    }
}
