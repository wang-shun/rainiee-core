package com.dimeng.p2p.escrow.fuyou.entity.enums;

import com.dimeng.util.StringHelper;

/**
 * 
 * 流水号
 * <迪蒙系统-富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月19日]
 */
public enum FuyouTypeEnum
{
    /** 
     * 用户注册
     */
    YHZC("用户注册"),
    
    /** 
     * 企业注册
     */
    QYZC("企业注册"),
    
    /** 
     * 用户充值
     */
    YHCZ("用户充值"),
    
    /** 
     * 用户提现
     */
    YHTX("用户提现"),
    
    /** 
     * 手动投标
     */
    SDTB("手动投标"),
    
    /** 
     * 自动投标
     */
    ZDTB("自动投标"),
    
    /** 
     * 购买债权
     */
    GMZQ("购买债权"),
    
    /** 
     * 购买商品
     */
    GMXP("购买商品"),
    
    /** 
     * 商品退款
     */
    XPTK("商品退款"),
    
    /** 
     * 手动放款
     */
    SDFK("手动放款"),
    
    /** 
     * 提前还款
     */
    TQHK("提前还款"),
    
    /** 
     * 手动还款
     */
    SDHK("手动还款"),
    
    /** 
     * 自动还款
     */
    ZDHK("自动还款"),
    
    /** 
     * 机构垫付
     */
    JGDF("机构垫付"),
    
    /** 
     * 不良资产处理
     */
    PTDF("不良资产处理"),
    
    /** 
     * 交易查询
     */
    JYCX("交易查询"),
    
    /** 
     * 充值提现查询
     */
    CTCX("交易查询"),
    
    /** 
     * 余额查询
     */
    YECX("余额查询"),
    
    /** 
     * 资金冻结
     */
    ZJDJ("资金冻结"),
    
    /** 
     * 资金解冻
     */
    ZJJD("资金解冻"),
    
    /** 
     * 推广奖励
     */
    TGJL("推广奖励"),
    
    /** 
     * 用户信息查询
     */
    YHXX("用户信息查询"),
    
    /** 
     * 借款流标
     */
    JKLB("借款流标"),
    
    /** 
     * 投标流标
     */
    TBLB("投标流标"),
    
    /** 
     * 有效推广奖励
     */
    TGYX("有效推广奖励"),
    
    /** 
     * 持续推广奖励
     */
    TGCX("持续推广奖励"),
    
    /** 
     * 投资奖励
     */
    TZJL("投资奖励"),
    
    /** 
     * 红包奖励
     */
    HBJL("红包奖励"),
    
    /** 
     * 成交服务费、借款管理费
     */
    CJFF("成交服务费、借款管理费"),
    
    /**
     * 理财管理费/投资管理费
     */
    GLF("理财管理费/投资管理费"),
    
    /**
     * 债权转让手续费
     */
    ZQZR_SXF("债权转让手续费"),
    
    /** 
     * 加息奖励
     */
    JXJL("加息奖励"),
    
    /** 
    * 体验金奖励
    */
    TLJL("体验金奖励"),
    
    /** 
     * 奖励标
     */
    JLB("奖励标"),
    
    /** 
     * 手机号修改
     */
    SJHX("手机号修改"),
    
    /** 
     * 银行卡更换
     */
    YHKK("银行卡更换"),
    
    /** 
     * 提现通知
     */
    TXTZ("提现通知"),
    
    /** 
     * 公益捐款
     */
    GYJK("公益捐款"),
    
    /** 
     * 线下充值
     */
    XXCZ("线下充值"),
    
    /** 
     * 不良债权购买
     */
    BLZQ("不良债权购买"),
    
    /** 
     * 操作对账重新生成流水号
     */
    NEW("操作对账重新生成流水号");
    
    protected final String chineseName;
    
    private FuyouTypeEnum(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName()
    {
        return chineseName;
    }
    
    /**
     * 解析字符串.
     * 
     * @return {@link FuyouTypeEnum}
     */
    public static final FuyouTypeEnum parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return FuyouTypeEnum.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
    
}
