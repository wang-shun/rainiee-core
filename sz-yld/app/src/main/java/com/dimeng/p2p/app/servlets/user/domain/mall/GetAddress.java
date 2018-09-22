/*
 * 文 件 名:  GetUserScoreInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 用户地址管理
 * 
 * @author  Jason
 * @version  [版本号, 2016年2月25日]
 */
public class GetAddress implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1584075868558063112L;

    /**
     * 主键Id
     */
    public int id;

    /**
     * 用户ID,参考T6110.F01
     */
    public int userid;
    
    /**
     * 收件人
     */
    private String name;
    
    /**
     * 收件人电话
     */
    private String phone;
    
    /**
     * 地区ID
     */
    private int regionID;
    
    /**
     * 地区ID
     */
    private String region;
    
    /**
     * 收件人地址
     */
    private String address;
    
    /**
     * 是否为默认地址
     */
    public YesOrNo yesOrNo;
    
    /**
     * 邮编
     */
    private String vo;
    
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public int getUserid() 
	{
		return userid;
	}
	
	public void setUserid(int userid) 
	{
		this.userid = userid;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getPhone() 
	{
		return phone;
	}
	
	public void setPhone(String phone) 
	{
		this.phone = phone;
	}
	
	public int getRegionID() 
	{
		return regionID;
	}
	
	public void setRegionID(int regionID) 
	{
		this.regionID = regionID;
	}
	
	public String getRegion() 
	{
		return region;
	}
	public void setRegion(String region) 
	{
		this.region = region;
	}
	
	public String getAddress() 
	{
		return address;
	}
	
	public void setAddress(String address) 
	{
		this.address = address;
	}
	
	public YesOrNo getYesOrNo() 
	{
		return yesOrNo;
	}
	
	public void setYesOrNo(YesOrNo yesOrNo) 
	{
		this.yesOrNo = yesOrNo;
	}
	
	public String getVo() 
	{
		return vo;
	}
	
	public void setVo(String vo) 
	{
		this.vo = vo;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "GetAddress [id=" + id + ", userid=" + userid + ", name=" + name + ", phone=" + phone + ", regionID="
            + regionID + ", region=" + region + ", address=" + address + ", yesOrNo=" + yesOrNo + ", vo=" + vo + "]";
    }
	
}
