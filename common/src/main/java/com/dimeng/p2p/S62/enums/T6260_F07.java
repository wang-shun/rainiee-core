package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6260_F07{


    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 转让中
     */
    ZRZ("转让中"),

    /** 
     * 已结束
     */
    YJS("已结束"),
    
    /** 
     * 已付款
     */
    YFK("已付款"),

    /**
     * 已下架
     */
    YXJ("已下架"),

    /**
     * 已取消
     */
    YQX("已取消")
    ;

    protected final String chineseName;

    private T6260_F07(String chineseName){
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
     * @return {@link T6260_F07}
     */
    public static final T6260_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6260_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
