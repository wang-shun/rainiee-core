/*
 * 文 件 名:  T6198.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6198_F04;

/**
 * <实名认证统计表>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
public class T6198 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 447205726087585650L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 错误认证次数
     */
    public int F03;
    
    /** 
     * 来源，PC:PC;APP:APP;WEIXIN:微信
     */
    public T6198_F04 F04;
    
    /** 
     * 累计登陆次数
     */
    public int F05;
    
    /** 
     * 认证通过时间
     */
    public Timestamp F06;
    
    /** 
     * 最后登陆时间
     */
    public Timestamp F07;
}
