/*
 * 文 件 名:  BaseInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月29日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 基本配置信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月29日]
 */
public class BaseInfo  implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6307781559588071852L;

    /**
     * 是否必须邮箱认证  true: 需要 false: 不需要
     */
    private String isNeedEmailRZ;
    
    /**
     * 托管前缀
     */
    private String prefix;
    
    public String getIsNeedEmailRZ()
    {
        return isNeedEmailRZ;
    }
    
    public void setIsNeedEmailRZ(String isNeedEmailRZ)
    {
        this.isNeedEmailRZ = isNeedEmailRZ;
    }
    
    public String getPrefix()
    {
        return prefix;
    }
    
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "BaseInfo [isNeedEmailRZ=" + isNeedEmailRZ + ", prefix=" + prefix + "]";
    }
    
    
}
