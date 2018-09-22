/*
 * 文 件 名:  RealNameVarivale.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月8日
 */
package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * <实名认证统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月8日]
 */
@VariableTypeAnnotation(id = "REALNAMEAUTH", name = "实名认证统计功能模版")
public enum RealNameAuthVarivale implements VariableBean
{
    /**
     * 是否有实名认证统计功能
     */
    IS_REALNAMEAUTH("是否有实名认证统计功能, true(有),false(没有)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    RealNameAuthVarivale(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return RealNameAuthVarivale.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(RealNameAuthVarivale.class.getResourceAsStream(getKey()), "UTF-8"))
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
