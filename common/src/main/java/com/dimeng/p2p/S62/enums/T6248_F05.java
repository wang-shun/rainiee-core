package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/**
 * 状态
 * <功能详细描述>
 * 状态,YFB:已发布;WFB:未发布;
 * @author  zengzhihua
 * @version  [版本号, 2015年3月9日]
 */
public enum T6248_F05{

    /**
     * 已发布
     */
    YFB("已发布"),

    /**
     * 未发布
     */
    WFB("未发布");

    protected final String chineseName;

    private T6248_F05(String chineseName){
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
     * @return {@link T6248_F05}
     */
    public static final T6248_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6248_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
