package com.dimeng.p2p;

import java.util.HashMap;
import java.util.Map;

public enum OrderType
{
    
    /**
     * 充值
     */
    CHARGE(10001, "充值"),
    /**
     * 提现
     */
    WITHDRAW(10002, "提现"),
    /**
     * 保证金
     */
    BOND(10003, "保证金"),
    /**
     * 逾期垫付
     */
    ADVANCE(10004, "逾期垫付"),
    /**
     * 转入保证金
     */
    BONDIN(10005, "转入保证金"),
    /**
     * 转出保证金
     */
    BONDOUT(10006, "转出保证金"),
    /**
     * 平台充值
     */
    PLATFORM_CHARGE(10007, "平台充值"),
    /**
     * 平台提现
     */
    PLATFORM_WITHDRAW(10008, "平台提现"),
    /**
     * 散标投资
     */
    BID(20001, "散标投资"),
    /**
     * 散标投资撤销
     */
    BID_CANCEL(20002, "散标投资撤销"),
    /**
     * 散标放款
     */
    BID_CONFIRM(20003, "散标放款"),
    /**
     * 散标还款
     */
    BID_REPAYMENT(20004, "散标还款"),
    /**
     * 散标债权转让
     */
    BID_EXCHANGE(20005, "散标债权转让"),
    /**
     * 体验金投资
     */
    BID_EXPERIENCE(20006, "体验金投资"),
    /**
     * 体验金放款
     */
    BID_EXPERIENCE_CONFIRM(20007, "体验金放款"),
    /**
     * 体验金还款
     */
    BID_EXPERIENCE_REPAYMENT(20008, "体验金还款"),
    /**
     * 体验金投资撤销
     */
    BID_EXPERIENCE_CANCEL(20009, "体验金投资撤销"),
    /**
     * 加息券投资
     */
    BID_COUPON(20010, "加息券投资"),
    /**
     * 加息券放款
     */
    BID_COUPON_CONFIRM(20011, "加息券放款"),
    /**
     * 加息券还款
     */
    BID_COUPON_REPAYMENT(20012, "加息券还款"),
    /**
     * 加息券投资撤销
     */
    BID_COUPON_CANCEL(20013, "加息券投资撤销"),
    
    /**
     * 红包投资
     */
    BID_RED_PACKET(20014, "红包投资"),
    
    /**
     * 购买不良债权
     */
    BUY_BAD_CLAIM(20015, "购买不良债权"),
    
    /**
     * 购买优选理财
     */
    FINANCIAL_PURCHASE(30001, "购买优选理财"),
    /**
     * 优选理财还款
     */
    FINANCIAL_REPAYMENT(30002, "优选理财还款"),
    /**
     * 优选理财放款
     */
    FINANCIAL_LOAN(30003, "优选理财放款"),
    /**
     * 提前还款
     */
    PREPAYMENT_LOAN(30004, "提前还款"),
    /**
     * 订单金额冻结
     */
    FREEZE(40001, "订单金额冻结"),
    /**
     * 订单金额解冻
     */
    UNFREEZE(40002, "订单金额解冻"),
    /**
     * 自助转账订单
     */
    AUTOTRANSFER(50000, "自助转账订单"),
    /**
     * 转账订单
     */
    TRANSFER(50001, "转账订单"),
    /**
     * 公益标转账
     */
    GYBTRANSFER(50002, "公益标转账"),
    
    /**
     * 商品购买转账
     */
    MALLTRANSFER(50003, "商品购买转账"),
    
    /**
     * 商品退款转账
     */
    MALLREFUND(50004, "商品退款转账"),
    
    /**
     * 线下充值
     */
    OFFLINECHARGE(10011, "线下充值");
    
    int orderType;
    
    String chineseName;
    
    protected static final Map<Integer, String> maps = new HashMap<Integer, String>();
    static
    {
        for (OrderType orderType : values())
        {
            maps.put(orderType.orderType, orderType.chineseName);
        }
    }
    
    private OrderType(int orderType, String chineseName)
    {
        this.orderType = orderType;
        this.chineseName = chineseName;
    }
    
    public int orderType()
    {
        return orderType;
    }
    
    public String getChineseName()
    {
        return chineseName;
    }
    
    public static String getTypeName(int typeId)
    {
        return maps.get(typeId);
    }
}
