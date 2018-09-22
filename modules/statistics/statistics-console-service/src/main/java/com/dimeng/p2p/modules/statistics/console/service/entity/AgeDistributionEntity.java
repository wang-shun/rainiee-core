package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;

/**
 * 投资/借款用户分布、平台用户性别、平台用户年龄、平台注册用户终端
 */
public class AgeDistributionEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 属性名称
     */
    public String ageRanage;
    
    /**
     * 属性数据
     */
    public int number;
}
