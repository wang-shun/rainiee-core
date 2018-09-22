/*
 * 文 件 名:  OAuthInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月13日
 */
package com.dimeng.p2p.app.servlets.platinfo.domain;

import java.io.Serializable;

/**
 * 用户鉴权信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月13日]
 */
public class OAuthInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8221410170625353472L;
    

    private String accessToken;
    
    private int expiresIn;
    
    private String refreshToken;
    
    private String openId;

    private String scope;
    
    public String getAccessToken()
    {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
    
    public int getExpiresIn()
    {
        return expiresIn;
    }
    
    public void setExpiresIn(int expiresIn)
    {
        this.expiresIn = expiresIn;
    }
    
    public String getRefreshToken()
    {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }
    
    public String getOpenId()
    {
        return openId;
    }
    
    public void setOpenId(String openId)
    {
        this.openId = openId;
    }
    
    public String getScope()
    {
        return scope;
    }
    
    public void setScope(String scope)
    {
        this.scope = scope;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "OAuthInfo [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", refreshToken=" + refreshToken
            + ", openId=" + openId + ", scope=" + scope + "]";
    }
    
    
}
