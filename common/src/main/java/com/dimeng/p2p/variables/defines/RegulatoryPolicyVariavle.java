/*
 * 文 件 名:  RegulatoryPolicyVariavle.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月8日
 */
package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * <政策监管常量值>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月8日]
 */
@VariableTypeAnnotation(id = "REGULATORYPOLICY", name = "政策监管功能模版")
public enum RegulatoryPolicyVariavle implements VariableBean
{
    /**
     * 标产品是否增加投资限制
     */
    IS_INVEST_LIMIT("标产品是否增加投资限制, true(限制),false(不限制)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    } ,
    
    /**
     * 是否开启风险承受能力评估
     */
    IS_OPEN_RISK_ASSESS("是否开启风险承受能力评估，true（开启），false（不开启)"){
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    /**
     * 用户一自然年内可进行的风险评估次数
     */
    ONE_YEAR_RISK_ASSESS_NUM("用户一自然年内可进行的风险评估次数"){
        @Override
        public String getValue()
        {
            return String.valueOf(3);
        }
    },
    
    /**
     * 风险评估分值设置（例如：8,6,4,2：分别对应ABCD选项值）
     */
    RISK_ASSESS_VALUE("风险评估分值设置"){
        @Override
        public String getValue()
        {
            return "8,6,4,2";
        }
    };

    protected final String key;
    protected final String description;

    RegulatoryPolicyVariavle(String description) {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }

    @Override
    public String getType() {
        return RegulatoryPolicyVariavle.class.getAnnotation(VariableTypeAnnotation.class).id();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        try (InputStreamReader reader = new InputStreamReader(
            RegulatoryPolicyVariavle.class.getResourceAsStream(getKey()), "UTF-8")) {
            StringBuilder builder = new StringBuilder();
            char[] cbuf = new char[1024];
            int len = reader.read(cbuf);
            while (len > 0) {
                builder.append(cbuf, 0, len);
                len = reader.read(cbuf);
            }
            return builder.toString();
        } catch (Throwable t) {
        }
        return null;
    }
    
    @Override
    public boolean isInit()
    {
        return true;
    }
    
}