package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F12;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;

/** 
 * 用户账号表
 */
public class T6110 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 用户ID,自增
     */
    public int F01;
    
    /** 
     * 用户登录账号
     */
    public String F02;
    
    /** 
     * 用户登录密码
     */
    public String F03;
    
    /** 
     * 手机号码
     */
    public String F04;
    
    /** 
     * 邮箱
     */
    public String F05;
    
    /** 
     * 用户类型,ZRR:自然人;FZRR:非自然人
     */
    public T6110_F06 F06;
    
    /** 
     * 状态,QY启用;SD:锁定;HMD:黑名单;
     */
    public T6110_F07 F07;
    
    /** 
     * 注册来源,ZC:注册;HTTJ:后台添加
     */
    public T6110_F08 F08;
    
    /** 
     * 注册时间
     */
    public Timestamp F09;
    
    /** 
     * 担保方,S:是;F:否;
     */
    public T6110_F10 F10;
    
    /** 
     * 当日交易密码输入错误次数
     */
    public short F11;
    
    /** 
     * 是否第一次登陆系统,S:是;F:否;
     */
    public T6110_F12 F12;
    
    /** 
     * 是否删除
     */
    public T6110_F13 F13;
    
    /** 
     * IP所属地
     */
    public String F14;
    
    /** 
     * 最近投资金额
     */
    /*
    public BigDecimal F15 = BigDecimal.ZERO;

    *//** 
      * 最近投资时间
      */
    /*
    public Timestamp F16;

    *//** 
      * 是否同意合同,S:是;F:否;
      */
    /*
    public T6110_F17 F17;

    *//** 
      * 是否用友员工,S:是;F:否;
      */
    /*
    public T6110_F18 F18;*/
    
    /**
     * 签到时间
     */
    public Timestamp F15;
    
    /**
     * 连续签到天数
     */
    public int F16;
    
    /**
     * 企业帐号是否允许投资,S:是;F:否;
     */
    public T6110_F17 F17;
    
    /**
     * 企业帐号是否开启过投资功能,S:是;F:否;
     */
    public T6110_F18 F18;
    
    /**
     * 是否允许购买不良债权,S：是;F：否;
     */
    public T6110_F19 F19;
    
    /** 
     * 法大大客户编号
     */
    public String F20;
    
}
