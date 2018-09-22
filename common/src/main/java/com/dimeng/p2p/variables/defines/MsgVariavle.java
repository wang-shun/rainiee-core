package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "MSG", name = "短信通知模版")
public enum MsgVariavle implements VariableBean
{
    
    /**
     * 找回密码
     */
    FIND_PASSWORD("找回密码"),
    /**
     * 手机绑定验证码
     */
    RIGISTER_VERIFIY_CODE("手机绑定验证码"),
    /**
     * 交易密码修改获取验证码
     */
    // WITHDRAW_PASSWORD_UPDATE_CODE("交易密码修改获取验证码"),
    /**
     * 交易密码修改成功
     */
    // WITHDRAW_PASSWORD_UPDATE_SUCCESS("交易密码修改成功"),
    /**
     * 筹款成功(满标)
     */
    // LOAN_FILLED("筹款成功(满标)"),
    /**
     * 放款成功
     */
    LOAN_SUCCESS("放款成功"),
    /**
     * 提现申请
     */
    TX_SQ("提现申请"),
    /**
     * 提现成功
     */
    TX_CG("提现成功"),
    /**
     * 提现失败
     */
    TX_SB("提现失败"),
    /**
     * 修改邮箱验证码
     */
    UPDATE_EMAIL_CODE("修改邮箱验证码"),
    /**
     * 修改手机验证码(旧手机)
     */
    // UPDATE_OLD_PHONE_CODE("修改手机验证码(旧手机)"),
    /**
     * 修改手机验证码(新手机)
     */
    UPDATE_NEW_PHONE_CODE("修改手机验证码(新手机)"),
    /**
     * 投资人,投资收款
     */
    TZR_TBSK("投资收款"),
    /**
     * 借款人,借款还款
     */
    JKR_JKHK("借款还款"),
    /**
     * 公用验证码
     */
    // COMMON("公用验证码"),
    /**
     * 通过手机验证码修改密保问题
     */
    UPDATE_SCURITY_PHONE_CODE("通过手机验证码修改密保问题"),
    /**
     * 还款提醒短信模板
     */
    SEND_HKTX_MSG("还款提醒短信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 在${time}需还款${money}元，请按时还款。";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    /**
     * 还款逾期提醒短信模板
     */
    SEND_QYTX_MSG("还款逾期提醒短信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 已经逾期，请尽快还款。";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    /**
    * 法大大电子签名CA申请模板
    */
    FDD_CA_APPLY_MSG("法大大电子签名CA申请模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户，您好。您的法大大CA数字证书已申请成功，证书申请成功后将会用于法大大电子合同签名，感谢您对我们的关注和支持！";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    /**
     * 商品订单审核不通过退款短信格式
     */
    MALLNOPASSSMS("商品订单审核不通过")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您于${time}提交的商品名称：${commodityName}，数量：${num}件的订单，由于“${reason}”的原因，未能通过审核，${payment}会返还至您的账户中，请注意查收，感谢您对我们的关注和支持！ ";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    
    /**
     * 商品发货通知短信格式
     */
    MALLSIPPINGSMS("商品发货通知")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您于${time}提交的商品名称：${commodityName}，数量：${num}件的订单已发货，可到我的订单查看，感谢您对我们的关注和支持！";
        }
    },
    /**
     * 登录密码输入错误超过限制次数短信
     */
    LOGIN_ERROR_TIMES_MSG("登录密码输入错误超过限制次数短信")
    {
        @Override
        public String getValue()
        {
            return "您于${dateTime} 输入密码已连续错误${count}次，如需帮助请致电${serviceTel}！";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    
    /**
     * 赠送活动通知短信
     */
    SEND_ACTIVITY("赠送红包/加息券通知短信")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:今天是您的生日，平台给您赠送了一张${actType},请在有效期内使用。";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
    },
    /**
     * 投资人,提前还款收款
     */
    TZR_TQHK_TBSK("投资收款-提前还款")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${userName}”：您投资的“${title}”因提前还款已结清，到账金额${amount}元，请注意查收。感谢您对我们的关注与支持！";
        }
        
    },
    /**
     * 借款人,借款还款-提前还款
     */
    JKR_TQHK_JKHK("借款还款-提前还款")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${userName}”：您的“${title}”提前还款${amount}元，该借款已结清。感谢您对我们的关注与支持！";
        }
        
    },
    /**
     * 逾期垫付
     */
    YQ_DF("逾期垫付")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户： 您投资的标的“${bid}”发生垫付${feeType},金额${amount}元。 感谢您对我们的关注与支持！";
        }
        
    },
    /**
     * 投资人（体验金）,投资成功
     */
    TYJ_TBCG("体验金投资成功")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户： 您好，您投的借款“${title}”投资成功，体验金金额为${money}元。 感谢您对我们的关注和支持！";
        }
    },
    /**
     * 投资人,投资成功
     */
    TZR_TBCG("投资成功")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户： 您好，您投的借款“${title}”投资成功，金额为${money}元。 感谢您对我们的关注和支持！";
        }
    },
    /**
     * 投资人,加息券投资流标
     */
    TZR_JXQ_TBLB("加息券投资流标")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您用加息券投资的‘${title}’借款，已流标,加息券已返回，请注意加息券的有效时间。感谢您对我们的关注和支持！ ";
        }
    },
    /**
     * 投资人,体验金投资流标
     */
    TZR_TYJ_TBLB("体验金投资流标")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您用体验金投资的‘${title}’借款，已流标,体验金已返回，请注意体验金的有效时间。感谢您对我们的关注和支持！ ";
        }
    },
    /**
     * 投资人,投资流标
     */
    TZR_TBLB("投资流标")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户： 您好，您投的借款“${title}”已流标,投资资金已解冻。 感谢您对我们的关注和支持！ ";
        }
    },
    /**
     * 商品退款
     */
    MALLREFUND("商品退款")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您于${time}提交的商品名称：${commodityName}，数量：${num}件的订单，已成功退款，金额会返还至您的账户中，请注意查收，感谢您对我们的关注和支持！";
        }
    },
    /**
     * 投资人,加息券返还利息
     */
    TZR_JXQFHLX("加息券返还利息")
    {
        @Override
        public String getValue()
        {
            return "您加息券投资的‘${title}’收到一笔还款，还款科目：‘${subject}’；还款金额：‘${amount}’元。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 风险保证金充值
     */
    FXBZJCZ("风险保证金充值")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户： 您好，您充值${amount}元风险保证金已成功入账。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 风险保证金充值
     */
    FXBZJTX("风险保证金提现")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您的${amount}元风险保证金已成功提现.感谢您对我们的关注和支持！ ";
        }
    },
    /**
     * 投资人,体验金返还利息
     */
    TZR_TYJFHLX("体验金返还利息")
    {
        @Override
        public String getValue()
        {
            return "您体验金投资的‘${title}’收到一笔还款，还款科目：‘${subject}’；还款金额：‘${amount}’元。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 投资人，不良债权转让短信模板
     */
    TZR_BLZQZR("投资人，不良债权转让短信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您投资的标的“${bid}”发生不良债权转让${feeType},金额${amount}元。感谢您对我们的关注与支持！";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    
    /**
     * 借款人，不良债权转让短信模板
     */
    JKR_BLZQZR("借款人，不良债权转让短信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您的借款标的“${bid}”发生不良债权转让,借款金额${amount}元,债权价值${creditPrice}元,转让价格${subscribePrice}元。感谢您对我们的关注与支持！";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    
    /**
     * 不良债权转让购买短信模板
     */
    BLZQZR_BUY("不良债权转让购买短信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您购买的资产“${title}”已认购成功，金额为${money}元。感谢您对我们的关注和支持！";
        }
        
        @Override
        public boolean isInit()
        {
            return true;
        }
        
    },
    /**
     * 体验金奖励短信模板
     */
    SEND_EXPERIENCE("体验金奖励短信模板"),
    /**
     * 加息券奖励短信模板
     */
    SEND_INTEREST("加息券奖励短信模板"),
    /**
     * 红包奖励短信模板
     */
    SEND_REDPACKET("红包奖励短信模板"),
    /**
     * 不良债权转让提醒短信模板(审核通过发送)
     */
    BLZQZRTX_SH("不良债权转让提醒短信模板");
    
    protected final String key;
    
    protected final String description;
    
    MsgVariavle(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return MsgVariavle.class.getAnnotation(VariableTypeAnnotation.class).id();
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
        try (InputStreamReader reader = new InputStreamReader(MsgVariavle.class.getResourceAsStream(getKey()), "UTF-8"))
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