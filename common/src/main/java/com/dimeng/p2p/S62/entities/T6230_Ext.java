package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_Ext_F04;

/*
 * 文 件 名:  T6230_Ext.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  自动还款授权
 * 修 改 人:  wangming
 * 修改时间:  2014年12月25日
 */
public class T6230_Ext extends AbstractEntity
{
    /**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	private int F01;
    
    private int F02;
    
    private int F03;
    
    private T6230_Ext_F04 F04;
    
    private String F05;
    
    public int getF01()
    {
        return F01;
    }
    
    public void setF01(int f01)
    {
        F01 = f01;
    }
    
    public int getF02()
    {
        return F02;
    }
    
    public void setF02(int f02)
    {
        F02 = f02;
    }
    
    public int getF03()
    {
        return F03;
    }
    
    public void setF03(int f03)
    {
        F03 = f03;
    }
    
    public T6230_Ext_F04 getF04()
    {
        return F04;
    }
    
    public void setF04(T6230_Ext_F04 f04)
    {
        F04 = f04;
    }
    
    public String getF05()
    {
        return F05;
    }
    
    public void setF05(String f05)
    {
        F05 = f05;
    }
    
}
