/*
 * 文 件 名:  XyrzTotal.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年8月19日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

/**
 * 信用认证数量统计实体
 * @author  huqinfu
 * @version  [版本号, 2016年8月19日]
 */
public class XyrzTotal
{
    /**
     * 必要认证
     */
    public int needAttestation;
    
    /**
     * 必要认证通过
     */
    public int byrztg;
    
    /**
     * 可选认证
     */
    public int notNeedAttestation;
    
    /**
     * 可选认证通过
     */
    public int kxrztg;
}
