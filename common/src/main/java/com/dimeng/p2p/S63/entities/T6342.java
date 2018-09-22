/*
 * 文 件 名:  T6342.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年9月30日
 */
package com.dimeng.p2p.S63.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6342_F04;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年9月30日]
 */
public class T6342 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2183646556366703093L;
    
    /**
     * 主键ID
     */
    public int F01;
    
    /**
     * 用户ID：参考T6110.F01
     */
    public int F02;
    
    /**
     * 活动规则id：参考T6344.F01
     */
    public int F03;
    
    /**
     * 使用状态： WSY-未使用，YSY-已使用，YGQ-已过期
     */
    public T6342_F04 F04;
    
    /**
     * 使用时间
     */
    public Timestamp F05;
    
    /**
     * 标ID： 参考T6230.F01
     */
    public int F06;
    
    /**
     * 赠送时间
     */
    public Timestamp F07;
    
    /**
     * 过期时间
     */
    public Timestamp F08;

    /**
     * 推荐用户
     */
    public int F09;
}
