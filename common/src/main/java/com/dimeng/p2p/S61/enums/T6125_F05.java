/*
 * 文 件 名:  T6125_F05
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/**
 * 担保方申请状态
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public enum T6125_F05 {


    /**
     * 申请担保待处理
     */
    SQDCL("申请担保待处理"),

    /**
     * 取消担保待处理
     */
    QXDCL("取消担保待处理"),

    /**
     * 申请担保成功
     */
    SQCG("申请担保成功"),

    /**
     * 申请担保失败
     */
    SQSB("申请担保失败"),

    /**
     * 取消担保成功
     */
    QXCG("取消担保成功"),

    /**
     * 取消担保失败
     */
    QXSB("取消担保失败");

    protected final String chineseName;

    private T6125_F05(String chineseName){
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
     * @return {@link T6125_F05}
     */
    public static final T6125_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6125_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
