package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "HTML", name = "HTML代码参数")
public enum HTMLVariable implements VariableBean
{
    /**
     * 理财产品介绍(优选理财计划)
     */
    FINANCIAL_PRODUCTS_YXLCJH("理财产品介绍(优选理财计划)"),
    /**
     * 理财产品介绍(散标投资)
     */
    FINANCIAL_PRODUCTS_SBTZ("理财产品介绍(散标投资)"),
    /**
     * 理财产品介绍(债权转让)
     */
    FINANCIAL_PRODUCTS_ZQZR("理财产品介绍(债权转让)"),
    /**
     * 借款产品介绍(信用贷)
     */
    LOAN_PRODUCTS_XJD("借款产品介绍(信用贷)"),
    /**
     * 借款产品介绍(担保贷)
     */
    LOAN_PRODUCTS_DBD("借款产品介绍(担保贷)"),
    /**
     * 借款产品介绍(信用贷)
     */
    LOAN_APPLY_CONDITION_XJD("借款产品申请条件(信用贷)"),
    /**
     * 借款产品介绍(担保贷)
     */
    LOAN_APPLY_CONDITION_DBD("借款产品申请条件(担保贷)"),
    /**
     * 借款产品介绍(车贷)
     */
    LOAN_PRODUCTS_CD("借款产品介绍(车贷)"),
    /**
     * 借款产品介绍(房贷)
     */
    LOAN_PRODUCTS_FD("借款产品介绍(房贷)"),
    /**
     * 借款产品介绍(流动金资贷款)
     */
    LOAN_PRODUCTS_LDZJDK("借款产品介绍(流动金资贷款)"),
    /**
     * 借款产品介绍(项目资金贷款)
     */
    LOAN_PRODUCTS_XMZJDK("借款产品介绍(项目资金贷款)"),
    
    /**
     * 个人借款意向(介绍)
     */
    GRJKYXJS("个人借款意向(介绍)"),
    /**
     * 企业借款意向(介绍)
     */
    QYJKYXJS("企业借款意向(介绍)"),
    /**
     * 借款产品介绍(生意贷)
     */
    LOAN_PRODUCTS_SYD("借款产品介绍(生意贷)"),
    /**
     * 关注我们HTML代码
     */
    GZWM("关注我们"),
    /**
     * 底部图标HTML代码
     */
    DBTB("底部图标"),
    /**
     * 个人信用融资申请提示
     */
    GRXYRZSQTS("个人信用融资申请提示"),
    
    /**
     * 个人担保融资申请提示
     */
    GRDBRZSQTS("个人担保融资申请提示"),
    /**
     * 企业信用融资申请提示
     */
    QYXYRZSQTS("企业信用融资申请提示"),
    /**
     * 企业担保融资申请提示
     */
    QYDBRZSQTS("企业担保融资申请提示"),
    /**
     * 平台特性介绍
     */
    PLATFORM_SPECIAL_FETURE("平台特性介绍");
    
    protected final String key;
    
    protected final String description;
    
    HTMLVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return HTMLVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(HTMLVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
