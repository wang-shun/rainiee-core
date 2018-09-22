package com.dimeng.p2p.S65.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6501_F03{


    /** 
     * 待提交
     */
    DTJ("待提交"),

    /** 
     * 已提交
     */
    YTJ("已提交"),

    /** 
     * 待确认
     */
    DQR("待确认"),

    /** 
     * 成功
     */
    CG("成功"),

    /** 
     * 失败
     */
    SB("失败");

    protected final String chineseName;

    private T6501_F03(String chineseName){
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
     * @return {@link T6501_F03}
     */
    public static final T6501_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6501_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
