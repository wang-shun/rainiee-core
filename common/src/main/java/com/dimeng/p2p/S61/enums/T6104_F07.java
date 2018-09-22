/*
 * 文 件 名:  T6104_F07.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月22日
 */
package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * 交易状态 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月22日]
 */
public enum T6104_F07 {

	DTJ("待提交"),
	CG("成功"),
	SB("失败");
	
	protected final String chineseName;

    private T6104_F07(String chineseName){
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
     * @return {@link T6101_F03}
     */
    public static final T6104_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6104_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
