package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否奖励标,S:是;F:否
 */
public enum T6231_F27 {
	 /**
     * 是
     */
    S("是"),

    /**
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6231_F27(String chineseName){
        this.chineseName = chineseName;
    }
    public String getChineseName() {
        return chineseName;
    }
    public static final T6231_F27 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6231_F27.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
