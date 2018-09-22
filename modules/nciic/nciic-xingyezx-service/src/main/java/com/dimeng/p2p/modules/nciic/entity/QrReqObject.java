package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  QrReqObject.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
@XmlRootElement(name = "reqQueryResult")
public class QrReqObject
{
    private String queryId;
    
    public String getQueryId()
    {
        return queryId;
    }
    
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }
}
