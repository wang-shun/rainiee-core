package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * register
 */
public enum T6356_F03{

    register("register"),
    sign("sign"),
    invite("invite"),
    invest("invest"),
    cellphone("cellphone"),
    mailbox("mailbox"),
    realname("realname"),
    trusteeship("trusteeship"),
    charge("charge"),
    buygoods("buygoods");

    protected final String chineseName;

    private T6356_F03(String chineseName){
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
     * @return {@link T6356_F03}
     */
    public static final T6356_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6356_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
