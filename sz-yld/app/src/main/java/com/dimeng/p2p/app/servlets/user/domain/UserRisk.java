/*
 * 文 件 名:  UserRisk.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月22日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 用户风控信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class UserRisk implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -295179465037639152L;

    /**
     * 用户风险评估成绩
     */
    private int score;
    
    /**
     * 用户风险评估类型
     */
    private String riskType;
    
    /**
     * 用户剩余风险评估次数
     */
    private int riskTimes;
    
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int score)
    {
        this.score = score;
    }
    
    public String getRiskType()
    {
        return riskType;
    }
    
    public void setRiskType(String riskType)
    {
        this.riskType = riskType;
    }
    
    public int getRiskTimes()
    {
        return riskTimes;
    }
    
    public void setRiskTimes(int riskTimes)
    {
        this.riskTimes = riskTimes;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "UserRisk [score=" + score + ", riskType=" + riskType + ", riskTimes=" + riskTimes + "]";
    }
}
