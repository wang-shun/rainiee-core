package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 积分商城查询条件：启用商品类型
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月23日]
 */

public class ScoreMallType implements Serializable {

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 4674951487292753546L;
    
    /**
	 * 商品类别Id
	 */
	private int id;
	
	 /**
     * 商品类别名称
     */
    private String name;
    
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ScoreMallType [id=" + id + ", name=" + name + "]";
    }
	
}
