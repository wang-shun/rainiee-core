/*
 * 文 件 名:  ScoreRuleInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月29日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 积分规则和说明
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月29日]
 */
public class ScoreRuleInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3210674573351218569L;

    /**
     * 积分说明
     */
    private String scoreDesc;
    
    /**
     * 积分规则
     */
    private String scoreRule;
    
    public String getScoreDesc()
    {
        return scoreDesc;
    }
    
    public void setScoreDesc(String scoreDesc)
    {
        this.scoreDesc = scoreDesc;
    }
    
    public String getScoreRule()
    {
        return scoreRule;
    }
    
    public void setScoreRule(String scoreRule)
    {
        this.scoreRule = scoreRule;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ScoreRuleInfo [scoreDesc=" + scoreDesc + ", scoreRule=" + scoreRule + "]";
    }
    
}
