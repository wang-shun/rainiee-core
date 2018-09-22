package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.p2p.S61.enums.T6114_F14;

/** 
 * 用户银行卡
 */
public class T6114 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 用户银行卡ID,自增ID
     */
    public int F01;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 银行ID,参考T5120.F01
     */
    public int F03;
    
    /** 
     * 开户行所在地区域ID,参考T5119.F01
     */
    public int F04;
    
    /** 
     * 开户行
     */
    public String F05;
    
    /** 
     * 银行卡号,前4位,后3位保留,其他星号代替
     */
    public String F06;
    
    /** 
     * 银行卡号,加密存储
     */
    public String F07;
    
    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T6114_F08 F08;
    
    /** 
     * 创建时间
     */
    public Timestamp F09;
    
    /** 
     * 实名认证,TG:通过;BTG:不通过;
     */
    public T6114_F10 F10;
    
    /**
     * 开户名
     */
    public String F11;
    
    /**
     * 开户名类型；1：个人，2：公司
     */
    public int F12;
    
    /**
     * 兴业银行快捷账户认证-系统跟踪号
     */
    public String F13;
    
    /**
     * 状态,WRZ:未认证;RZCG:认证成功;RZSB:认证失败;
     */
    public T6114_F14 F14;
    
    /**
     * 解绑请求流水号
     */
    public String F15;
    
    /**
     * 绑定标识号
     */
    public String F16;
    
}
