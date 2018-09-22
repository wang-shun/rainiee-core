package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 活动类型
 * @author heluzhu
 *
 */
public enum T6340_F04 {
	
	/**
	 * 注册赠送
	 */
	register("注册赠送"),
	
	/**
	 * 单笔充值赠送
	 */
	recharge("单笔充值赠送"),

	/**
	 *首次充值赠送
	 */
	firstrecharge("首次充值赠送"),
	
	/**
	 * 生日赠送
	 */
	birthday("生日赠送"),
	
	/**
	 * 投资额度赠送
	 */
	investlimit("投资额度赠送"),
	
	/**
	 * 指定用户赠送
	 */
	foruser("指定用户赠送"),
	
	/**
	 * 推荐首次充值奖励
	 */
	tjsccz("推荐首次充值奖励"),
	
	/**
	 * 推荐首次投资奖励
	 */
	tjsctz("推荐首次投资奖励"),
	
	/**
	 * 推荐投资总额奖励
	 */
	tjtzze("推荐投资总额奖励"),

	/**
	 * 积分兑换
	 */
	exchange("积分兑换"),

	/**
	 * 积分抽奖
	 */
	integraldraw("积分抽奖");
	
	protected final String chineseName;

    private T6340_F04(String chineseName){
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
     * @return {@link T6340_F04}
     */
    public static final T6340_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6340_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
