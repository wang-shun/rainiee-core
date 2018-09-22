package com.dimeng.p2p.S62.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F08;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;

/**
 * 法大大电子签章合同列表
 * 文  件  名：T6273.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月14日
 */
public class T6273 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID/主键ID
     */
    public int F01;
    
    /** 
     * 用户ID，参考T6110.F01
     */
    public int F02;
    
    /** 
     * 标ID,参考T6230.F01
     */
    public int F03;
    
    /** 
     * 合同编号
     */
    public String F04;
    
    /** 
     * 电子签章ID
     */
    public String F05;
    
    /** 
     * 签名时间
     */
    public Timestamp F06;
    
    /** 
     * 签名状态：DQ-待签名，YQ-已签名
     */
    public T6273_F07 F07;
    
    /** 
     * 合同类型：JKHT-借款合同，ZQZRHT-债权转让合同
     */
    public T6273_F08 F08;
    
    /** 
     * 合同本地存储路径
     */
    public String F09;
    
    /** 
     * 用户类型: TZR-投资人，JKR-借款人，ZCR-转出人，SRR-受让人 , DFR("垫付人"), BDFR("被垫付人")
     */
    public T6273_F10 F10;
    
    /** 
     * 债权ID,参考T6251.F01
     */
    public int F11;
    
    /** 
     * 债权申请ID, 参考T6260.F01
     */
    public int F12;
    
    /** 
     * 创建时间
     */
    public Timestamp F13;
    
    /**
     * 投资记录ID，参考T6250.F01
     */
    public int F14;
    
    /** 
     * 签章状态: DSQ-待申请，DSC-待上传，DQM-待签名，DGD-待归档，YGD-已归档
     */
    public T6273_F15 F15;
    
    /** 
     * 在线查看已签文档的地址
     */
    public String F16;
    
    /** 
     * 签章后文档下载地址
     */
    public String F17;
    
    /**
     * 债权转让id，参考T6251.F01
     */
    public int F18;
    
    /** 
     * 签章请求交易号
     */
    public String F19;
    
    /** 
     * 合同标题
     */
    public String F20;
    
}
