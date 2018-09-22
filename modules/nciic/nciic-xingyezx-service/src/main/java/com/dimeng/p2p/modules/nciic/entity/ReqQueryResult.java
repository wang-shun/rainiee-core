package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  ReqQueryResult.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月21日
 */
@XmlRootElement(name = "reqQueryResult")
public class ReqQueryResult
{
    private String queryId;
    
    private String applyId;
    
    private String respTime;
    
    private QueryResultList queryResultList;
    
    public String getQueryId()
    {
        return queryId;
    }
    
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }
    
    public String getApplyId()
    {
        return applyId;
    }
    
    public void setApplyId(String applyId)
    {
        this.applyId = applyId;
    }
    
    public String getRespTime()
    {
        return respTime;
    }
    
    public void setRespTime(String respTime)
    {
        this.respTime = respTime;
    }
    
    public QueryResultList getQueryResultList()
    {
        return queryResultList;
    }
    
    public void setQueryResultList(QueryResultList queryResultList)
    {
        this.queryResultList = queryResultList;
    }
}
