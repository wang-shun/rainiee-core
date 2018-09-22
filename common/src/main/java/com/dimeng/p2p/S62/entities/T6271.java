/*
 * 文 件 名:  T6271.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.S62.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6271_F07;
import com.dimeng.p2p.S62.enums.T6271_F08;
import com.dimeng.p2p.S62.enums.T6271_F10;

/**
 * <合同保全列表实体>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年6月16日]
 */
public class T6271 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    public int F01;
    
    /**
     * 用户ID，参考T6110.F01
     */
    public int F02;
    
    /**
     * 标的ID,参考T6230.F01
     */
    public int F03;
    
    /**
     * 合同编号
     */
    public String F04;
    
    /**
     * 易保全ID
     */
    public String F05;
    
    /**
     * 保全时间
     */
    public Timestamp F06;
    
    /**
     * 保全状态：WBQ-未保全，YBQ-已保全
     */
    public T6271_F07 F07;
    
    /**
     * 合同类型：JKHT-借款合同，ZQZRHT-债权转让合同，DFHT-垫付合同，BLZQZRHT-不良债权转让合同
     */
    public T6271_F08 F08;
    
    /**
     * 协议本地存储路径
     */
    public String F09;
    
    /**
     * 用户类型: 用户类型: TZR-投资人，JKR-借款人，ZCR-转出人，SRR-受让人，DFR-垫付人，BDFR-被垫付人
     */
    public T6271_F10 F10;
    
    /**
     * 债权ID,参考T6251.F01
     */
    public int F11;
    
    /**
     * 不良债权ID,参考 T6265.F01
     */
    public int F12;
    
    /**
     * 债权申请ID, 参考T6260.F01
     */
    public int F13;
}
