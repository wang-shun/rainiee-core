package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  ReqObjectEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
@XmlRootElement(name = "reqObject")
public class ReqObjectEntity
{
    
    /**
     * 渠道编号
     */
    private String channelNo;
    
    /**
     * 操作用户。
     */
    private String querier;
    
    /**
     * 查询原因
     */
    private String queryReason;
    
    /**
     * 机构名称
     */
    private String orgName;
    
    /**
     * 结果取回方式
     */
    private String getBackWay;
    
    private String reserve1;
    
    private String reserve2;
    
    private String reserve3;
    
    private String reserve4;
    
    /**
     * 被查询人信息
     */
    private QueryListEntity queryList;
    
    public String getChannelNo()
    {
        return channelNo;
    }
    
    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }
    
    public String getQuerier()
    {
        return querier;
    }
    
    public void setQuerier(String querier)
    {
        this.querier = querier;
    }
    
    public String getQueryReason()
    {
        return queryReason;
    }
    
    public void setQueryReason(String queryReason)
    {
        this.queryReason = queryReason;
    }
    
    public String getOrgName()
    {
        return orgName;
    }
    
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
    
    public String getGetBackWay()
    {
        return getBackWay;
    }
    
    public void setGetBackWay(String getBackWay)
    {
        this.getBackWay = getBackWay;
    }
    
    public String getReserve1()
    {
        return reserve1;
    }
    
    public void setReserve1(String reserve1)
    {
        this.reserve1 = reserve1;
    }
    
    public String getReserve2()
    {
        return reserve2;
    }
    
    public void setReserve2(String reserve2)
    {
        this.reserve2 = reserve2;
    }
    
    public String getReserve3()
    {
        return reserve3;
    }
    
    public void setReserve3(String reserve3)
    {
        this.reserve3 = reserve3;
    }
    
    public String getReserve4()
    {
        return reserve4;
    }
    
    public void setReserve4(String reserve4)
    {
        this.reserve4 = reserve4;
    }
    
    public QueryListEntity getQueryList()
    {
        return queryList;
    }
    
    public void setQueryList(QueryListEntity queryList)
    {
        this.queryList = queryList;
    }
    
}
