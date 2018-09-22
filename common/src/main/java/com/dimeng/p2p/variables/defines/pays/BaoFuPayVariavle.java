package com.dimeng.p2p.variables.defines.pays;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "BAOFUPAY", name = "第三方支付-宝付支付")
public enum BaoFuPayVariavle implements VariableBean
{
    ISTEST("宝付支付是否为测试环境", true + ""), KEY("宝付支付加密KEY", "abcdefg"), MEMBER_ID("商户号", "100000178"), TERMINAL_ID("终端号",
        "100000916"), BF_DOMAIN_CHARGE_WAP("宝付_充值地址", "https://tgw.baofoo.com/apipay/wap"), BF_DOMAIN_CHECK("宝付_对账地址",
        "http://gw.baofoo.com/order/query"), BF_CHARGE_PAGEURL("宝付_充值同步地址", "http://${SYSTEM.SITE_DOMAIN}/"), MOBILE_KEY(
        "宝付_移动SDK私钥密码", "100000178_204500"), MOBILE_TERMINAL_ID("宝付_移动SDK终端号", "100000916"), BF_DOMAIN_MOBILE_CHARGE(
        "宝付_移动SDK充值同步地址", "https://tgw.baofoo.com/apipay/sdk"), BF_PUB_KEY_PATH("宝付_移动SDK公钥地址",
        "/home/webapps/p2p-zbbank/app/current/certs/baofoo/baofoo_pub.cer"), MEMBER_PFX_KEY_PATH("宝付_移动SDK商户私钥地址",
        "/home/webapps/p2p-zbbank/app/current/certs/baofoo/bfkey_100000178@@100000916.pfx"), CHARGE_RATE_BF(
        "宝付收取平台手续费", "0"), BF_GW_REQURL("宝付网关支付地址", "https://tgw.baofoo.com/wapmobile"), CHARGE_ENCRYPTION_TYPE(
        "宝付加密报文的数据类型", "xml");
    
    private String key;
    
    private String description;
    
    private String value;
    
    BaoFuPayVariavle(String description, String value)
    {
        StringBuffer sb = new StringBuffer(this.getType());
        sb.append(".");
        sb.append(this.name());
        this.key = sb.toString();
        this.description = description;
        this.value = value;
    }
    
    @Override
    public String getType()
    {
        return this.getClass().getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return this.key;
    }
    
    @Override
    public String getValue()
    {
        return this.value;
    }
    
    @Override
    public String getDescription()
    {
        return this.description;
    }
    
    @Override
    public boolean isInit()
    {
        return false;
    }
    
}
