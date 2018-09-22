package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/**
 * 
 * 协议保全状态
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月16日]
 */
public enum T6272_F06{


    /** 
     * 未保全
     */
    WBQ("未保全"),

    /** 
     * 已保全
     */
    YBQ("已保全");


    protected final String chineseName;

    private T6272_F06(String chineseName){
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
     * @return {@link T6272_F06}
     */
    public static final T6272_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6272_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
