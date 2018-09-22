package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 审核状态标记
 */
public enum T6165_F02{


    /** 
     * 开户成功
     */
    Y("开户成功"),

    /** 
     * 开户中
     */
    K("开户中"),

    /** 
     * 开户失败
     */
    F("开户失败"),

    /** 
     * 审核拒绝
     */
    R("审核拒绝"),

    /** 
     * 审核中
     */
    P("审核中"),

    /** 
     * 提交
     */
    T("提交"),

    /** 
     * 初始
     */
    I("初始");

    protected final String chineseName;

    private T6165_F02(String chineseName){
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
     * @return {@link T6165_F02}
     */
    public static final T6165_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6165_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
