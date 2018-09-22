package com.dimeng.p2p.modules.nciic.service.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/*
 * 文 件 名:  XingyezxVariable.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
@VariableTypeAnnotation(id = "NCIICXYZX", name = "兴业--实名认证")
public enum XingyezxVariable implements VariableBean
{
    CHANNELNO("兴业实名认证_渠道编号")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    QUERIER("兴业实名认证_操作用户")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    ORGNAME("兴业实名认证_机构名称")
    {
        @Override
        public String getValue()
        {
            return "123456";
        }
    },
    QUERYURL("兴业实名认证_查询请求地址")
    {
        @Override
        public String getValue()
        {
            return "http://220.248.31.55:7100/xyzxQueryServer/query";
        }
    },
    QUERYRESULTURL("兴业实名认证_认证结果查询请求地址")
    {
        @Override
        public String getValue()
        {
            return "http://220.248.31.55:7100/xyzxQueryServer/queryResult";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    XingyezxVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String getKey()
    {
        return key;
    }
    
    @Override
    public String getType()
    {
        return XingyezxVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(XingyezxVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
