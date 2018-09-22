package com.dimeng.p2p.repeater.score.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <基本设置-运营设置-积分设置>
 * <功能详细描述>
 * @author  zhoucl
 * @version  [版本号, 2015年9月2日]
 */
public class SetScore extends AbstractEntity{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8538368699942876443L;

    /**
     * 基本设置-checkbox
     */
    public String baseSetCheckbox;
    
	/**
     * 注册积分-checkbox
     */
    public String registerCheckbox;
    
    /**
     * 注册积分
     */
    public String register;
    
    /**
     * 签到积分-checkbox
     */
    public String signCheckbox;
    
    /**
     * 初始积分
     */
    public String initialScore;
    
    /**
     * 递增积分
     */
    public String increaseScore;
    
    /**
     * 连续签到上限积分
     */
    public String limitScore;
    
    /**
     * 邀请有效投资用户积分-checkbox
     */
    public String inviteCheckbox;
    
    /**
     * 邀请有效投资用户积分
     */
    public String invite;
    
    /**
     * 投资积分-checkbox
     */
    public String investCheckbox;
    
    /**
     * 投资积分-钱
     */
    public String investAmount;
    
    /**
     * 投资积分-积分
     */
    public String investScore;
    
    /**
     * 手机认证积分-checkbox
     */
    public String cellPhoneCheckbox;
    
    /**
     * 手机认证积分
     */
    public String cellPhone;
    
    /**
     * 邮箱认证积分-checkbox
     */
    public String mailBoxCheckbox;
    
    /**
     * 邮箱认证积分
     */
    public String mailBox;
    
    /**
     * 实名认证积分-checkbox
     */
    public String realNameCheckbox;
    
    /**
     * 实名认证积分
     */
    public String realName;
    
    /**
     * 开通第三方托管账户-checkbox
     */
    public String trusteeshipCheckbox;
    
    /**
     * 开通第三方托管账户
     */
    public String trusteeship;
    
    /**
     * 充值-checkbox
     */
    public String chargeCheckbox;
    
    /**
     * 充值-钱
     */
    public String chargeAmount;
    
    /**
     * 充值-积分
     */
    public String chargeScore;
    
    /**
     * 现金购买商品积分-checkbox
     */
    public String buygoodsCheckbox;
    
    /**
     * 现金购买商品积分-钱
     */
    public String buygoodsAmount;
    
    /**
     * 现金购买商品积分-积分
     */
    public String buygoodsScore;
    
}
