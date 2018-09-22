package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

public class Enterprise extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 企业编号
     */
    public String F02;

    /** 
     * 营业执照登记注册号,唯一
     */
    public String F03;

    /** 
     * 企业名称
     */
    public String F04;

    /** 
     * 企业纳税号
     */
    public String F05;

    /** 
     * 组织机构代码
     */
    public String F06;

    /** 
     * 注册年份
     */
    public int F07;

    /** 
     * 注册资金
     */
    public BigDecimal F08 = new BigDecimal(0);

    /** 
     * 行业
     */
    public String F09;

    /** 
     * 企业规模,单位: 人
     */
    public int F10;

    /** 
     * 法人
     */
    public String F11;

    /** 
     * 法人身份证号,9-16位星号替换
     */
    public String F12;

    /** 
     * 法人身份证号,加密存储
     */
    public String F13;

    /** 
     * 资产净值
     */
    public BigDecimal F14 = new BigDecimal(0);

    /** 
     * 上年度经营现金流入
     */
    public BigDecimal F15 = new BigDecimal(0);

    /** 
     * 企业简介
     */
    public String F16;

    /** 
     * 经营情况
     */
    public String F17;

    /** 
     * 涉诉情况
     */
    public String F18;

    /** 
     * 征信情况
     */
    public String F19;

}
