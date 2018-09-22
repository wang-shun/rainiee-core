/*
 * 文 件 名:  SpreadInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 被推荐人员列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月14日]
 */
public class SpreadInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -9191564895267582876L;

    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 注册时间
     */
    private String registerTime;
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getRegisterTime()
    {
        return registerTime;
    }
    
    public void setRegisterTime(String registerTime)
    {
        this.registerTime = registerTime;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "SpreadInfo [userName=" + userName + ", registerTime=" + registerTime + "]";
    }
    
}
