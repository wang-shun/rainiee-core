package com.dimeng.p2p.escrow.fuyou.entity.enums;


import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.util.StringHelper;

/** 
 * 冻结状态
 */
public enum T6170_F06{

    /** 
     * 冻结中
     */
    DJZ("冻结中"),

    /** 
     * 已解冻
     */
    YJD("已解冻");

    protected final String chineseName;

    private T6170_F06(String chineseName){
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
     * @return {@link T6110_F06}
     */
    public static final T6170_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6170_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
