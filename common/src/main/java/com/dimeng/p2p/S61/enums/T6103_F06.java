package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 使用状态
 */
public enum T6103_F06{

    /** 
     * 未使用
     */
    WSY("未使用"),

    //'使用状态：已过期：''YGQ'',未使用：''WSY'',已委托：''YWT'',已投资：''YTZ'',已结清：''YJQ''',

    /**
     * 使用中
     */
    YWT("使用中"),

    /**
     * 已投资
     */
    YTZ("已投资"),

    /**
     * 已结清
     */
    YJQ("已结清"),

    /** 
     * 已过期
     */
    YGQ("已过期");

    protected final String chineseName;

    private T6103_F06(String chineseName){
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
     * @return {@link T6103_F06}
     */
    public static final T6103_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6103_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
