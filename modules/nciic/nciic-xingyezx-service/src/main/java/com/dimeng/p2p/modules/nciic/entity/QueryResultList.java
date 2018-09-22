package com.dimeng.p2p.modules.nciic.entity;

import java.util.List;

/*
 * 文 件 名:  QueryResultList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月21日
 */
public class QueryResultList
{
    private List<QueryResult> queryResult;
    
    public List<QueryResult> getQueryResult()
    {
        return queryResult;
    }
    
    public void setQueryResult(List<QueryResult> queryResult)
    {
        this.queryResult = queryResult;
    }
}
