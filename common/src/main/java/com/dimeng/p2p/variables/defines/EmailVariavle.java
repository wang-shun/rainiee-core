package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "EMAIL", name = "邮件通知模版")
public enum EmailVariavle implements VariableBean
{
    
    /**
     * 找回密码
     */
    FIND_PASSWORD("找回密码"),
    /**
    * 邮件绑定验证码
    */
    RIGISTER_VERIFIY_LINK("邮件绑定验证码"),
    
    /**
     * 发标审核成功
     */
    //LOAN_CHECKED("发标审核成功"),
    /**
    * 放款成功
    */
    //LOAN_SUCCESS("放款成功"),
    /**
    * 提现申请
    */
    TX_SQ("提现申请"),
    /**
    * 提现成功
    */
    //TX_CG("提现成功"),
    /**
    * 提现失败
    */
    TX_SB("提现失败"),
    /**
    * 修改邮箱验证码(老邮箱)
    */
    UPDATE_OLD_LINK("修改邮箱验证码(老邮箱)"),
    /**
    * 修改邮箱验证码(新邮箱)
    */
    UPDATE_NEW_LINK("修改邮箱验证链接(新邮箱)"),
    
    SEND_HKTX_MAIL("还款提醒邮件模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 在${time}需还款${money}元，请按时还款。此邮件由 【${SYSTEM.SITE_NAME}】 发出,请勿回复。";
        }
    },
    SEND_QYTX_MAIL("还款逾期提醒邮件模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 已经逾期，请尽快还款。此邮件由 【${SYSTEM.SITE_NAME}】 发出,请勿回复。";
        }
    },
    /**
     * 邮箱链接地址有效时间
     */
    EMAIL_LINK_VALID_PERIOD("邮箱链接地址有效期[单位为小时]")
    {
        @Override
        public String getValue()
        {
            return String.valueOf(24);
        }
    },
    /**
     * 投资人，不良债权转让邮件模板
     */
    TZR_BLZQZR("投资人，不良债权转让邮件模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您投资的标的“${bid}”发生不良债权转让${feeType},金额${amount}元。感谢您对我们的关注与支持！[${SYSTEM.SITE_NAME}]";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    
    /**
     * 借款人，不良债权转让邮件模板
     */
    JKR_BLZQZR("借款人，不良债权转让邮件模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您的借款标的“${bid}”发生不良债权转让,借款金额${amount}元,债权价值${creditPrice}元,转让价格${subscribePrice}元。感谢您对我们的关注与支持！[${SYSTEM.SITE_NAME}]";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    
    /**
     * 不良债权转让购买邮件模板
     */
    BLZQZR_BUY("不良债权转让购买邮件模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您购买的资产“${title}”已认购成功，金额为${money}元。感谢您对我们的关注和支持！[${SYSTEM.SITE_NAME}]";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    /**
     * 借款审核不通过邮件内容
     */
    JKSHBTG_MAIL_STR("尊敬的用户：您好，您提交的借款申请审核不通过。具体原因是‘%s’。感谢您对我们的关注和支持！ %s");
    
    protected final String key;
    
    protected final String description;
    
    EmailVariavle(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return EmailVariavle.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(EmailVariavle.class.getResourceAsStream(getKey()), "UTF-8"))
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