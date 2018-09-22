package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "LETTER", name = "站内信模版")
public enum LetterVariable implements VariableBean
{
    
    /**
     * 注册成功
     */
    REGESTER_SUCCESS("注册发送成功${name}:注册账号")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${name},恭喜您在${SYSTEM.SITE_NAME}成为会员。";
        }
    },
    /**
     * 充值成功
     */
    CHARGE_SUCCESS("充值成功"),
    /**
     * 充值失败
     */
    CHARGE_FAILD("充值失败"),
    /**
     * 发标审核成功
     */
    LOAN_CHECKED("发标审核成功"),
    /**
     * 借款审核不通过
     */
    LOAN_CHECKED_FAILD("借款审核不通过")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您提交的‘${title}’借款申请审核不通过。具体原因是‘${reason}’。感谢您对我们的关注与支持！";
        }
    },
    /**
     * 筹款成功(满标)
     */
    LOAN_FILLED("筹款成功(满标)"),
    /**
     * 放款成功
     */
    LOAN_SUCCESS("放款成功")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户${name}：感谢您的积极配合，您于${datetime}发布的借款“${title}”已放款成功。您可以通过“我的${SYSTEM.SITE_NAME}”（个人中心）- “我的借款”查看相关信息。点击 <a href='${lookUrl}' class='red'>这里 </a>查看您的借款信息。感谢您对我们的关注和支持！[${SYSTEM.SITE_NAME}]";
        }
    },
    /**
     * 放款失败
     */
    LOAN_FAILED("放款失败"),
    /**
     * 提现成功
     */
    TX_CG("提现成功"),
    /**
     * 提现失败
     */
    TX_SB("提现失败"),
    /**
     * 投资人,投资成功
     */
    TZR_TBCG("投资成功"),
    /**
     * 捐赠人，捐赠成功
     */
    TZR_JZCG("捐赠成功"),
    /**
     * 投资人（体验金）,投资成功
     */
    TYJ_TBCG("体验金投资成功"),
    /**
     * 投资人,投资流标
     */
    TZR_TBLB("投资流标"),
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
     * 投资人,投资收款
     */
    TZR_TBSK("投资收款")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您投资的“${title}”收到一笔还款，还款金额：${amount}元，还款期数：第${periods}期。感谢您对我们的关注和支持！";
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
     * 借款人,借款还款
     */
    JKR_JKHK("借款还款"),
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
     * 借款流标
     */
    JKR_JKLB("借款流标")
    {
        @Override
        public String getValue()
        {
            return "尊敬的用户：您好，您的‘${title}’借款，因无人投资已流标。感谢您对我们的关注与支持！";
        }
    },
    /**
     * 平台返还体验金利息
     */
    PT_TYJLX("平台返还体验金利息"),
    /**
     * 推广持续奖励
     */
    TG_CXJL("推广: 持续奖励站内信模板"),
    /**
     * 推广,有效奖励
     */
    TG_YXJL("推广:有效奖励站内信模板"),
    /**
     * 投优选理财成功
     */
    YX_TBCG("投优选理财成功"),
    /**
     * 投优选理财成功
     */
    YX_HKCG("投优选理财还款成功"),
    /**
     * 风险保证金充值
     */
    FXBZJCZ("风险保证金充值"),
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
     * 提前还款，债权下架
     */
    TQHK_ZQXJ("提前还款，债权下架")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您正在转让中的${zqId}债权，因借款人进行了提前还款，此债权已结束，所以您正在转让的此债权已下架。";
        }
    },
    /**
     * 逾期垫付
     */
    YQ_DF("逾期垫付"),
    /**
     * 标的发布成功站内信模板
     */
    LOAN_RELEASE_SUCCESS("标的发布成功站内信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：感谢您的积极配合，您的借款标的“${title}”已经发布成功。"
                + "您可以通过“我的${SYSTEM.SITE_NAME}”（个人中心）- “借款申请查询”查看相关信息。点击 <a href='${lookUrl}' class='blue'>这里</a> 查看您的借款投资进度。"
                + "感谢您对我们的关注和支持！";
        }
    },
    /**
     * 标的作废站内信模板
     */
    LOAN_RELEASE_CANCEL("标的作废站内信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您的借款标的“${title}”已经作废。具体原因如下：“${reason}”。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 用户手动取消债权转让
     */
    ZQ_MANUAL_CANCEL("手动下架债权")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您的债权转让“${title}”已手动取消。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 系统自动下架债权转让
     */
    ZQ_AUTOMATIC_CANCEL("自动下架债权")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您的债权转让“${title}”因到期无人购买，现已自动取消。感谢您对我们的关注和支持！";
        }
        
    },
    /**
     * 提前还款申请不通过站内信模板
     */
    TQHK_BTG("提前还款申请不通过站内信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您的借款“${bidName}”的提前还款申请于${verifyDate}日审核不通过。感谢您对我们的关注和支持！";
        }
    },
    /**
     * 还款提醒
     */
    SEND_HKTX_SUCCESS("还款提醒站内信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 在${time}需还款${money}元，请按时还款.";
        }
        
    },
    /**
     * 还款逾期提醒
     */
    SEND_YQTX_SUCCESS("还款逾期提醒站内信模板")
    {
        @Override
        public String getValue()
        {
            return "尊敬的“${name}”:您的借款“${title}” 已经逾期，请尽快还款.";
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
     * 商品订单审核不通过退款站内信格式
     */
    MALLNOPASSLETTER("商品订单审核不通过")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您于${time}提交的商品名称：${commodityName}，数量：${num}件的订单，由于“${reason}”的原因，未能通过审核，${payment}会返还至您的账户中，请注意查收，感谢您对我们的关注和支持！ ";
        }
    },
    /**
     * 商品发货通知站内信格式
     */
    MALLSIPPINGLETTER("商品发货通知")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您于${time}提交的商品名称：${commodityName}，数量：${num}件的订单已发货，可到我的订单查看，感谢您对我们的关注和支持！";
        }
    },
    /**
     * 赠送活动站内信通知
     */
    SEND_ACTIVITY("赠送红包/加息券站内信通知")
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
     * 投资人，不良债权转让站内信模板
     */
    TZR_BLZQZR("投资人，不良债权转让站内信模板")
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
     * 借款人，不良债权转让站内信模板
     */
    JKR_BLZQZR("借款人，不良债权转让站内信模板")
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
     * 不良债权转让购买站内信模板
     */
    BLZQZR_BUY("不良债权转让购买站内信模板")
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
     * 不良债权转让，债权下架
     */
    BLZQZR_ZQXJ("不良债权转让，债权下架")
    {
        @Override
        public String getValue()
        {
            return "尊敬的${userName}：您好，您正在转让中的${zqId}债权，因被机构进行了不良债权购买，此债权已结束，所以您正在转让的此债权已下架。";
        }
    },
    /**
     * 体验金奖励站内信模板
     */
    SEND_EXPERIENCE("体验金奖励站内信模板"),
    /**
     * 加息券奖励站内信模板
     */
    SEND_INTEREST("加息券奖励站内信模板"),
    /**
     * 红包奖励站内信模板
     */
    SEND_REDPACKET("红包奖励站内信模板"),
    
    /**
     * 不良债权转让提醒站内信模板(审核通过发送)
     */
    BLZQZRTX_SH("不良债权转让提醒站内信模板"),
    
    /**
     * 申请担保，审核通过
     */
    GUARANT_AUDIT_YES("申请担保，审核通过站内信模板"),
    
    /**
     * 申请担保，审核不通过
     */
    GUARANT_AUDIT_NO("申请担保，审核不通过站内信模板"),
    
    /**
     * 取消担保
     */
    GUARANT_CANCEL("取消担保站内信模板");
    
    protected final String key;
    
    protected final String description;
    
    LetterVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return LetterVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(LetterVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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