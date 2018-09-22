/*
 * 文 件 名:  BusTradeType
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/5/18
 */
package com.dimeng.p2p.modules.business.console.service.achieve;

import com.dimeng.util.StringHelper;

/**
 * 业务员名下客户交易类型
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/5/18]
 */
public enum  BusTradeType {
    /**
     * 投资
     */
    invest("投资"),
    /**
     * 借款
     */
    loan("借款"),
    /**
     * 充值
     */
    charge("充值"),
    /**
     * 提现
     */
    withdraw("提现");

    protected final String chineseName;

    private BusTradeType(String chineseName) {
        this.chineseName = chineseName;
    }

    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     *
     * @return {@link BusTradeType}
     */
    public static final BusTradeType parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return BusTradeType.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
