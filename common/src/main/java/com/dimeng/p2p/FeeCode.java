package com.dimeng.p2p;

public class FeeCode
{
    // 充值类
    /**
     * 在线充值金额
     */
    public static final int CZ = 1001;
    
    /**
     * 充值手续费
     */
    public static final int CZ_SXF = 1002;
    
    /**
     * 充值成本
     */
    public static final int CZ_CB = 1003;
    
    /**
     * 线下充值金额
     */
    public static final int CZ_XX = 1004;
    
    /**
     * 平台充值
     */
    public static final int PTCZ = 1005;
    
    /**
     * 线下提现
     */
    public static final int TX_XX = 1006;
    
    // 提现类
    /**
     * 提现金额
     */
    public static final int TX = 2001;
    
    /**
     * 提现手续费
     */
    public static final int TX_SXF = 2002;
    
    /**
     * 提现成本
     */
    public static final int TX_CB = 2003;
    
    /**
     * 平台提现
     */
    public static final int PTTX = 2004;
    
    /**
     * 提现失败金额返还
     */
    public static final int TXSB = 2005;
    
    /**
     * 提现失败手续费返还
     */
    public static final int TXSB_SXF = 2006;
    
    // 投资类
    /**
     * 投资
     */
    public static final int TZ = 3001;
    
    /**
     * 投资撤销
     */
    public static final int TZ_CX = 3002;
    
    /**
     * 体验金投资
     */
    public static final int TYJTZ = 3003;
    
    // 债权转让
    /**
     * 债权转让手续费
     */
    public static final int ZQZR_SXF = 4001;
    
    /**
     * 卖出债权
     */
    public static final int ZQZR_MC = 4002;
    
    /**
     * 债权转让买入
     */
    public static final int ZQZR_MR = 4003;
    
    /**
     * 购买不良债权
     */
    public static final int BLZQ_GM = 4004;
    
    // 借款类
    /**
     * 借款本金
     */
    public static final int JK = 6001;
    
    /**
     * 借款管理费
     */
    public static final int JK_GLF = 6002;
    
    // 还款类
    /**
     * 本金
     */
    public static final int TZ_BJ = 7001;
    
    /**
     * 利息
     */
    public static final int TZ_LX = 7002;
    
    /**
     * 逾期管理费
     */
    public static final int TZ_YQGLF = 7003;
    
    /**
     * 逾期罚息
     */
    public static final int TZ_FX = 7004;
    
    /**
     * 提前还款违约金
     */
    public static final int TZ_WYJ = 7005;
    
    /**
     * 垫付
     */
    public static final int TZ_DF = 7006;
    
    /**
     * 违约金手续费
     */
    public static final int TZ_WYJ_SXF = 7007;
    
    /**
     * 投资奖励
     */
    public static final int TZ_TBJL = 7008;
    
    /**
     * 投资红包
     */
    public static final int TZ_TBHB = 7009;
    
    /**
     * 加息奖励
     */
    public static final int JX_REWARD = 7010;
    
    /**
     * 活动费用
     */
    public static final int HD = 8001;
    
    /**
     * 有效推广
     */
    public static final int TG_YX = 9001;
    
    /**
     * 持续推广
     */
    public static final int TG_CX = 9002;
    
    // 信用类
    /**
     * 发标审核信用扣除
     */
    public static final int XY_FB_TZ = 1101;
    
    /**
     * 还款信用返还
     */
    public static final int XY_HK_FH = 1102;
    
    /**
     * 流标信用返还
     */
    public static final int XY_LB_FH = 1103;
    
    /**
     * 人工调整信用额度
     */
    public static final int XY_CZ = 1104;
    
    /**
     * 默认信用额度
     */
    public static final int XY_MR = 1105;
    
    /**
     * 放款多余信用额度返还
     */
    public static final int XY_FK_FH = 1106;
    
    // 费用类
    /**
     * 成交服务费、借款管理费
     */
    public static final int CJFWF = 1201;
    
    /**
     * 理财管理费/投资管理费
     */
    public static final int GLF = 1202;
    
    // 优选理财
    /**
     * 投优选理财
     */
    public static final int YXLC = 1301;
    
    /**
     * 优选加入费
     */
    public static final int YXLC_JR = 1302;
    
    /**
     * 优选服务费
     */
    public static final int YXLC_FW = 1303;
    
    /**
     * 优选退出费
     */
    public static final int YXLC_TC = 1304;
    
    /**
     * 优选理财还款
     */
    public static final int YXLC_HK = 1305;
    
    /**
     * 优选理财利息
     */
    public static final int YXLC_LX = 1306;
    
    /**
     * 风险保证金充值
     */
    public static final int FXBZJ = 1401;
    
    /**
     * 风险保证金转出
     */
    public static final int FXBZJ_ZC = 1402;
    
    /**
     * 身份验证手续费
     */
    public static final int SFYZSXF = 1501;
    
    /**
     * 查标服务费
     */
    public static final int CBFWF = 1502;
    
    /**
     * 担保费
     */
    public static final int DBF = 1503;
    
    /**
     * 平台划拨
     */
    public static final int PTHB = 1701;
    
    /**
     * 商品交易
     */
    public static final int MALL_BUY = 1702;
    
    /**
     * 商品退款
     */
    public static final int MALL_REFUND = 1703;
    
    /**
     * 撤销商品交易
     */
    public static final int MALL_NOPASS = 1704;
    
    /****************** 统计费用 *****************************/
    /**
     * 公司虚拟户到借款人虚拟户的总额
     */
    public static final int GSDJKRZE = 1601;
    
    /**
     * 借款人虚拟户到投资人卡的总额；
     */
    public static final int JKRDTZRZE = 1602;
    
    /**
     * 公益标捐款
     */
    public static final int PUBLIC_GOOD_ID = 1801;
    
    /****************** 担保额度 *****************************/
    /**
     * 发标审核担保额度扣除
     */
    public static final int DB_FB_KC = 1301;
    
    /**
     * 流标担保额度返还
     */
    public static final int DB_LB_FH = 1302;
    
    /**
     * 还款担保额度返还
     */
    public static final int DB_HK_FH = 1303;
    
    /**
     * 人工调整担保额度
     */
    public static final int DB_CZ = 1304;
    
    /**
     * 默认担保额度
     */
    public static final int DB_MR = 1305;
    
    /**
     * 放款多余担保额度返还
     */
    public static final int DB_FK_FH = 1306;

    /**
     * 购买不良债权担保额度赠送
     */
    public static final int DB_BLZQGM_ZS = 1307;
}
