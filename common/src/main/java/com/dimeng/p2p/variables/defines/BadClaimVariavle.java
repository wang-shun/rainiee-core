/*
 * 文 件 名:  BadClaimVariavle.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月24日
 */
package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * <不良债权转让>
 * <功能详细描述>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月24日]
 */
@VariableTypeAnnotation(id = "BadClaim", name = "不良债权转让功能模版")
public enum BadClaimVariavle implements VariableBean
{
    
    /**
     * 不良债权转让开关,true(开启),false(关闭)
     */
    IS_BADCLAIM_TRANSFER("不良债权转让开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    /**
     * 不良债权转让合同保全开关,true(开启),false(关闭)
     */
    IS_ALLOW_BADCLAIM_TRANSFER("不良债权转让合同保全开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 不良债权转让：逾期多少天可转让
     */
    BLZQZR_YQ_DAY("不良债权转让：逾期多少天可转让")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    BadClaimVariavle(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return BadClaimVariavle.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(BadClaimVariavle.class.getResourceAsStream(getKey()), "UTF-8"))
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
