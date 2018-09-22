package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 积分商城查询条件：积分范围
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月24日]
 */

public class ScoreRange implements Serializable {

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 3868815545482009014L;
    
    /**
	 * 积分Id
	 */
	private int id;
	
	 /**
     * 积分最小值
     */
    private String minScore;
    
	/**
	 * 积分最大值
	 */
	private String maxScore;
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getMinScore() 
	{
		return minScore;
	}
	
	public void setMinScore(String minScore)
	{
		this.minScore = minScore;
	}
	
	public String getMaxScore()
	{
		return maxScore;
	}
	
	public void setMaxScore(String maxScore) 
	{
		this.maxScore = maxScore;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ScoreRange [id=" + id + ", minScore=" + minScore + ", maxScore=" + maxScore + "]";
    }
	
}
