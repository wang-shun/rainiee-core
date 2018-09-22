/*
 * 文 件 名:  MySpreadInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月26日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;
import java.util.List;

import com.dimeng.p2p.account.user.service.entity.SpreadTotle;

/**
 * 推荐信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月26日]
 */
public class MySpreadInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1111989655707707581L;

    /**
     * 推广码
     */
    private String tgm;
    
    /**
     * 推广信息
     */
    private String msg;
    
    /**
     * 好友最低充值下限
     */
    private String czjs;
    
    /**
     * 每次邀请成功奖励
     */
    private String tghl;
    
    /**
     * 每月推荐上限
     */
    private String tgsx;
    
    /**
     * 好友投资下限
     */
    private String tzcs;
    
    /**
     * 投资奖励
     */
    private String tzjl;
    
    /**
     * 推广统计
     */
    private SpreadTotle spreadTotle;
    
    /**
     * 获取推荐列表
     */
    private List<SpreadInfo> spreadEntitys;
    
    public String getTgm()
    {
        return tgm;
    }
    
    public void setTgm(String tgm)
    {
        this.tgm = tgm;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public String getCzjs()
    {
        return czjs;
    }
    
    public void setCzjs(String czjs)
    {
        this.czjs = czjs;
    }
    
    public String getTghl()
    {
        return tghl;
    }
    
    public void setTghl(String tghl)
    {
        this.tghl = tghl;
    }
    
    public String getTgsx()
    {
        return tgsx;
    }
    
    public void setTgsx(String tgsx)
    {
        this.tgsx = tgsx;
    }
    
    public String getTzcs()
    {
        return tzcs;
    }
    
    public void setTzcs(String tzcs)
    {
        this.tzcs = tzcs;
    }
    
    public String getTzjl()
    {
        return tzjl;
    }
    
    public void setTzjl(String tzjl)
    {
        this.tzjl = tzjl;
    }
    
    public SpreadTotle getSpreadTotle()
    {
        return spreadTotle;
    }
    
    public void setSpreadTotle(SpreadTotle spreadTotle)
    {
        this.spreadTotle = spreadTotle;
    }
    
    public List<SpreadInfo> getSpreadEntitys()
    {
        return spreadEntitys;
    }
    
    public void setSpreadEntitys(List<SpreadInfo> spreadEntitys)
    {
        this.spreadEntitys = spreadEntitys;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MySpreadInfo [tgm=" + tgm + ", msg=" + msg + ", czjs=" + czjs + ", tghl=" + tghl + ", tgsx=" + tgsx
            + ", tzcs=" + tzcs + ", tzjl=" + tzjl + ", spreadTotle=" + spreadTotle + ", spreadEntitys=" + spreadEntitys
            + "]";
    }
    
}
