package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6252_F12{


    /** 
     * 已发送到富友
     */
	YFS("已发送"),

    /** 
     * 已撤销了预授权
     */
	YCX("已撤销预授权"),
	
	/** 
	 * 已撤销了还款(转账或者划拨)
	 */
	YCXZZ("已撤销了还款(转账或者划拨)");

    protected final String chineseName;

    private T6252_F12(String chineseName){
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
     * @return {@link T6252_F13}
     */
    public static final T6252_F12 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6252_F12.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
