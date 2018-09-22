/*
 * 文 件 名:  MallVariavle.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * <平台商城>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
@VariableTypeAnnotation(id = "Mall", name = "积分商城功能模版")
public enum MallVariavle implements VariableBean
{
    
    /**
     * 是否有平台商城功能
     */
    IS_MALL("是否有积分商城功能, true(有),false(没有)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    } ,
    /**
     * 商品是否允许余额购买，true（允许），false（不允许)
     */
    ALLOWS_THE_BALANCE_TO_BUY("商品是否允许余额购买，true（允许），false（不允许)"){
        @Override
        public String getValue()
        {
            return "false";
        }
    };

    protected final String key;
    protected final String description;

    MallVariavle(String description) {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }

    @Override
    public String getType() {
        return MallVariavle.class.getAnnotation(VariableTypeAnnotation.class)
                .id();
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
                MallVariavle.class.getResourceAsStream(getKey()), "UTF-8")) {
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