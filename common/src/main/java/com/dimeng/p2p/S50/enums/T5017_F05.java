package com.dimeng.p2p.S50.enums;


import com.dimeng.util.StringHelper;

/** 
 * 协议条款状态，QY启用，TY停用
 */
public enum T5017_F05 {
    /**
     * 启用
     */
    QY("启用"),

    /**
     * 停用
     */
    TY("停用");



    protected final String chineseName;

    private T5017_F05(String chineseName){
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
     * @return {@link T5017_F05}
     */
    public static final T5017_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5017_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
