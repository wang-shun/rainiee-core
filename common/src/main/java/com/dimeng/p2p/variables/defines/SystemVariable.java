package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "SYSTEM", name = "系统参数")
public enum SystemVariable implements VariableBean
{
    
    /**
     * SITE: 网站标题
     */
    SITE_TITLE("SITE: 网站标题")
    {
        @Override
        public String getValue()
        {
            return "迪蒙网贷";
        }
    },
    
    /**
     * SITE: 网站名称
     */
    SITE_NAME("SITE: 网站名称")
    {
        @Override
        public String getValue()
        {
            return "迪蒙网贷";
        }
    },
    
    /**
     * SITE: 富友网关商户号
     */
    MCHNTCD(" 富友网关商户号")
    {
        @Override
        public String getValue()
        {
            return "0002900F0096235";
        }
    },
    
    /**
     * SITE: 富友网关商户密钥
     */
    KEY(" 富友网关商户密钥")
    {
        @Override
        public String getValue()
        {
            return "5old71wihg2tqjug9kkpxnhx9hiujoqj";
        }
    },
      
    /**
     * SITE: 富友网关商户密钥
     */
    PRIVATEKEY("富友网关商户私钥")
    {
        @Override
        public String getValue()
        {
            return "5old71wihg2tqjug9kkpxnhx9hiujoqj";
        }
    },
    /**
     * SITE: 富友网关公钥
     */
    FUIOU_PUB_KEY("富友网关公钥")
    {
        @Override
        public String getValue()
        {
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBCcvUDkw3ONsVx7Rzh9IJoKKurwBnKSjJEJbLXQWDKIPZMtmxcHa5jNu6OgpQ0BatOYl4p4BmgH3HzVwWyn6iDOsDlxwZezFzArtPjtECq241nfmoGhbz9lMr7T56yY5PhATws32Dm1ZQbY8DvsFvTe2hKgmIGbZQ030seRnfSwIDAQAB";
        }
    },
    
    /**
     * SITE: 富友网关协议绑卡短信请求接口
     */
    BINDMSGURL("富友网关协议绑卡短信请求接口")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindMsg.pay";
        }
    },
    
    /**
     * SITE: 富友网关协议绑卡提交地址
     */
    BINDCOMMITURL("富友网关协议绑卡提交地址")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindCommit.pay";
        }
    },
    
    /**
     * SITE: 富友网关协议支付解绑地址
     */
    UNBIND("富友网关协议支付解绑地址")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/newpropay/unbind.pay";
        }
    },
    /**
     * SITE: 富友网关协议卡查询地址
     */
    BINDQUERY(" 富友网关协议支付解绑地址")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindQuery.pay";
        }
    },
    
    /**
     * SITE: 富友网关协议卡查询地址
     */
    ORDERPAY(" 富友网关协议支付请求地址")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/newpropay/order.pay";
        }
    },
    
    /**
     * SITE: 富友网关协议支付后台通知 URL
     */
    BACKURL("富友网关协议支付后台通知 URL")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/backNotifyServlet.htm";
        }
    },
    /**
     * SITE: 富友网关协议支付订单查询地址
     */
    CHECKRESULT("富友网关协议支付订单查询地址")
    {
        @Override
        public String getValue()
        {
            return "http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkResult.pay";
        }
    },
    /**
     * SITE: 网站域名
     */
    SITE_DOMAIN("SITE: 网站域名")
    {
        @Override
        public String getValue()
        {
            return "www.dimeng.net";
        }
    },
    
    /**
     * SITE: 网站版权信息
     */
    SITE_COPYRIGTH("SITE: 网站版权信息")
    {
        @Override
        public String getValue()
        {
            return "© 2014 ${SYSTEM.SITE_DOMAIN} All Rights Reserved ";
        }
    },
    /**
     * SITE: 网站备案号
     */
    SITE_FILING_NUM("SITE: 网站备案号")
    {
        @Override
        public String getValue()
        {
            return "粤ICP备13082949号";
        }
    },
    /**
     * SITE: 客服电话
     */
    SITE_CUSTOMERSERVICE_TEL("SITE: 客服电话")
    {
        @Override
        public String getValue()
        {
            return "400 - 678 - 5518";
        }
    },
    /**
     * SITE: 网站口号
     */
    SITE_WORD("SITE: 网站口号")
    {
        @Override
        public String getValue()
        {
            return "轻松贷款 投资无忧";
        }
    },
    /**
     * SITE: 邮编
     */
    SITE_POSTCODE("SITE: 邮编")
    {
        @Override
        public String getValue()
        {
            return "518000";
        }
    },
    /**
     * SITE: 网站Meta关键字
     */
    SITE_META_KEYWORDS("SITE: 网站Meta关键字"),
    /**
     * SITE: 网站Meta描述
     */
    SITE_META_DESC("SITE: 网站Meta关键字描述"),
    /**
     * SITE: 工作时间
     */
    SITE_WORK_TIME("SITE: 工作时间")
    {
        @Override
        public String getValue()
        {
            return "9:00 - 18:00";
        }
    },
    IS_ALLOW_USER_OTHER_LOGIN("是否允许用户多终端登录(默认为否)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 费率：投资管理费率
     */
    SUCCESS_TZ_RATE("费率：投资管理费率, 如0.001")
    {
        @Override
        public String getValue()
        {
            return "0.01";
        }
    },
    /**
     * 费率：逾期罚息率
     */
    YU_QI_FAXI_RATE("费率：逾期罚息费率, 如0.001")
    {
        @Override
        public String getValue()
        {
            return "0.005";
        }
    },
    /**
     * 费率：成交服务费率
     */
    SUCCESS_BMONEY_RATE("费率：成交服务费率, 如0.001")
    {
        @Override
        public String getValue()
        {
            return "0.05";
        }
    },
    /**
     * 费率：借款管理费率
     */
    LMONEY_SUCCESS_RATION("费率：借款管理费率, 如0.001(代码还存在，功能已经废弃，不会初始化到数据库)")
    {
        @Override
        public String getValue()
        {
            return "0.003";
        }
        
        @Override
        public boolean isInit()
        {
            return false;
        }
    },
    /**
     * 费率：债权转让管理费率
     */
    ZQZRGLF_RATE("费率：债权转让手续费率, 如0.001")
    {
        @Override
        public String getValue()
        {
            return "0.01";
        }
    },
    /**
     * 手动投资：最低起投金额
     */
    MIN_BIDING_AMOUNT("手动投资：最低起投金额")
    {
        @Override
        public String getValue()
        {
            return "100.00";
        }
    },
    /**
     * 提现: 最低提取金额（单位：元）
     */
    WITHDRAW_MIN_FUNDS("提现: 最低提取金额（单位：元）")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    /**
     * 提现: 最高提取金额（单位：元）
     */
    WITHDRAW_MAX_FUNDS("提现: 最高提取金额（单位：元）")
    {
        @Override
        public String getValue()
        {
            return "200000";
        }
    },
    /**
     * 提现: 大于此金额时，提现需经过审核（单位：元）
     */
    WITHDRAW_LIMIT_FUNDS("提现: 大于此金额时，提现需经过审核（单位：元）")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
    },
    /**
     * 提现手续费计算方式, ED:按额度(默认方式);BL:按比例
     */
    WITHDRAW_POUNDAGE_WAY("提现: 手续费计算方式, ED:按额度(默认);BL:按比例")
    {
        @Override
        public String getValue()
        {
            return "ED";
        }
    },
    /**
     * 提现手续费:按额度计算,每笔(1-5万)收取的手续费
     */
    WITHDRAW_POUNDAGE_1_5("提现手续费:按额度计算,每笔(1-5万)收取的手续费")
    {
        @Override
        public String getValue()
        {
            return "3";
        }
    },
    /**
     * 提现手续费:按额度计算,每笔(5-20万)收取的手续费
     */
    WITHDRAW_POUNDAGE_5_20("提现手续费:按额度计算,每笔(5-20万)收取的手续费")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 提现手续费:按比例计算,每笔收取其金额的比例, 如0.001
     */
    WITHDRAW_POUNDAGE_PROPORTION("提现手续费:按比例计算,每笔收取其金额的比例, 如0.001")
    {
        @Override
        public String getValue()
        {
            return "0.001";
        }
    },
    /**
     * 费率: 提前还款费率, 如0.001
     */
    FANACING_PREPAYMENT_RATES("费率: 提前还款费率, 如0.001(功能已经屏蔽)")
    {
        @Override
        public String getValue()
        {
            return "0.01";
        }
        
        @Override
        public boolean isInit()
        {
            return false;
        }
    },
    /**
     * 用户自动投资设置: 大于等于最低可用金额，才能开启自动投资
     */
    AUTO_BIDING_MUST_AMOUNT("用户自动投资设置: 大于等于最低可用金额，才能开启自动投资")
    {
        @Override
        public String getValue()
        {
            return "1000";
        }
    },
    /**
     * 用户自动投资: 最低投资金额
     */
    AUTO_BIDING_MIN_AMOUNT("用户自动投资设置: 最低投资金额")
    {
        @Override
        public String getValue()
        {
            return "200";
        }
    },
    /**
     * 用户自动投资设置: 投资金额的倍数
     */
    AUTO_BIDING_MULT_AMOUNT("用户自动投资设置: 投资金额的倍数")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
        
    },
    
    /**
     * 平台特性介绍： 投资理财特性描述
     */
    FETURE_TZLC("平台特性介绍： 投资理财特性描述")
    {
        @Override
        public String getValue()
        {
            return "成为理财人，通过主动投资或加入优选理财计划将资金进行出借投资，可获得预期12-15%的稳定年化利率。";
        }
    },
    /**
     * 平台特性介绍： 快捷贷款特性描述
     */
    FETURE_KJDK("平台特性介绍： 快捷贷款特性描述")
    {
        @Override
        public String getValue()
        {
            return "成为借款人，按照要求完善个人信息，通过发标进行贷款，最快2小时可获得所需资金。";
        }
    },
    /**
     * 平台特性介绍: 本息保障特性描述
     */
    FETURE_BZBZ("平台特性介绍: 本息保障特性描述")
    {
        @Override
        public String getValue()
        {
            return "所有投资标的100%适用本息保障计划，如遇贷款人违约，${SYSTEM.SITE_NAME}将通过风险备用金有效保障理财人的本息安全。";
        }
    },
    /**
     * 定时任务(需重启)： 停止自动投资的进度,如0.9，达到标90%时停止自动投资
     */
    AUTO_BIDING_END_PROGRESS("定时任务(需重启)： 停止自动投资的进度,如0.9，达到标90%时停止自动投资")
    {
        @Override
        public String getValue()
        {
            return "0.95";
        }
    },
    /**
     * 定时任务(需重启)：单笔投资最大百分比，例如：0.2，借款10000，单笔最大投资金额是10000 * 0.2
     */
    AUTO_BIDING_MAX_PERCENT("定时任务(需重启)：单笔投资最大百分比，例如：0.2，借款10000，单笔最大投资金额是10000 * 0.2")
    {
        @Override
        public String getValue()
        {
            return "0.2";
        }
    },
    /**
     * 充值推广: 被推广人充值大于等于此金额时，才会有充值推广奖励(单位：元)
     */
    TG_YXCZJS("充值推广: 被推广人充值大于等于此金额时，才会有充值推广奖励(单位：元)")
    {
        @Override
        public String getValue()
        {
            return "5000";
        }
    },
    /**
     * 充值推广: 首笔充值推广奖励金额，被推广人达到推广奖励充值金额下限(单位：元)
     */
    TG_YXTGJL("充值推广: 首笔充值推广奖励金额，被推广人达到推广奖励充值金额下限(单位：元)")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 充值推广: 每月每人奖励上限次数
     */
    TG_YXTGSX("充值推广: 每月每人奖励上限次数")
    {
        @Override
        public String getValue()
        {
            return "50";
        }
    },
    /**
     * 投资推广: 被推广人投资金额大于等于此金额时，才会有投资推广奖励(单位：元)
     */
    TG_TZJS("投资推广: 被推广人投资金额大于等于此金额时，才会有投资推广奖励(单位：元)")
    {
        @Override
        public String getValue()
        {
            return "5000";
        }
    },
    /**
     * 投资推广: 投资推广奖励金额,投资每笔奖励金额(单位：元)
     */
    TG_TZJL("投资推广: 投资推广奖励金额,投资每笔奖励金额(单位：元)")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 认证： 当天实名认证上限次数
     */
    SM_RZCS("认证： 当天实名认证上限次数")
    {
        @Override
        public String getValue()
        {
            return "2";
        }
    },
    /**
     * 认证： 邮件认证上限次数
     */
    YJ_RZCS("认证： 邮件认证上限次数")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 认证： 短信认证上限次数
     */
    DX_RZCS("认证： 短信认证上限次数")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 认证： 最多添加银行卡数量
     */
    MAX_BANKCARD_COUNT("认证： 最多添加银行卡数量")
    {
        @Override
        public String getValue()
        {
            return "3";
        }
    },
    /**
     * 平台介绍： 平台所属公司名称
     */
    COMPANY_NAME("平台介绍： 平台所属公司名")
    {
        @Override
        public String getValue()
        {
            return "深圳市迪蒙网络科技有限公司";
        }
    },
    /**
     * 平台介绍： 平台所属公司地址
     */
    COMPANY_ADDRESS("平台介绍： 平台所属公司地址")
    {
        @Override
        public String getValue()
        {
            return "广东省深圳市罗湖区振业大厦28楼";
        }
    },
    /* *//**
              * 严重逾期扣除积分数
              */
    /*
     * YZYQJFCF("严重逾期扣除积分数") {
     * 
     * @Override public String getValue() { return "30"; } },
     */
    /**
     * 交易密码当日允许验证次数
     */
    WITHDRAW_MAX_INPUT("交易密码当日允许验证次数")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 付息方式
     */
    FXFS("付息方式")
    {
        @Override
        public String getValue()
        {
            return "GDR";
        }
    },
    /**
     * 固定日
     */
    GDR("固定日")
    {
        @Override
        public String getValue()
        {
            return "20";
        }
    },
    /**
     * 起息天数
     */
    QXTS("起息天数")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    /**
     * 担保方案
     */
    DBFA("担保方案")
    {
        @Override
        public String getValue()
        {
            return "BXQEDB";
        }
    },
    /**
     * 是否自动放款
     */
    SFZDFK("是否自动放款")
    {
        @Override
        public String getValue()
        {
            return "F";
        }
    },
    /**
     * 计息日
     */
    BID_JXR("计息日")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    /**
     * 是否允许流标
     */
    SFYXLB("是否允许流标")
    {
        @Override
        public String getValue()
        {
            return "F";
        }
    },
    /**
     * 是否资金托管
     */
    SFZJTG("是否开启资金托管, true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 前台页头Logo图片
     */
    QTLOGO("SITE: 前台页头Logo图片")
    {
        @Override
        public String getValue()
        {
            return "logo.jpg";
        }
    },
    /**
     * 前台页尾Logo图片
     */
    QTYWLOGO("SITE: 前台页尾Logo图片")
    {
        @Override
        public String getValue()
        {
            return "footer_logo.png";
        }
    },
    /**
     * 后台登录LOGO图片
     */
    HTDLLOGO("SITE: 后台登录LOGO图片")
    {
        @Override
        public String getValue()
        {
            return "logo.jpg";
        }
    },
    /**
     * 违约费率(合同中显示的违约费率)
     */
    WYJ_RATE("违约费率(合同中显示的违约费率)")
    {
        @Override
        public String getValue()
        {
            return "0.005";
        }
    },
    /**
     * 后台首页Logo图片
     */
    HTSYLOGO("SITE: 后台首页Logo图片")
    {
        @Override
        public String getValue()
        {
            return "logo.jpg";
        }
    },
    
    /**
     * 前台微信二维码图片
     */
    QTWXEWM("SITE: 前台微信二维码图片")
    {
        @Override
        public String getValue()
        {
            return "weixin.jpg";
        }
    },
    /**
     * 前台微博二维码图片
     */
    QTWBEWM("SITE: 前台微博二维码图片")
    {
        @Override
        public String getValue()
        {
            return "weibo.jpg";
        }
    },
    /**
     * 前台手机客户端图片
     */
    QTSJKHD("SITE: 前台手机客户端图片")
    {
        @Override
        public String getValue()
        {
            return "app.jpg";
        }
    },
    /**
     * 水印图标
     */
    WATERIMAGE("SITE: 水印图标")
    {
        @Override
        public String getValue()
        {
            return "waterimage.png";
        }
    },
    
    /**
     * 标的默认图标
     */
    BDMRTB("SITE: 标的默认图标")
    {
        @Override
        public String getValue()
        {
            return "biao.jpg";
        }
    },
    
    /**
     * 用户中心头像图标（男）
     */
    TXNANTB("SITE: 用户中心头像图标（男）")
    {
        @Override
        public String getValue()
        {
            return "woman_portrait.jpg";
        }
    },
    
    /**
     * 用户中心头像图标（女）
     */
    TXNVTB("SITE: 用户中心头像图标（女）")
    {
        @Override
        public String getValue()
        {
            return "portrait.jpg";
        }
    },
    
    /**
     * 客服邮箱
     */
    KFYX("SITE: 客服邮箱")
    {
        @Override
        public String getValue()
        {
            return "kefu@dimeng.net";
        }
    },
    /**
     * 客服QQ号码
     */
    KFQQ("SITE: 客服QQ号码")
    {
        @Override
        public String getValue()
        {
            return "499849064";
        }
    },
    /**
     * 投资： 是否自己可投自己的标,true:可以,false,不可以
     */
    BID_SFZJKT("投资： 是否自己可投自己的标,true:可以,false,不可以")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 推广: 是否开启推广,true:开启,false,不开启
     */
    ACCOUNT_SFTG("推广: 是否开启推广,true:开启,false,不开启")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 还款计算:年表示的天数
     */
    REPAY_DAYS_OF_YEAR("还款计算:年表示的天数")
    {
        @Override
        public String getValue()
        {
            return "360";
        }
    },
    /**
     * 借款： 本息到期，一次付清（按天计），起借天数
     */
    JK_BXDQ_LEAST_DAYS("借款： 本息到期，一次付清（按天计），起借天数")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    /**
     * 平台介绍：公司传真地址
     */
    COMPANY_FAX("平台介绍：公司传真地址")
    {
        @Override
        public String getValue()
        {
            return "0755-25879477";
        }
    },
    /**
     * 平台介绍：公司联系电话地址
     */
    COMPANY_CONTACT_TEL("平台介绍：公司联系电话")
    {
        @Override
        public String getValue()
        {
            return "0755 - 2586 3308";
        }
    },
    /**
     * 接口私钥
     */
    PRI_KEY_PATH("接口私钥")
    {
        @Override
        public String getValue()
        {
            return "C:/p2p/xd/prkey.key";
        }
    },
    /**
     * 接口公钥
     */
    PUB_KEY_PATH("接口公钥")
    {
        @Override
        public String getValue()
        {
            return "C:/p2p/xd/pbkey.key";
        }
    },
    
    /**
     * 平台介绍：公司QQ号码
     */
    COMPANY_QQ_NUMBER("平台介绍：公司QQ号码")
    {
        @Override
        public String getValue()
        {
            return "8695235763";
        }
    },
    /**
     * 债权转让是否自己可购买，false：不可以，true：可以
     */
    ZQZR_SFZJKT("债权转让是否自己可购买，false：不可以，true：可以")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 平台特性介绍： 高收益
     */
    FETURE_GSY("平台特性介绍： 高收益")
    {
        @Override
        public String getValue()
        {
            return "<span class='blue f16'><strong>49</strong></span>倍银行活期存款利息，<br/>"
                + "<span class='blue f16'><strong>3.9</strong></span>倍余额宝收益预期<br/>" + "<span class='blue f16'>"
                + "<strong>12—16%</strong></span>的稳定年化利率。";
        }
    },
    /**
     * 系统： 验证码超时时间(单位：毫秒), 默认为3分钟
     */
    VERIFY_CODE_TIME_OUT("系统： 验证码超时时间(单位：毫秒)")
    {
        @Override
        public String getValue()
        {
            return "180000";
        }
    },
    /**
     * 系统： Session会话超时时间(单位：毫秒),默认为0.5小时
     */
    SESSION_MAX_IDLE_TIME("系统： Session会话超时时间(单位：毫秒)")
    {
        @Override
        public String getValue()
        {
            return "1800000";
        }
    },
    /**
     * 债权转让： 债权持有多少天后可转让
     */
    ZQZR_CY_DAY("债权转让： 债权持有多少天后可转让")
    {
        @Override
        public String getValue()
        {
            return "90";
        }
    },
    /**
     * 债权价值比例设置： 常量值为区间（比例下限-比例上限），数值最大保留两位小数。 0-0表示不限
     */
    ZQZRBL("债权价值比例设置： 常量值为区间（比例下限-比例上限），数值最大保留两位小数。     0-0表示不限定上下限")
    {
        @Override
        public String getValue()
        {
            return "1-1.2";
        }
    },
    /**
     * 平台特性介绍： 首页展示收益率
     */
    INDEX_SHOW_POUNDAGE("平台特性介绍： 首页展示收益率")
    {
        @Override
        public String getValue()
        {
            return "18%+3.6%";
        }
    },
    /**
     * 费率： 提前还款手续费费率，未还本金 * 费率
     */
    FORWARD_REPAY_POUNDAGE("费率： 提前还款手续费率,例0.001,未还本金 * 费率")
    {
        @Override
        public String getValue()
        {
            return "0.001";
        }
    },
    /**
     * 费率： 提前还款违约金费率，未还本金 * 费率
     */
    FORWARD_REPAY_BREACH_POUNDAGE("费率： 提前还款违约费率,例0.001")
    {
        @Override
        public String getValue()
        {
            return "0.001";
        }
    },
    /**
     * 提现手续费: 扣除方式(true:内扣，从提现金额里面扣，false：外扣，提现金额+提现手续费)
     */
    TXSXF_KCFS("提现手续费: 扣除方式(true:内扣，从提现金额里面扣，false：外扣，提现金额+提现手续费)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
        
    },
    /**
     * 文件上传： 允许上传的文件类型
     */
    ALLOW_UPLOAD_FILE_TYPE("文件上传： 允许上传的文件类型")
    {
        @Override
        public String getValue()
        {
            return "jpg,gif,png,jpeg";
        }
        
    },
    /**
     * 优选理财投资下限,功能已经屏蔽
     */
    YXLCTZXX("优选理财投资下限，功能已经屏蔽")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
        
        @Override
        public boolean isInit()
        {
            return false;
        }
    },
    /**
     * LUHN验证不通过的银行卡号
     */
    ALLOW_BANKCARDS("LUHN验证不通过的银行卡号")
    {
        @Override
        public String getValue()
        {
            return "966666393740195612,4019335762667420";
        }
    },
    /**
     * 投资奖励利率
     */
    TB_JLLL_RATE("投资奖励利率")
    {
        @Override
        public String getValue()
        {
            return "0.005";
        }
    },
    /**
     * 允许登录错误次数
     */
    ALLWO_LOGIN_ERROR_TIMES("允许登录错误次数")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 是否需要验证码(false:不需要,true:需要)
     */
    SFXYYZM("是否需要验证码")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 微信APP ID
     */
    WEIXIN_APP_ID("微信APP_ID")
    {
        @Override
        public String getValue()
        {
            return "xxxx";
        }
        
    },
    /**
     * 微信APP 秘钥
     */
    WEIXIN_APP_SECRET("微信APP秘钥")
    {
        @Override
        public String getValue()
        {
            return "xxxx";
        }
        
    },
    /**
     * 微信APP 秘钥
     */
    WEIXIN_API_SERVICE_TOKEN("微信jsAPI秘钥")
    {
        @Override
        public String getValue()
        {
            return "xxxx";
        }
        
    },
    /**
     * 用户名规则正则表达式
     */
    NEW_USERNAME_REGEX("用户名规则正则表达式")
    {
        @Override
        public String getValue()
        {
            return "/^[a-zA-Z]([\\w]{5,17})$/";
        }
    },
    /**
     * 用户名规则正则表达式提示消息
     */
    USERNAME_REGEX_CONTENT("用户名规则正则表达式提示消息")
    {
        @Override
        public String getValue()
        {
            return "6-18个字，可使用字母、数字、下划线，需以字母开头";
        }
    },
    /**
     * 密码规则正则表达式
     */
    NEW_PASSWORD_REGEX("密码规则正则表达式")
    {
        @Override
        public String getValue()
        {
            return "/^(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,16}$/";
            /*
             * return
             * "/^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/";
             */
        }
    },
    /**
     * 密码规则正则表达式
     */
    PASSWORD_REGEX_CONTENT("密码规则正则表达式提示消息")
    {
        @Override
        public String getValue()
        {
            return "数字+字母+特殊字符，8-12位长度";
        }
    },
    
    /**
     * 交易密码规则正则表达式
     */
    NEW_TXPASSWORD_REGEX("交易密码规则正则表达式")
    {
        @Override
        public String getValue()
        {
            return "/^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$/";
        }
    },
    /**
     * 密码规则正则表达式
     */
    TXPASSWORD_REGEX_CONTENT("交易密码规则正则表达式提示消息")
    {
        @Override
        public String getValue()
        {
            return "密码需由八个字符组成：字母+数字，且首个字符必须是字母";
        }
    },
    /**
     * 默认信用额度
     */
    DEFAULT_CREDIT_AMOUNT("默认信用额度")
    {
        @Override
        public String getValue()
        {
            return "1000.00";
        }
    },
    /**
     * 一天中，一个IP地址允许注册账号的数量
     */
    IP_ALLOW_REGISTER_COUNT("一天中，一个IP地址允许注册账号的数量（值为0，则不限制）")
    {
        @Override
        public String getValue()
        {
            return "0";
        }
    },
    /**
     * 允许最大在线客服数量
     */
    ALLOW_ZXKF_COUNT("允许最大在线客服数量")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 允许最大产品个数
     */
    ALLOW_PRODUCT_COUNT("允许最大产品个数")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    /**
     * 是否需要密保安全验证(false:不需要,true:需要)
     */
    SFXYMBAQNZ("是否需要密保安全验证")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 公益捐赠描述
     */
    GYB_DESC("公益捐赠描述")
    {
        @Override
        public String getValue()
        {
            return "衣家人公益了解到广西梧州市岑溪市波塘镇六岑小学地处偏远山区，设有学前班至三年级，全校有49名学生，其中一名14岁的男孩为智障儿童，剩下的48个学生年龄在5到12岁之间。这些孩子大多是留守儿童，家庭条件都很差，而且很多孩子们上学需要走一个多小时不好走的山路，孩子们急需温暖的衣服和鞋。 现在好心人捐赠的15双鞋子到了，但是还有34个孩子没有一双温暖的鞋，穿着破旧的拖鞋。这里的孩子们还需要夏季的衣服，如果您家里有闲置的儿童衣物，请不要让它们在柜子里“发霉”了，伸出您温暖的手，送给这些偏远山区的孩子们吧！<br/> 世界不缺少爱，只是缺少爱的途径。";
        }
        
    },
    /**
     * 是否必须邮箱认证(false:非必须邮箱认证,true:必须邮箱认证)
     */
    /*
     * SFBXYXRZ("是否必须邮箱认证") {
     * 
     * @Override public String getValue() { return "true"; } },
     */
    CREDIT_LOAN_RATE_MAX("信用贷借款年化利率最大值,如0.24")
    {
        @Override
        public String getValue()
        {
            return "0.24";
        }
    },
    CREDIT_LOAN_AMOUNT_MIN("信用贷借款额度最小值")
    {
        @Override
        public String getValue()
        {
            return "3000";
        }
    },
    CREDIT_LOAN_AMOUNT_MAX("信用贷借款额度最大值")
    {
        @Override
        public String getValue()
        {
            return "10000";
        }
    },
    CREDIT_LOAN_RATE_MIN("信用贷借款年化利率最小值,如0.01")
    {
        @Override
        public String getValue()
        {
            return "0.01";
        }
    },
    CREDIT_LOAN_QX_MAX("信用贷借款期限最大值")
    {
        @Override
        public String getValue()
        {
            return "36";
        }
    },
    CREDIT_LOAN_QX_MIN("信用贷借款期限最小值")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    CREDIT_LOAN_SHSJ_MAX("信用贷审核时间最大值")
    {
        @Override
        public String getValue()
        {
            return "3";
        }
    },
    CREDIT_LOAN_SHSJ_MIN("信用贷审核时间最小值")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    CREDIT_LOAN_AGE_MAX("申请信用贷最大年龄")
    {
        @Override
        public String getValue()
        {
            return "60";
        }
    },
    CREDIT_LOAN_AGE_MIN("申请信用贷最小年龄")
    {
        @Override
        public String getValue()
        {
            return "18";
        }
    },
    CREDIT_IS_SHOW_DEBX("是否展示等额本息（只针对前台信用贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    CREDIT_IS_SHOW_MYFX("是否展示每月付息,到期还本（只针对前台信用贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    CREDIT_IS_SHOW_YCFQ("是否展示本息到期一次付清（只针对前台信用贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    CREDIT_IS_SHOW_DEBJ("是否展示等额本金（只针对前台信用贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    GUARANTEE_LOAN_RATE_MAX("担保贷借款年化利率最大值,如0.24")
    {
        @Override
        public String getValue()
        {
            return "0.24";
        }
    },
    GUARANTEE_LOAN_AMOUNT_MIN("担保贷借款额度最小值")
    {
        @Override
        public String getValue()
        {
            return "3000";
        }
    },
    GUARANTEE_LOAN_AMOUNT_MAX("担保贷借款额度最大值")
    {
        @Override
        public String getValue()
        {
            return "10000";
        }
    },
    GUARANTEE_LOAN_RATE_MIN("担保贷借款年化利率最小值,如0.01")
    {
        @Override
        public String getValue()
        {
            return "0.01";
        }
    },
    GUARANTEE_LOAN_QX_MAX("担保贷借款期限最大值")
    {
        @Override
        public String getValue()
        {
            return "36";
        }
    },
    GUARANTEE_LOAN_QX_MIN("担保贷借款期限最小值")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    GUARANTEE_LOAN_SHSJ_MAX("担保贷审核时间最大值")
    {
        @Override
        public String getValue()
        {
            return "3";
        }
    },
    GUARANTEE_LOAN_SHSJ_MIN("担保贷审核时间最小值")
    {
        @Override
        public String getValue()
        {
            return "1";
        }
    },
    GUARANTEE_LOAN_AGE_MAX("申请担保贷最大年龄")
    {
        @Override
        public String getValue()
        {
            return "60";
        }
    },
    GUARANTEE_LOAN_AGE_MIN("申请担保贷最小年龄")
    {
        @Override
        public String getValue()
        {
            return "18";
        }
    },
    GUARANTEE_IS_SHOW_DEBX("是否展示等额本息（只针对前台担保贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    GUARANTEE_IS_SHOW_MYFX("是否展示每月付息,到期还本（只针对前台担保贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    GUARANTEE_IS_SHOW_YCFQ("是否展示本息到期一次付清（只针对前台担保贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    GUARANTEE_IS_SHOW_DEBJ("是否展示等额本金（只针对前台担保贷）")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * QQ登陆APPKEY
     */
    INDEX_QQ_KEY("QQ登陆APPKEY")
    {
        @Override
        public String getValue()
        {
            return "101173448";
        }
        
    },
    /**
     * 新浪微博登陆APPKEY
     */
    INDEX_SINA_KEY("新浪微博登陆APPKEY")
    {
        @Override
        public String getValue()
        {
            return "2876050872";
        }
        
    },
    THIRD_PART_STATISTICS_SETTING("第三方统计设置[百度统计]"),
    /**
     * 首页问候语-子夜
     */
    INDEX_GREETING_ZY("首页问候语-子夜")
    {
        @Override
        public String getValue()
        {
            return "子夜了！据说长得帅的人都已经休息了，您呢？";
        }
    },
    /**
     * 首页问候语-凌晨
     */
    INDEX_GREETING_LC("首页问候语-凌晨")
    {
        @Override
        public String getValue()
        {
            return "凌晨了！年轻是一种资本，也不要肆意挥霍，早些休息吧！";
        }
    },
    /**
     * 首页问候语-早上
     */
    INDEX_GREETING_ZS("首页问候语-早上")
    {
        @Override
        public String getValue()
        {
            return "早上好！给胃美味早餐，它给您健康体魄！";
        }
    },
    /**
     * 首页问候语-上午
     */
    INDEX_GREETING_SW("首页问候语-上午")
    {
        @Override
        public String getValue()
        {
            return "上午好！一天之计在于晨，开始奋斗吧！";
        }
    },
    /**
     * 首页问候语-中午
     */
    INDEX_GREETING_ZW("首页问候语-中午")
    {
        @Override
        public String getValue()
        {
            return "中午好！世界上最美好的莫过于午饭后的小憩了！";
        }
    },
    /**
     * 首页问候语-下午
     */
    INDEX_GREETING_XW("首页问候语-下午")
    {
        @Override
        public String getValue()
        {
            return "下午好！来杯香浓拿铁，微笑面对一切挑战！";
        }
    },
    /**
     * 推广文本
     */
    TGWB("推广文本")
    {
        @Override
        public String getValue()
        {
            return "我在SITE_NAME投资已获得很高的收益，您也快来试试吧！http://SITE_DOMAIN/register.html?code=xxx";
        }
    },
    /**
     * 推广文本
     */
    APPTGWB("APP端推广文本")
    {
        @Override
        public String getValue()
        {
            return "我在SITE_NAME投资已获得很高的收益，您也快来试试吧！http://SITE_DOMAIN/app/register.jsp?yqm=xxx";
        }
    },
    /**
     * 首页问候语-晚上
     */
    INDEX_GREETING_WS("首页问候语-晚上")
    {
        @Override
        public String getValue()
        {
            return "晚上好！在忙碌中找寻休憩，在平淡中找寻快乐！";
        }
    },
    /**
     * 托管前缀
     */
    ESCROW_PREFIX("支付标识（网关模式，除通联代付外，可以不填）托管:FUYOU,yeepay,shuangqian,baofu,huifu,通联代付:ALLINPAY")
    {
        @Override
        public String getValue()
        {
            return "";
        }
    },
    /**
     * 每次体验金返还条数
     */
    EXPERIENCE_RET_COUNT("每次体验金返还条数")
    {
        @Override
        public String getValue()
        {
            return "20";
        }
    },
    /**
     * 第三方每次处理最大订单数
     */
    THIRD_MAX_ORDERIDS("第三方每次处理最大订单数")
    {
        @Override
        public String getValue()
        {
            return "200";
        }
    },
    /**
     * 实名认证中修改实名信息等待时间
     */
    SMRZ_MAX_WAITTIME("实名认证中修改实名认证信息等待时间[默认30分钟]")
    {
        @Override
        public String getValue()
        {
            return "30";
        }
    },
    IS_ALLOW_ACCESS_APP_ADDRESS("是否允许访问移动端地址")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    HAS_WEIXIN("是否有微信端")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    HAS_APP("是否有APP终端")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    IS_AUTOBID("是否允许自动投标")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 用户一天最多可提交投诉及建议的数量
     */
    ALLWO_ADVICE_TIMES("用户一天最多可提交投诉及建议的数量")
    {
        @Override
        public String getValue()
        {
            return "10";
        }
    },
    /**
     * 网站请求协议。格式：http:// 或 https://
     */
    SITE_REQUEST_PROTOCOL("网站请求协议。格式：http:// 或 https://")
    {
        @Override
        public String getValue()
        {
            return "http://";
        }
    },
    /**
     * 第三方域名
     */
    THIRD_DOMAIN("第三方域名：不需要拦截的第三方域名地址(多个域名用“,”号隔开)")
    {
        @Override
        public String getValue()
        {
            return "220.181.25.233,ceshi.allinpay.com";
        }
    },
    /**
     * 不拦截的url
     */
    UNCATCH_URL("不拦截的url：多个地址“,”号隔开"),
    
    /**
     * 活动发放短信通知开关,true(开启),false(关闭)
     */
    IS_OPEN_SEND_AWARD_MSG("活动发放短信通知开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    WX_APPID("微信应用唯一标识")
    {
        @Override
        public String getValue()
        {
            return "wx7f62699d9446c4c5";
        }
    },
    WX_SECRET("微信应用密钥")
    {
        @Override
        public String getValue()
        {
            return "fa1d47e3d15724937ebf7da136e3e943";
        }
    },
    /**
     * 默认担保额度
     */
    DEFAULT_GUARATTE_AMOUNT("默认担保额度")
    {
        @Override
        public String getValue()
        {
            return "5000.00";
        }
    },
    /**
     * 垫付合同保全开关,true(开启),false(关闭)
     */
    IS_SAVE_ADVANCE_CONTRACT("垫付合同保全开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 债权转让合同保全开关,true(开启),false(关闭)
     */
    IS_SAVE_TRANSFER_CONTRACT("债权转让合同保全开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 三方/四方借款电子合同保全开关,true(开启),false(关闭)
     */
    IS_SAVE_BID_CONTRACT("三方/四方借款电子合同保全开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * 网签协议保全开关,true(开启),false(关闭)
     */
    IS_SAVE_LOAN_CONTRACT("网签协议保全开关,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    /**
     * 合同模版头部html部分
     */
    CONTRACT_TEMPLATE_HTML_HEADER("合同模版头部HTML部分"),
    
    /**
     * 合同模版尾部html部分
     */
    CONTRACT_TEMPLATE_HTML_FOOTER("合同模版尾部HTML部分"),
    
    /**
     * 是否开启网签功能,true(开启),false(关闭)
     */
    IS_HAS_NETSIGN("是否开启网签功能,true(开启),false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 邀请码描述
     */
    INVITE_CODE_DESC("邀请码描述")
    {
        @Override
        public String getValue()
        {
            return "每个用户都有一个专属邀请码，邀请好友注册时，好友输入您的邀请码注册成功，即可证明好友是由您推荐注册的用户。";
        }
    },
    
    /**
     * 是否开启图片加水印功能：true(开启)，false(关闭)
     */
    IS_OPEN_WATERIMAGE("是否开启图片加水印功能：true(开启)，false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    /**
     * 是否有我要投资产品介绍页面：true(有)，false(无)
     */
    IS_HAS_INVEST_PRODUCT_INTRODUCE("是否有我要投资产品介绍页面：true(有)，false(无)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },

    /**
     * 是否有我要借款产品介绍页面：true(有)，false(无)
     */
    IS_HAS_LOAN_PRODUCT_INTRODUCE("是否有我要借款产品介绍页面：true(有)，false(无)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 是否开通银行存管
     */
    IS_OPEN_DEPOSIT("是否开通银行存管 true:是；false:否")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     * GraphicsMagick安装目录（windows系统需要配置，linux环境不需要配置）
     */
    GRAPHICSMAGICK_INSTALL_PATH("GraphicsMagick安装目录（windows系统需要配置，linux环境不需要配置）")
    {
        @Override
        public String getValue()
        {
            return "D:\\program files\\graphicsmagick-1.3.18-q8";
        }
    },
    
    /**
     * 图片压缩后的大小：格式widthXheight
     */
    IMAGE_ZIP_SIZE("图片压缩后的大小：格式widthXheight")
    {
        @Override
        public String getValue()
        {
            return "500X500";
        }
    },
    
    /**
     * 需要压缩图片大小限制：单位kb
     */
    IMAGE_NEED_ZIP_SIZE("需要压缩图片大小限制：单位kb")
    {
        @Override
        public String getValue()
        {
            return "1024";
        }
    },
    
    /**
     * 是否开启图片压缩功能：true(开启)，false(关闭)
     */
    IS_OPEN_ZIP_IMAGE("是否开启图片压缩功能：true(开启)，false(关闭)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    
    /**
     * 页面模板
     */
    PAGE_TEMPLATE("页面模板,standard:豪华版；simple:标准版")
    {
        @Override
        public String getValue()
        {
            return "simple";
        }
    },
    
    /**
     * 电子签章合同平台简称
     */
    SIGNATURE_CONTRACT_PTNAME("电子签章合同平台简称")
    {
        @Override
        public String getValue()
        {
            return "BDF";
        }
    },
    
    /**
     * 水印图片的间隔，建议范围（20~100）,可以根据图片的大小设置
     */
    IMAGE_WATER_INTERVAL("水印图片的间隔，建议范围（20~100）,可以根据图片的大小设置")
    {
        @Override
        public String getValue()
        {
            return "20";
        }
    },
    
    /**
     * 图片压缩方式：true为百分比处理，false为像素
     */
    IMAGE_ZIP_TYPE("图片压缩方式：true为百分比处理，false为像素")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     * 登陆允失败次数（超过显示验证码）
     */
    LOGIN_FAIL_COUNT("登陆允失败次数（超过显示验证码）")
    {
        @Override
        public String getValue() { return "3"; }
    }
    ;
    
    protected final String key;
    
    protected final String description;
    
    SystemVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return SystemVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(SystemVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
